package Domain.Factories;

public interface BaseSlideItemFactory extends SlideItemFactory {
    void setContent(String content);
    void setLevel(int level);
}
