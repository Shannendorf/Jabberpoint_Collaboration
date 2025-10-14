package Application;

import Domain.Presentation;

public class LastSlideCommand extends PresentationCommand {


    public LastSlideCommand(Presentation presentation) {
        super(presentation);
    }

    @Override
    public void execute() {
        presentation.setSlideNumber(presentation.getSlideNumber()-1);
    }
}
