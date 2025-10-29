package Application;

import Domain.Presentation;
import Domain.Slide;

import java.util.ArrayList;

public class PresentationBuilder {

    private final ArrayList<Slide> slides;
    private String title;

    /**
     * Ctor
     */
    public PresentationBuilder() {
        slides = new ArrayList<>();
    }

    /**
     * Voeg een Slide toe aan de Presentation
     */
    public void addSlide(Slide slide)
    {
        slides.add(slide);
    }

    /**
     * Zet de titel van de presentatie
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Maak een instantie van de presentatie
     */
    public Presentation createInstance()
    {
        var presentation = new Presentation();
        for(var slide : slides)
        {
            presentation.append(slide);
        }
        presentation.setTitle(title);
        return presentation;
    }
}
