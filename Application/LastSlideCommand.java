package Application;

import Domain.Presentation;

public class LastSlideCommand extends PresentationCommand {

    /**
     * Ctor
     */
    public LastSlideCommand(Presentation presentation) {
        super(presentation);
    }

    /**
     * Voer Last Slide Command uit
     */
    @Override
    public void execute() {
        presentation.setSlideNumber(presentation.getSlideNumber()-1);
    }
}
