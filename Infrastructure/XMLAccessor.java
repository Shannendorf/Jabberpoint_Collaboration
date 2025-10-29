package Infrastructure;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Application.*;
import Domain.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


/** Infrastructure.XMLAccessor, reads and writes XML files
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class XMLAccessor extends Accessor {

    /** Default API to use. */
    protected static final String DEFAULT_API_TO_USE = "dom";

    /** namen van xml tags of attributen */
    protected static final String SHOWTITLE = "showtitle";
    protected static final String SLIDETITLE = "title";
    protected static final String SLIDE = "slide";
    protected static final String ITEMS = "items";
    protected static final String LEVEL = "level";
    protected static final String KIND = "kind";
    protected static final String TEXT = "text";
    protected static final String IMAGE = "image";

    /** tekst van messages */
    protected static final String PCE = "Parser Configuration Exception";
    protected static final String UNKNOWNTYPE = "Unknown Element type";
    protected static final String NFE = "Number Format Exception";

    /**
     * Hulpmethode om de tekstinhoud van een XML-tag op te halen.
     */
    private String getTitle(Element element, String tagName) {
        NodeList titles = element.getElementsByTagName(tagName);
        return titles.item(0).getTextContent();

    }

    /**
     * Laadt een presentatie uit een XML-bestand.
     * Gebruikt de DOM-parser om de structuur te lezen en vertaalt deze
     * naar Slide- en SlideItem-objecten.
     */
    public void loadFile(Presentation presentation, String filename) throws IOException {
        try {
            // Maak een DOM-document aan vanuit het bestand
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File(filename));
            Element doc = document.getDocumentElement();

            // Set de titel van de presentatie
            presentation.setTitle(getTitle(doc, SHOWTITLE));

            // Lees alle <slide>-elementen
            NodeList slides = doc.getElementsByTagName(SLIDE);
            for (int slideIndex = 0; slideIndex < slides.getLength(); slideIndex++) {
                Element xmlSlide = (Element) slides.item(slideIndex);

                // Maak een nieuwe Slide aan via de SlideFactory
                var slideFactory = OrdinarySlideFactory.getFactory();
                var slideBuilder = slideFactory.createBuilder();
                slideBuilder.setTitle(getTitle(xmlSlide, SLIDETITLE));

                // Zoek de <items>-container binnen de slide
                NodeList itemsNodes = xmlSlide.getElementsByTagName(ITEMS);
                if (itemsNodes.getLength() > 0) {
                    Element itemsContainer = (Element) itemsNodes.item(0);
                    NodeList children = itemsContainer.getChildNodes();

                    // Loop door alle items binnen <items>
                    for (int i = 0; i < children.getLength(); i++) {
                        if (children.item(i) instanceof Element childElement) {
                            SlideItem item = createSlideItem(presentation, childElement);
                            if (item != null) {
                                slideBuilder.addItem(item);
                            }
                        }
                    }
                }

                // Voeg de gemaakte slide toe aan de presentatie
                presentation.append(slideBuilder.createInstance());
            }

        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Failed to load XML presentation: " + filename, e);
        }
    }

    /**
     * Maakt een SlideItem op basis van het XML-elementtype.
     * Ondersteunt tekst, afbeeldingen en action-elementen (InteractableSlideItem).
     */
    private SlideItem createSlideItem(Presentation presentation, Element element) {
        String tag = element.getTagName();

        switch (tag) {
            case "text" -> {
                int level = parseLevel(element);
                String content = element.getTextContent();
                return new TextItemFactory().createSlideItem(content, level);
            }
            case "image" -> {
                int level = parseLevel(element);
                String content = element.getTextContent();
                return new BitmapItemFactory().createSlideItem(content, level);
            }
            case "action" -> {
                // Maak een interactief slide-item aan dat een of meerdere acties kan bevatten
                InteractableSlideItemBuilder builder = new InteractableSlideItemBuilder(presentation);
                CompositeCommand compositeCommand = new CompositeCommand();

                // Verzamel alle geneste actions en vind de basis-slideitem (tekst of afbeelding)
                collectActionsAndBase(element, builder, compositeCommand, presentation);
                builder.setCommand(compositeCommand);

                return builder.createSlideItem();
            }

            default -> {
                System.err.println("Unknown tag: " + tag);
                return null;
            }
        }
    }

    /**
     * Recursieve methode die geneste <action>-elementen doorloopt,
     * hun commando’s verzamelt en het basisitem identificeert.
     */
    private void collectActionsAndBase(Element element, InteractableSlideItemBuilder builder, CompositeCommand compositeCommand, Presentation presentation) {

        // Lees de 'name'-attributen van action en map naar Command
        String actionName = element.getAttribute("name");
        if (actionName != null && !actionName.isEmpty()) {
            Command command = mapActionNameToCommand(actionName, presentation);
            compositeCommand.add(command);
        }

        // Doorloop de children van dit element
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (!(node instanceof Element childElement)) continue;

            String tag = childElement.getTagName();

            // Indien opnieuw een <action>, ga recursief verder
            if ("action".equalsIgnoreCase(tag)) {
                collectActionsAndBase(childElement, builder, compositeCommand, presentation);
            } else {
                // Anders is dit het basis SlideItem (bv. <text> of <image>)
                SlideItem base = createSlideItem(presentation, childElement);
                builder.setBaseItem(base);
            }
        }
    }

    /**
     * Zet een actie-naam om naar het juiste Command-object.
     */
    private Command mapActionNameToCommand(String actionName, Presentation presentation) {
        return switch (actionName.toLowerCase()) {
            case "next" -> new NextSlideCommand(presentation);
            case "prev" -> new PreviousSlideCommand(presentation);
            case "first" -> new FirstSlideCommand(presentation);
            case "last" -> new LastSlideCommand(presentation);
            case "beep" -> new PlaySoundCommand(presentation, "blip.wav");
            default -> null;
        };
    }

    /**
     * Probeert het 'level'-attribuut uit te lezen; standaard = 1 bij fouten.
     */
    private int parseLevel(Element element) {
        String levelAttr = element.getAttribute("level");
        try {
            return (levelAttr == null || levelAttr.isEmpty()) ? 1 : Integer.parseInt(levelAttr);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    /**
     * Schrijft een presentatie naar een XML-bestand in het juiste JabberPoint-formaat.
     */
    @Override
    public void saveFile(Presentation presentation, String filename) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            out.println("<?xml version=\"1.0\"?>");
            out.println("<!DOCTYPE presentation SYSTEM \"jabberpoint.dtd\">");
            out.println("<presentation>");
            out.printf("    <showtitle>%s</showtitle>%n", escapeXml(presentation.getTitle()));

            for (int slideIndex = 0; slideIndex < presentation.getSize(); slideIndex++) {
                Slide slide = presentation.getSlide(slideIndex);

                out.println();
                out.printf("    <!-- Slide %d -->%n", slideIndex + 1);
                out.println("    <slide>");
                out.printf("        <title>%s</title>%n", escapeXml(slide.getTitle()));
                out.println("        <items>");

                for (SlideItem item : slide.getSlideItems()) {
                    writeSlideItem(out, item, "            ");
                }

                out.println("        </items>");
                out.println("    </slide>");
            }

            out.println("</presentation>");
        }
    }

    /**
     * Schrijft één SlideItem (tekst, afbeelding of interactief element).
     * Indien een InteractableSlideItem → roept recursieve writeActions() aan.
     */
    private void writeSlideItem(PrintWriter out, SlideItem item, String indent) {
        if (item instanceof TextItem textItem) {
            out.printf("%s<text level=\"%d\">%s</text>%n",
                    indent,
                    textItem.getLevel(),
                    escapeXml(textItem.getText()));
        } else if (item instanceof BitmapItem imageItem) {
            out.printf("%s<image level=\"%d\">%s</image>%n",
                    indent,
                    imageItem.getLevel(),
                    escapeXml(imageItem.getName()));
        } else if (item instanceof InteractableSlideItem interactable) {
            // Verzamel alle commands uit dit InteractableSlideItem
            ArrayList<Command> commands = new ArrayList<>();

            if (interactable.getCommand() instanceof CompositeCommand composite) {
                commands.addAll(composite.getCommands());
            } else if (interactable.getCommand() != null) {
                commands.add(interactable.getCommand());
            }

            // Child-item (tekst, afbeelding, of opnieuw interactable)
            SlideItem child = interactable.getChild();

            // Schrijf geneste <action>-tags
            writeActions(out, commands, child, indent);
        } else {
            System.err.println("Ignoring unknown slide item type: " + item.getClass().getSimpleName());
        }
    }

    /**
     * Schrijft geneste <action> elementen recursief, met correcte inspringing.
     * Gebruikt een ArrayList om meerdere commando's te ondersteunen.
     */
    private void writeActions(PrintWriter out, ArrayList<Command> commands, SlideItem child, String indent) {
        if (commands.isEmpty()) {
            if (child != null) {
                writeSlideItem(out, child, indent);
            }
            return;
        }

        // Neem de eerste command en schrijf de openende tag
        Command command = commands.getFirst();
        String actionName = mapCommandToActionName(command);

        out.printf("%s<action name=\"%s\">%n", indent, escapeXml(actionName));

        // Schrijf de rest van de commando’s en het kind met extra inspringing
        ArrayList<Command> remaining = new ArrayList<>(commands.subList(1, commands.size()));
        writeActions(out, remaining, child, indent + "    ");

        // Sluit de action-tag
        out.printf("%s</action>%n", indent);
    }

    /**
     * Zet Command-objecten om naar de corresponderende XML-actionnaam.
     */
    private String mapCommandToActionName(Command cmd) {
        if (cmd instanceof NextSlideCommand) return "next";
        if (cmd instanceof PreviousSlideCommand) return "prev";
        if (cmd instanceof FirstSlideCommand) return "first";
        if (cmd instanceof LastSlideCommand) return "last";
        if (cmd instanceof PlaySoundCommand) return "beep";
        return "unknown";
    }

    /**
     * Maakt tekst XML-veilig door speciale karakters te escapen.
     */
    private String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
