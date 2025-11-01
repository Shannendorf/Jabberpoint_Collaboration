package Domain.Builders;

import Domain.Entities.OrdinarySlide;
import Domain.Entities.Slide;
import Domain.Entities.SlideItem;

import java.util.ArrayList;

public class OrdinarySlideBuilder implements SlideBuilder {

    private String title;
    private final ArrayList<SlideItem> slideItems;

    /**
     * Ctor
     */
    public OrdinarySlideBuilder() {
        slideItems = new ArrayList<>();
    }

    /**
     * Return instantie van de Ordinary Slide builder
     */
    public static OrdinarySlideBuilder getSlideBuilder(){
        return new OrdinarySlideBuilder();
    }

    /**
     * Zet de titel van de slide
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Voeg Item aan de slide toe
     */
    @Override
    public void addItem(SlideItem item) {
        slideItems.add(item);
    }

    /**
     * Maak instantie van de Ordinary Slide aan en return het
     */
    @Override
    public Slide createInstance() {
        OrdinarySlide slide = new OrdinarySlide();
        slide.setTitle(title);
        for  (SlideItem slideItem : slideItems) {
            slide.append(slideItem);
        }

        return slide;
    }
}
