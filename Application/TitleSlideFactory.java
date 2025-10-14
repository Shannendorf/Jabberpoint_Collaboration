package Application;

public class TitleSlideFactory implements SlideFactory {

    public static SlideFactory getFactory() {
        return new TitleSlideFactory();
    }

    @Override
    public TitleSlideBuilder createBuilder() {
        return new TitleSlideBuilder();
    }
}
