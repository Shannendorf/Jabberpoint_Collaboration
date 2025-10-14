package Application;

import Domain.Presentation;

public class NextSlideCommand extends PresentationCommand {
	public NextSlideCommand(Presentation presentation) {
        super(presentation);
    }

    @Override
    public void execute() {
        presentation.nextSlide();
    }
}
