package Application;

import Domain.Presentation;

public class FirstSlideCommand extends PresentationCommand {

    /**
     * Ctor
     */
    public FirstSlideCommand(Presentation presentation) {
        super(presentation);
    }

    /**
     * Voer First Slide Command uit
     */
    @Override
    public void execute() {
        presentation.setSlideNumber(0);
    }
}
