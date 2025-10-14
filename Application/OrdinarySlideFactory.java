package Application;

import Domain.OrdinarySlide;
import Domain.Slide;

public class OrdinarySlideFactory implements SlideFactory {

    public static SlideFactory getFactory() {
        return new OrdinarySlideFactory();
    }

    @Override
    public OrdinarySlideBuilder createBuilder() {
        return new OrdinarySlideBuilder();
    }
}
