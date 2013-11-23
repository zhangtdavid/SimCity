package city.gui;

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

public class RestaurantTimmsPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1255285244678935863L;
	
	private int panelX;
    private int panelY;
    private final int delayMS = 5;
	private List<Animation> animations = new ArrayList<Animation>();

    public RestaurantTimmsPanel(Dimension panelDimension) {
    	panelX = panelDimension.width;
    	panelY = panelDimension.height;
    	
        setVisible(true);
 
    	Timer timer = new Timer(delayMS, this);
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;

        // Clear the screen by painting a rectangle the size of the frame
        graphics2D.setColor(getBackground());
        graphics2D.fillRect(0, 0, panelX, panelY);

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
                animation.draw(graphics2D);
            }
        }
    }
    
    public void addVisualizationElement(Animation ve) {
    	animations.add(ve);
    }
}
