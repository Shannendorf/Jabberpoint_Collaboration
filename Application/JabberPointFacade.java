package Application;

import Domain.Presentation;
import Domain.Slide;
import Domain.Style;
import Infrastructure.XMLAccessor;
import UI.SlideViewerComponent;

import java.io.IOException;

public class JabberPointFacade {
    private Presentation presentation;
    private PresentationAccessorService presentationAccessor;
    private PresentationService presentationService;

    public JabberPointFacade() {
        this.presentation = PresentationFactory.createPresentation(); //PresentationFactory.createPresentation();
        this.presentationAccessor = new PresentationAccessorService(new XMLAccessor());
        this.presentationService = new PresentationService();

        Style.createStyles();
    }

    public String getTitle() {
        return this.presentation.getTitle();
    }

    public int getSize() {
        return this.presentation.getSize();
    }

    public void nextSlide() {
        this.presentationService.nextSlide(this.presentation);
    }

    public void previousSlide() {
        this.presentationService.previousSlide(this.presentation);
    }

    public Slide getCurrentSlide() {
        return this.presentation.getCurrentSlide();
    }

    public int getSlideNumber() {
        return this.presentation.getSlideNumber();
    }

    public void clearPresentation() {
        this.presentationService.clearPresentation(this.presentation);
    }

    public void addEmptySlide() {
        this.presentationService.addEmptySlide(this.presentation);
    }

    public void setSlideNumber(int slideNumber) {
        this.presentationService.setSlideNumber(this.presentation, slideNumber);
    }

    public void setShowView(SlideViewerComponent slideViewerComponent) {
        this.presentation.setShowView(slideViewerComponent);
    }

    public void loadFile(String fileName) throws IOException {
        this.presentationAccessor.loadFile(this.presentation, fileName);
    }

    public void saveFile(String fileName) throws IOException {
        this.presentationAccessor.saveFile(this.presentation, fileName);
    }

    public void exit(int n) {
        this.presentationService.exit(this.presentation, n);
    }

}
