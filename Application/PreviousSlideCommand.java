package Application;

import Domain.Presentation;

public class PreviousSlideCommand extends PresentationCommand {

    public PreviousSlideCommand(Presentation presentation) {
        super(presentation);
    }

    @Override
    public void execute() {
        presentation.prevSlide();
    }
}
