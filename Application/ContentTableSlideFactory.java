package Application;

public class ContentTableSlideFactory implements SlideFactory{

    /**
     * Return Factory
     */
    public static SlideFactory getFactory() {
        return new ContentTableSlideFactory();
    }

    /**
     * Maak ContentTableSlide builder aan
     */
    @Override
    public ContentTableSlideBuilder createBuilder() {
        return new ContentTableSlideBuilder();
    }
}
