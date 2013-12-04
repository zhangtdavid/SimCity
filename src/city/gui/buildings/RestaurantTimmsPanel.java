package city.gui.buildings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import city.AnimationInterface;
import city.gui.BuildingCard;

public class RestaurantTimmsPanel extends BuildingCard implements ActionListener {

	private static final long serialVersionUID = 1255285244678935863L;

	public RestaurantTimmsPanel(Color color) {
		super(color);

		setVisible(true);

		Timer timer = new Timer(delayMS, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D)graphics;
		// Clear the screen by painting a rectangle the size of the frame
		graphics2D.setColor(background);
		graphics2D.fillRect(0, 0, panelX, panelY);
		
		animate();

		// Draw each visible element after updating their positions
		// TODO generates concurrent modification exception
		for(AnimationInterface animation : animations) {
			if (animation.getVisible()) {
				animation.draw(graphics2D);
			}
		}
	}
}
