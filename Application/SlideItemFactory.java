package Application;

import Domain.SlideItem;

import org.w3c.dom.Element;

public interface SlideItemFactory {

    SlideItem createSlideItem(String content, int level);
}
