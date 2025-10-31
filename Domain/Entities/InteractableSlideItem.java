package Domain.Entities;

import Domain.Commands.Command;

import java.awt.*;
import java.awt.image.ImageObserver;

public class InteractableSlideItem extends SlideItem {

    private final SlideItem childItem;
    private final Command command;

    public InteractableSlideItem(SlideItem childItem, Command command) {
        super(childItem != null ? childItem.getLevel() : 0);
        this.childItem = childItem;
        this.command = command;
    }
    
    public Command getCommand(){
        return command;
    }

    public SlideItem getChild(){
        return childItem;
    }

    @Override
    public Rectangle getBoundingBox() {
        if (childItem == null) return new Rectangle(0, 0, 0, 0);
        return childItem.getBoundingBox();
    }


    @Override
    public void draw(int x, int y, float scale, Graphics g, Style style, ImageObserver observer) {
        if (childItem == null) return;

        childItem.draw(x, y, scale, g, style, observer);
        boundingBox =  childItem.getBoundingBox();

        //Draw bounding box
        if (boundingBox != null) {
            Color prev = g.getColor();
            ((Graphics2D) g).setStroke(new BasicStroke(2));
            g.setColor(Color.BLUE);
            g.drawRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
            g.setColor(prev);
        }
    }

    public boolean isClicked(int x, int y) {
        return boundingBox != null && boundingBox.contains(x, y);
    }

    public void onClick() {
        if (command != null) {
            command.execute();
        }
    }
}
