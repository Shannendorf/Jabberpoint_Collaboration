package Domain.Factories;

import Domain.Builders.TitleSlideBuilder;

public class TitleSlideFactory implements SlideFactory {

    /**
     * Return Slide Factory
     */
    public static SlideFactory getFactory() {
        return new TitleSlideFactory();
    }

    /**
     * Maak een instantie van de TitleSlide builder aan en return deze
     */
    @Override
    public TitleSlideBuilder createBuilder() {
        return new TitleSlideBuilder();
    }
}
