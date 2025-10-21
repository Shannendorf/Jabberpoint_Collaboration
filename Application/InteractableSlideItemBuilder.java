package Application;

import Domain.InteractableSlideItem;
import Domain.Presentation;
import Domain.SlideItem;

public class InteractableSlideItemBuilder {

	CompositeCommand compositeCommand;
	Presentation presentation;
	SlideItem baseItem;
	
	public InteractableSlideItemBuilder(Presentation presentation) {
		compositeCommand = new CompositeCommand();
	}
	
	public void addCommand(String name) {
		Command command = mapActionNameToCommand(name);
		compositeCommand.add(command);
	}
	
	public void addBaseItem(SlideItem item) {
		baseItem = item;
	}
	
	public SlideItem createSlideItem() {
		return new InteractableSlideItem(baseItem, compositeCommand);
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
