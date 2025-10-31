package Domain.Factories;

import Domain.Entities.SlideItem;
import Domain.Entities.TextItem;

public class TextItemFactory implements SlideItemFactory {

    /**
     * Maak een Text Item aan
     */
	@Override
	public SlideItem createSlideItem(String content, int level) {
		return new TextItem(level, content);
	}

}
