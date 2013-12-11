package city.gui.interiors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import city.animations.RestaurantTimmsTableAnimation;
import city.bases.interfaces.AnimationInterface;
import city.gui.BuildingCard;

public class RestaurantTimmsPanel extends BuildingCard implements ActionListener {

	private static final long serialVersionUID = 1255285244678935863L;

	public RestaurantTimmsPanel(Color color) {
		super(color);

		setVisible(true);

		Timer timer = new Timer(delayMS, this);
		timer.start();
		
        int i = 0;
        while (i < 9) {
                this.addVisualizationElement(new RestaurantTimmsTableAnimation(i));
                i++;
        }
	}

	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D)graphics;
		// Clear the screen by painting a rectangle the size of the frame
		graphics2D.setColor(background);
		graphics2D.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
		
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
