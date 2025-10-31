package Domain.Factories;

import Domain.Entities.SlideItem;

public interface SlideItemFactory {

    SlideItem createSlideItem(String content, int level);
}
