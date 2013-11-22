package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import city.Animation;

public class RestaurantChungPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1355285244678935863L;

	private Timer timer = new Timer(5, this );
	
//	Fixed Numbers
//	=====================================================================
//    private final int WINDOWX = 600;
//    private final int WINDOWY = 600;   
    private static final int TABLEX = 125, TABLEY = 200;
    private static final int TABLEGAP = 80;
    private static final int TABLEW = 50, TABLEH = 50;
    private static final int KITCHEN1X = 450, KITCHEN1Y = 40;
    private static final int KITCHEN1W = 30, KITCHEN1L = 60;
    private static final int KITCHEN2Y = KITCHEN1Y+KITCHEN1L;
    private static final int KITCHEN2W = 90, KITCHEN2L = 30;
    private static final int GRILLX = KITCHEN1X+40, GRILLY = KITCHEN2Y;
    private static final int GRILLW = 35, GRILLL = 15;

	private int panelX;
	private int panelY;
//	private final int delayMS = 5;
	private List<Animation> animations = new ArrayList<Animation>();

	public RestaurantChungPanel(Dimension panelDimension) {
		panelX = panelDimension.width;
		panelY = panelDimension.height;

		setVisible(true);

//		Timer timer = new Timer(delayMS, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	public void paintComponent(Graphics graphics) {
		Graphics2D g2 = (Graphics2D)graphics;

		// Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, panelX, panelY);

        // kitchen area
        g2.setColor(Color.YELLOW);
        g2.fillRect(KITCHEN1X, KITCHEN1Y, KITCHEN1W, KITCHEN1L);
        g2.fillRect(KITCHEN1X, KITCHEN2Y, KITCHEN2W, KITCHEN2L);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(GRILLX, GRILLY, GRILLW, GRILLL);
        for (int i = 0; i < 3; i++) {
            //Here is the table
            g2.setColor(Color.ORANGE);
            g2.fillRect(TABLEX+((i%5)*TABLEGAP), TABLEY+((i/5)*TABLEGAP), TABLEW, TABLEH);        	
        }
		
		// Update the position of each visible element
		for(Animation animation : animations) {
			if (animation.getVisible()) {
				animation.updatePosition();
			}
		}

		// Draw each visible element after updating their positions
		// TODO generates concurrent modification exception
		for(Animation animation : animations) {
			if (animation.getVisible()) {
				animation.draw(g2);
			}
		}
	}

	public void addVisualizationElement(Animation ve) {
		animations.add(ve);
		System.out.println(animations.size());
	}
}