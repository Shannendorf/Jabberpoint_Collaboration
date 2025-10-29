package Application;

import Domain.InteractableSlideItem;
import Domain.Presentation;
import Domain.SlideItem;

public class InteractableSlideItemBuilder {

	Command command;
	Presentation presentation;
	SlideItem baseItem;

    /**
     * Ctor
     */
	public InteractableSlideItemBuilder(Presentation presentation) {
        this.presentation = presentation;
	}

    /**
     * Zet Command dat uitgevoerd wordt door interactie
     */
	public void setCommand(Command command) {
		this.command = command;
	}

    /**
     * Zet base item waar de interactie op gebeurt
     */
	public void setBaseItem(SlideItem item) {
		baseItem = item;
	}

    /**
     * Maak Interactable Slide Item aan
     */
	public SlideItem createSlideItem() {
		return new InteractableSlideItem(baseItem, command);
	}
}
