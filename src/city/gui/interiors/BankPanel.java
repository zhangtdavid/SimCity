package city.gui.interiors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import city.bases.interfaces.AnimationInterface;
import city.gui.BuildingCard;

public class BankPanel extends BuildingCard implements ActionListener {

	private static final long serialVersionUID = 1255285244678935863L;
	
    private final int delayMS = 5;
    
    public BankPanel(Color color) {
    	super(Color.CYAN);
    	
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
        graphics2D.setColor(background);
        graphics2D.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);

        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(300, 100, 20, 320);
        graphics2D.fillRect(320, 100, 50, 20);
        graphics2D.fillRect(320, 200, 50, 20);
        graphics2D.fillRect(320, 300, 50, 20);
        graphics2D.fillRect(320, 400, 50, 20);
        graphics2D.setColor(Color.yellow);
        graphics2D.fillRect(450, 200, 50, 120);
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
