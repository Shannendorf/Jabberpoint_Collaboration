package Domain.Factories;

import Domain.Entities.SlideItem;
import Domain.Entities.TextItem;

public class TextItemFactory implements BaseSlideItemFactory {

    private String content;
    private int level;

    /**
     * Maak een Text Item aan
     */
	@Override
	public SlideItem createSlideItem() {
		return new TextItem(level, content);
	}

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }
}
