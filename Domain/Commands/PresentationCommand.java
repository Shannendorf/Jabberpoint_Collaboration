package Domain.Commands;

import Domain.Entities.Presentation;

public abstract class PresentationCommand implements Command {
    public Presentation presentation;

    /**
     * Ctor
     */
    public PresentationCommand(Presentation presentation) {
        this.presentation = presentation;
    }

    public abstract void execute();
}
