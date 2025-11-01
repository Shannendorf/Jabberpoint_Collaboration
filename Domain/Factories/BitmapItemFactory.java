package Domain.Factories;

import Domain.Entities.BitmapItem;
import Domain.Entities.SlideItem;

public class BitmapItemFactory implements BaseSlideItemFactory {

    private String content;
    private int level;

    /**
     * Maak Slide Item aan
     */
	@Override
	public SlideItem createSlideItem() {
		return new BitmapItem(level, content);
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
