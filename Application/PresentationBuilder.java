package Application;

import Domain.Presentation;
import Domain.Slide;

import java.util.ArrayList;

public class PresentationBuilder {

    private final ArrayList<Slide> slides;
    private String title;

    public PresentationBuilder() {
        slides = new ArrayList<>();
    }

    public void addSlide(Slide slide)
    {
        slides.add(slide);
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
