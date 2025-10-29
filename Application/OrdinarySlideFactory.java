package Application;

public class OrdinarySlideFactory implements SlideFactory {

    /**
     * Return Ordinary Slide Builder
     */
    public static SlideFactory getFactory() {
        return new OrdinarySlideFactory();
    }


    /**
     * Maak Ordinary Slide Builder aan en return het
     */
    @Override
    public OrdinarySlideBuilder createBuilder() {
        return new OrdinarySlideBuilder();
    }
}
