package city.gui.interiors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import city.bases.interfaces.AnimationInterface;
import city.gui.BuildingCard;

public class RestaurantJPPanel extends BuildingCard implements ActionListener {

	private static final long serialVersionUID = 1255285244678935863L;
    private final int TABLEORIGINX = 200;
    private final int TABLEORIGINY = 250;
    private final int TABLELENGTH = 50;
    private final int TABLESEPARATION = 100;
    private final int delayMS = 5;

    public RestaurantJPPanel(Color color) {
    	super(color);
    	
        setVisible(true);
 
    	Timer timer = new Timer(delayMS, this);
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	@Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;

        // Clear the screen by painting a rectangle the size of the frame
        graphics2D.setColor(background);
        graphics2D.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);

        for(int i=0; i<3; i++)			//HACK, need 3 to be a variable!
        {
        graphics2D.setColor(Color.ORANGE);
        graphics2D.fillRect(TABLEORIGINX, TABLEORIGINY - i*TABLESEPARATION, TABLELENGTH, TABLELENGTH);
        }
        
    	graphics2D.setColor(Color.yellow);
    	graphics2D.fillRect(160, 420, 60, 20);
    	graphics2D.setColor(Color.RED);
    	graphics2D.fillRect(80, 420, 60, 20);
    	graphics2D.setColor(Color.white);
    	graphics2D.fillRect(20, 350, 50, 50);
        // Update the position of each visible element
        for(AnimationInterface animation : animations) {
        	if (animation.getVisible()) {
                animation.updatePosition();
            }
        }

        // Draw each visible element after updating their positions
        // TODO generates concurrent modification exception
        for(AnimationInterface animation : animations) {
            if (animation.getVisible()) {
                animation.draw(graphics2D);
            }
        }
    }
}
