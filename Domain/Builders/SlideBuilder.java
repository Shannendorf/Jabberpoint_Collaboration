package Domain.Builders;

import Domain.Entities.Slide;
import Domain.Entities.SlideItem;

public interface SlideBuilder {

    void setTitle(String title);
    void addItem(SlideItem item);
    Slide createInstance();
}
