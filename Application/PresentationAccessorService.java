package Application;

import Domain.Presentation;
import Infrastructure.Accessor;

import java.io.IOException;

public class PresentationAccessorService {

    private final Accessor accessor;

    /**
     * Ctor
     */
    public PresentationAccessorService(Accessor accessor)
    {
        this.accessor = accessor;
    }

    /**
     * Laad een presentatie met filename fn
     */
    public void loadFile(Presentation presentation, String fn) throws IOException {
        this.accessor.loadFile(presentation, fn);
    }

    /**
     * Sla een presentie op in een file met filename fn
     */
    public void saveFile(Presentation presentation, String fn) throws IOException {
        this.accessor.saveFile(presentation, fn);
    }
}
