package Application;

import Domain.Presentation;

public class NextSlideCommand extends PresentationCommand {

    /**
     * Ctor
     */
    public NextSlideCommand(Presentation presentation) {
        super(presentation);
    }

    /**
     * Voer Next Slide Command uit
     */
    @Override
    public void execute() {
        presentation.nextSlide();
    }
}
