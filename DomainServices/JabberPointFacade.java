package DomainServices;

import Domain.Commands.NextSlideCommand;
import Domain.Commands.PreviousSlideCommand;
import Domain.Factories.PresentationFactory;
import Domain.Entities.Presentation;
import Domain.Entities.Slide;
import Domain.Entities.Style;
import Infrastructure.XMLAccessor;

import java.io.IOException;

public class JabberPointFacade {
    private Presentation presentation;
    private PresentationAccessorService presentationAccessor;

    public JabberPointFacade() {
        this.presentation = PresentationFactory.createPresentation(); //PresentationFactory.createPresentation();
        this.presentationAccessor = new PresentationAccessorService(new XMLAccessor());

        Style.createStyles();
    }

    public String getTitle() {
        return this.presentation.getTitle();
    }

    public int getSize() {
        return this.presentation.getSize();
    }

    public void nextSlide() {
        var command = new NextSlideCommand(this.presentation);
        command.execute();
    }

    public void previousSlide() {
        var command = new PreviousSlideCommand(this.presentation);
        command.execute();
    }

    public Slide getCurrentSlide() {
        return this.presentation.getCurrentSlide();
    }

    public int getSlideNumber() {
        return this.presentation.getSlideNumber();
    }

    public void clearPresentation() {
        this.presentation.clear();
    }

    public void addEmptySlide() {
        this.presentation.addEmptySlide();
    }

    public void setSlideNumber(int slideNumber) {
        this.presentation.setSlideNumber(slideNumber);
    }

    public void handleSlideClick(int x, int y){
        var currentSlide = this.presentation.getCurrentSlide();
        currentSlide.handleClick(x, y);
    }

    public void loadFile(String fileName) throws IOException {
        this.presentationAccessor.loadFile(this.presentation, fileName);
    }

    public void saveFile(String fileName) throws IOException {
        this.presentationAccessor.saveFile(this.presentation, fileName);
    }

    public void exit(int n) {
        this.presentation.exit(n);
    }

    public void subscribe(PresentationSubscriber subscriber) {
        this.presentation.subscribe(subscriber);
    }

}
