package Application;

import Domain.BitmapItem;
import Domain.SlideItem;

public class BitmapItemFactory implements SlideItemFactory {

    /**
     * Maak Slide Item aan
     */
	@Override
	public SlideItem createSlideItem(String content, int level) {
		return new BitmapItem(level, content);
	}

}
