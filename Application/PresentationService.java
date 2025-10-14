package Application;

import Domain.Presentation;
import Domain.Slide;
import Domain.SlideItem;

public class PresentationService {

    public void addEmptySlide(Presentation presentation)
    {
        Slide slide = new Slide();
        presentation.append(slide);
    }

    public void addSlide(Presentation presentation, Slide slide)
    {
        presentation.append(slide);
    }

    public void setSlideNumber(Presentation presentation, int slideNumber)
    {
        presentation.setSlideNumber(slideNumber);
    }

    public void clearPresentation(Presentation presentation)
    {
        presentation.clear();
    }

    public void nextSlide(Presentation presentation)
    {
        presentation.setSlideNumber(presentation.getSlideNumber() + 1);
    }

    public void previousSlide(Presentation presentation)
    {
        if (presentation.getSlideNumber() > 0)
        {
            presentation.setSlideNumber(presentation.getSlideNumber() - 1);
        }
    }

    public void exit(Presentation presentation, int n)
    {
        presentation.exit(n);
    }

    public void addItemToLastSlide(Presentation presentation, SlideItem slideItem)
    {
        //creator?
        var slide = presentation.getSlide(presentation.getSize()-1);
        slide.append(slideItem);
    }
}
