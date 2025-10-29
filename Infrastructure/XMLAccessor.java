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


    private String getTitle(Element element, String tagName) {
        NodeList titles = element.getElementsByTagName(tagName);
        return titles.item(0).getTextContent();

    }

    public void loadFile(Presentation presentation, String filename) throws IOException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File(filename));
            Element doc = document.getDocumentElement();

            presentation.setTitle(getTitle(doc, SHOWTITLE));

            NodeList slides = doc.getElementsByTagName(SLIDE);
            for (int slideIndex = 0; slideIndex < slides.getLength(); slideIndex++) {
                Element xmlSlide = (Element) slides.item(slideIndex);

                var slideFactory = OrdinarySlideFactory.getFactory();
                var slideBuilder = slideFactory.createBuilder();
                slideBuilder.setTitle(getTitle(xmlSlide, SLIDETITLE));

                NodeList itemsNodes = xmlSlide.getElementsByTagName(ITEMS);
                if (itemsNodes.getLength() > 0) {
                    Element itemsContainer = (Element) itemsNodes.item(0);
                    NodeList children = itemsContainer.getChildNodes();

                    for (int i = 0; i < children.getLength(); i++) {
                        if (children.item(i) instanceof Element childElement) {
                            SlideItem item = createSlideItem(presentation, childElement);
                            if (item != null) {
                                slideBuilder.addItem(item);
                            }
                        }
                    }
                }

                presentation.append(slideBuilder.createInstance());
            }

        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Failed to load XML presentation: " + filename, e);
        }
    }

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
                InteractableSlideItemBuilder builder = new InteractableSlideItemBuilder(presentation);
                CompositeCommand compositeCommand = new CompositeCommand();

                // recursively collect commands and find the base item
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

    private void collectActionsAndBase(Element element, InteractableSlideItemBuilder builder, CompositeCommand compositeCommand, Presentation presentation) {
        String actionName = element.getAttribute("name");
        if (actionName != null && !actionName.isEmpty()) {
            Command command = mapActionNameToCommand(actionName, presentation);
            compositeCommand.add(command);
        }

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (!(node instanceof Element childElement)) continue;

            String tag = childElement.getTagName();

            if ("action".equalsIgnoreCase(tag)) {
                collectActionsAndBase(childElement, builder, compositeCommand, presentation);
            } else {
                SlideItem base = createSlideItem(presentation, childElement);
                builder.setBaseItem(base);
            }
        }
    }

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


    private int parseLevel(Element element) {
        String levelAttr = element.getAttribute("level");
        try {
            return (levelAttr == null || levelAttr.isEmpty()) ? 1 : Integer.parseInt(levelAttr);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

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
            ArrayList<Command> commands = new ArrayList<>();

            if (interactable.getCommand() instanceof CompositeCommand composite) {
                commands.addAll(composite.getCommands());
            } else if (interactable.getCommand() != null) {
                commands.add(interactable.getCommand());
            }

            SlideItem child = interactable.getChild();

            writeActions(out, commands, child, indent);
        } else {
            System.err.println("Ignoring unknown slide item type: " + item.getClass().getSimpleName());
        }
    }

    private void writeActions(PrintWriter out, ArrayList<Command> commands, SlideItem child, String indent) {
        if (commands.isEmpty()) {
            if (child != null) {
                writeSlideItem(out, child, indent);
            }
            return;
        }

        Command command = commands.getFirst();
        String actionName = mapCommandToActionName(command);

        out.printf("%s<action name=\"%s\">%n", indent, escapeXml(actionName));

        ArrayList<Command> remaining = new ArrayList<>(commands.subList(1, commands.size()));
        writeActions(out, remaining, child, indent + "    ");

        out.printf("%s</action>%n", indent);
    }

    private String mapCommandToActionName(Command cmd) {
        if (cmd instanceof NextSlideCommand) return "next";
        if (cmd instanceof PreviousSlideCommand) return "prev";
        if (cmd instanceof FirstSlideCommand) return "first";
        if (cmd instanceof LastSlideCommand) return "last";
        if (cmd instanceof PlaySoundCommand) return "beep";
        return "unknown";
    }

    private String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
