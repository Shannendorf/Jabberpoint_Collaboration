package Infrastructure;

import java.util.Vector;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Application.BitmapItemFactory;
import Application.InteractableSlideItemBuilder;
import Application.OrdinarySlideFactory;
import Application.SlideBuilder;
import Application.SlideItemFactory;
import Application.TextItemFactory;
import Domain.*;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;


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

                String actionName = element.getAttribute("name");
                if (!actionName.isEmpty()) {
                    builder.addCommand(actionName);
                }

                NodeList children = element.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    if (children.item(i) instanceof Element childElement) {
                        SlideItem childItem = createSlideItem(presentation, childElement);
                        builder.addBaseItem(childItem);
                    }
                }

                return builder.createSlideItem();
            }
            default -> {
                System.err.println("Unknown tag: " + tag);
                return null;
            }
        }
    }

    private int parseLevel(Element element) {
        String levelAttr = element.getAttribute("level");
        try {
            return (levelAttr == null || levelAttr.isEmpty()) ? 1 : Integer.parseInt(levelAttr);
        } catch (NumberFormatException e) {
            return 1; // fallback if malformed
        }
    }

    public void saveFile(Presentation presentation, String filename) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(filename));
        out.println("<?xml version=\"1.0\"?>");
        out.println("<!DOCTYPE presentation SYSTEM \"jabberpoint.dtd\">");
        out.println("<presentation>");
        out.print("<showtitle>");
        out.print(presentation.getTitle());
        out.println("</showtitle>");
        for (int slideNumber=0; slideNumber<presentation.getSize(); slideNumber++) {
            Slide slide = presentation.getSlide(slideNumber);
            out.println("<slide>");
            out.println("<title>" + slide.getTitle() + "</title>");
            Vector<SlideItem> slideItems = slide.getSlideItems();
            for (int itemNumber = 0; itemNumber<slideItems.size(); itemNumber++) {
                SlideItem slideItem = (SlideItem) slideItems.elementAt(itemNumber);
                out.print("<item kind=");
                if (slideItem instanceof TextItem) {
                    out.print("\"text\" level=\"" + slideItem.getLevel() + "\">");
                    out.print( ( (TextItem) slideItem).getText());
                }
                else {
                    if (slideItem instanceof BitmapItem) {
                        out.print("\"image\" level=\"" + slideItem.getLevel() + "\">");
                        out.print( ( (BitmapItem) slideItem).getName());
                    }
                    else {
                        System.out.println("Ignoring " + slideItem);
                    }
                }
                out.println("</item>");
            }
            out.println("</slide>");
        }
        out.println("</presentation>");
        out.close();
    }
}
