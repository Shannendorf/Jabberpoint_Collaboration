package Application;

import Domain.*;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DefaultSlideItemFactory implements SlideItemFactory {

    private final Presentation presentation;

    public DefaultSlideItemFactory(Presentation presentation) {
        this.presentation = presentation;
    }

    @Override
    public SlideItem createSlideItem(Element element) {
        String tag = element.getTagName();

        switch (tag) {
            case "text" -> {
                int level = Integer.parseInt(element.getAttribute("level"));
                return new TextItem(level, element.getTextContent());
            }
            case "image" -> {
                int level = Integer.parseInt(element.getAttribute("level"));
                return new BitmapItem(level, element.getTextContent());
            }
            case "action" -> {
                CompositeCommand compositeCommand = new CompositeCommand();

                String actionName = element.getAttribute("name");
                if (!actionName.isEmpty()) {
                    Command mainCommand = mapActionNameToCommand(actionName);
                    if (mainCommand != null) {
                        compositeCommand.add(mainCommand);
                    }
                }

                NodeList children = element.getChildNodes();
                SlideItem childItem = null;

                for (int i = 0; i < children.getLength(); i++) {
                    if (children.item(i) instanceof Element childElement) {
                    	
                        String recursiveTag = childElement.getTagName();

                        if (recursiveTag.equals("action")) {
                            SlideItem nestedClickable = createSlideItem(childElement);
                            if (nestedClickable instanceof ClickableSlideItem clickable) {
                                compositeCommand.add(clickable.getCommand());
                                childItem = clickable.getChild();
                            }
                        } else {
                            childItem = createSlideItem(childElement);
                        }
                    }
                }
            }
        }
        return null;
    }

    private Command mapActionNameToCommand(String actionName) {
        return switch (actionName.toLowerCase()) {
            case "next" -> new NextSlideCommand(presentation);
            case "prev" -> new PreviousSlideCommand(presentation);
            case "first" -> new FirstSlideCommand(presentation);
            case "last" -> new LastSlideCommand(presentation);
            case "beep" -> new PlaySoundCommand(presentation, "");
            default -> null;
        };
    }
}