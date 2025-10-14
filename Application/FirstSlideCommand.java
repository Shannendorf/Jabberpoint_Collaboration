package Application;

import Domain.Presentation;

public class FirstSlideCommand extends PresentationCommand {

    public FirstSlideCommand(Presentation presentation) {
        super(presentation);
    }

    @Override
    public void execute() {
        presentation.setSlideNumber(0);
    }
}
