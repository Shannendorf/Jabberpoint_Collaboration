package Application;

public class ContentTableSlideFactory implements SlideFactory{

    public static SlideFactory getFactory() {
        return new ContentTableSlideFactory();
    }

    @Override
    public ContentTableSlideBuilder createBuilder() {
        return new ContentTableSlideBuilder();
    }
}
