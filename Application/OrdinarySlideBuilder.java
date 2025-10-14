package Application;

import Domain.OrdinarySlide;
import Domain.Slide;
import Domain.SlideItem;

import java.util.ArrayList;

public class OrdinarySlideBuilder implements SlideBuilder {

    private String title;
    private final ArrayList<SlideItem> slideItems;

    public OrdinarySlideBuilder() {
        slideItems = new ArrayList<>();
    }

    public static OrdinarySlideBuilder getSlideBuilder(){
        return new OrdinarySlideBuilder();
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void addItem(SlideItem item) {
        slideItems.add(item);
    }

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
