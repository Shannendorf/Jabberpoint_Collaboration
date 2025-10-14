package Application;

import Domain.Presentation;

public abstract class PresentationCommand implements Command {
    public Presentation presentation;

    public PresentationCommand(Presentation presentation) {
        this.presentation = presentation;
    }

    public abstract void execute();
}
