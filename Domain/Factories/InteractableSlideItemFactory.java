package Domain.Factories;

import Domain.Commands.Command;
import Domain.Entities.InteractableSlideItem;
import Domain.Entities.Presentation;
import Domain.Entities.SlideItem;

public class InteractableSlideItemFactory implements SlideItemFactory{

	Command command;
	Presentation presentation;
	SlideItem baseItem;

    /**
     * Ctor
     */
	public InteractableSlideItemFactory(Presentation presentation) {
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
    @Override
    public SlideItem createSlideItem() {
        return new InteractableSlideItem(baseItem, command);
    }
}
