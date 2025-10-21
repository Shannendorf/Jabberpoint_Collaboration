package UI;

import Application.JabberPointFacade;
import Application.PresentationSubscriber;
import Domain.ClickableSlideItem;
import Domain.Slide;
import Domain.SlideItem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;


/** <p>UI.SlideViewerComponent is een grafische component die Slides kan laten zien.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class SlideViewerComponent extends JComponent implements PresentationSubscriber {
		
	private Slide slide; // de huidige slide
	private Font labelFont = null; // het font voor labels
	private JabberPointFacade jabberPointFacade;
	private JFrame frame = null;
	
	private static final long serialVersionUID = 227L;
	
	private static final Color BGCOLOR = Color.white;
	private static final Color COLOR = Color.black;
	private static final String FONTNAME = "Dialog";
	private static final int FONTSTYLE = Font.BOLD;
	private static final int FONTHEIGHT = 10;
	private static final int XPOS = 1100;
	private static final int YPOS = 20;

	public SlideViewerComponent(JFrame frame, JabberPointFacade jabberPointFacade) {
		setBackground(BGCOLOR);
		labelFont = new Font(FONTNAME, FONTSTYLE, FONTHEIGHT);
		this.frame = frame;
		this.jabberPointFacade = jabberPointFacade;
		jabberPointFacade.subscribe((PresentationSubscriber) this);

        MouseInputAdapter mouseHandler = new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        };

        addMouseListener(mouseHandler);
	}

    private void handleMouseClick(MouseEvent e) {
        Slide currentSlide = this.jabberPointFacade.getCurrentSlide();
        if (currentSlide == null) return;

        for (SlideItem item : currentSlide.getSlideItems()) {
            if (item instanceof ClickableSlideItem clickable) {
                if (clickable.isClicked(e.getX(), e.getY())) {
                    clickable.onClick();
                    break;
                }
            }
        }
    }

	public Dimension getPreferredSize() {
		return new Dimension(Slide.WIDTH, Slide.HEIGHT);
	}

// teken de slide
	public void paintComponent(Graphics g) {
		g.setColor(BGCOLOR);
		g.fillRect(0, 0, getSize().width, getSize().height);
		if (jabberPointFacade.getSlideNumber() < 0 || slide == null) {
			return;
		}
		g.setFont(labelFont);
		g.setColor(COLOR);
		g.drawString("Slide " + (1 + jabberPointFacade.getSlideNumber()) + " of " +
                 jabberPointFacade.getSize(), XPOS, YPOS);
		Rectangle area = new Rectangle(0, YPOS, getWidth(), (getHeight() - YPOS));
		slide.draw(g, area, this);
	}

	public void notifySlideChange() {
		this.slide = jabberPointFacade.getCurrentSlide();
		repaint();
		frame.setTitle(jabberPointFacade.getTitle());
	}
}
