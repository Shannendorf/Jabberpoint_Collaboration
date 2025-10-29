package Application;

import Domain.Presentation;
import Infrastructure.Accessor;

import java.io.IOException;

public class OpenNewPresentationCommand extends PresentationCommand {

    public Accessor accessor;
    public String fileName;

    /**
     * Ctor, bevat naast command ook Accessor en file naam van de presentation
     */
    public OpenNewPresentationCommand(Presentation presentation, Accessor accessor, String fileName) {
        super(presentation);
        this.accessor = accessor;
        this.fileName = fileName;
    }

    /**
     * Voer Open New Presentation Command uit
     */
    @Override
    public void execute() {
        PresentationAccessorService presentationAccessorService = new PresentationAccessorService(accessor);
        try {
            loadNewPresentation(presentationAccessorService);
        } catch (IOException e) {
            System.err.println("Error loading new presentation: " + e.getMessage());
        }

        presentation.setSlideNumber(0);
    }

    /**
     * Open nieuwe presentatie
     */
    private void loadNewPresentation(PresentationAccessorService presentationAccessorService) throws IOException {
        presentationAccessorService.loadFile(presentation, fileName);
    }
}
