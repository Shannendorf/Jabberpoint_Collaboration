package Application;

import Domain.SlideItem;
import Domain.TextItem;

public class TextItemFactory implements SlideItemFactory{

	@Override
	public SlideItem createSlideItem(String content, int level) {
		return new TextItem(level, content);
	}

}
