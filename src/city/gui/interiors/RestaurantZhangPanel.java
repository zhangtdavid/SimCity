package city.gui.interiors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Timer;

import utilities.RestaurantZhangTable;
import city.bases.interfaces.AnimationInterface;
import city.gui.BuildingCard;

public class RestaurantZhangPanel extends BuildingCard implements ActionListener {

	private static final long serialVersionUID = 1255285244678935863L;

	private Collection<RestaurantZhangTable> tables = new ArrayList<RestaurantZhangTable>();

	public RestaurantZhangPanel(Color color) {
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
		graphics2D.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
		graphics.setColor(Color.black);
		graphics.fillRect(20, 10, 40, 460);
		graphics.setColor(Color.GRAY);
		for(int i = 0; i < 3; i++) {
			graphics.fillRect(100 + 70 * i, 10, 60, 20);
			graphics.drawString("Grill " + i, 100 + 70 * i, 10);
		}
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(100, 100, 200, 20);
		graphics.drawString("Plating", 100, 100);
		graphics.setColor(Color.ORANGE);

		graphics.setColor(Color.ORANGE);
		for(RestaurantZhangTable t : tables) {
			graphics.fillRect(t.getX(), t.getY(), t.getW(), t.getH());
		}

		animate();

		// Draw each visible element after updating their positions
		synchronized(animations) {
			for(AnimationInterface animation : animations) {
				if (animation.getVisible()) {
					animation.draw(graphics2D);
				}
			}
		}
	}

	public void setTables(Collection<RestaurantZhangTable> t) {
		tables = t;
	}
}
