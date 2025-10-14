package Application;

import Domain.Presentation;
import Infrastructure.Accessor;

import java.io.IOException;

public class PresentationAccessorService {

    private final Accessor accessor;

    public PresentationAccessorService(Accessor accessor)
    {
        this.accessor = accessor;
    }

    public void loadFile(Presentation presentation, String fn) throws IOException {
        this.accessor.loadFile(presentation, fn);
    }

    public void saveFile(Presentation presentation, String fn) throws IOException {
        this.accessor.saveFile(presentation, fn);
    }
}
