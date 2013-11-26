package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Timer;

import utilities.RestaurantZhangTable;
import city.Animation;

public class RestaurantZhangPanel extends BuildingCard implements ActionListener {

	private static final long serialVersionUID = 1255285244678935863L;

	private int panelX;
	private int panelY;
	private final int delayMS = 5;
	private List<Animation> animations = new ArrayList<Animation>();
	private Collection<RestaurantZhangTable> tables = new ArrayList<RestaurantZhangTable>();

	public RestaurantZhangPanel(Color color, Dimension panelDimension) {
		super(color);
		panelX = panelDimension.width;
		panelY = panelDimension.height;

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
		// TODO generates concurrent modification exception
		for(Animation animation : animations) {
			if (animation.getVisible()) {
				animation.draw(graphics2D);
			}
		}
	}

	public void animate() {
		// Update the position of each visible element
		for(Animation animation : animations) {
			if (animation.getVisible()) {
				animation.updatePosition();
			}
		}
	}

	public void addVisualizationElement(Animation ve) {
		animations.add(ve);
	}

	public void setTables(Collection<RestaurantZhangTable> t) {
		tables = t;
	}
}
