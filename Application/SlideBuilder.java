package Application;

import Domain.Slide;
import Domain.SlideItem;

public interface SlideBuilder {

    void setTitle(String title);
    void addItem(SlideItem item);
    Slide createInstance();
}
