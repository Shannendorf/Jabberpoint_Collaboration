package Application;

import Domain.Presentation;

public class PreviousSlideCommand extends PresentationCommand {

    /**
     * Ctor
     */
    public PreviousSlideCommand(Presentation presentation) {
        super(presentation);
    }

    /**
     * Voer het Previous Slide Command uit
     */
    @Override
    public void execute() {
        presentation.prevSlide();
    }
}
