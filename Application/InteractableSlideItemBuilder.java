package Application;

import Domain.InteractableSlideItem;
import Domain.Presentation;
import Domain.SlideItem;

public class InteractableSlideItemBuilder {

	Command command;
	Presentation presentation;
	SlideItem baseItem;
	
	public InteractableSlideItemBuilder(Presentation presentation) {
        this.presentation = presentation;
	}
	
	public void setCommand(Command command) {
		this.command = command;
	}
	
	public void setBaseItem(SlideItem item) {
		baseItem = item;
	}
	
	public SlideItem createSlideItem() {
		return new InteractableSlideItem(baseItem, command);
	}
}
