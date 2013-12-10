package city.gui.interiors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import utilities.RestaurantZhangTable;
import city.bases.interfaces.AnimationInterface;
import city.gui.BuildingCard;

public class RestaurantZhangPanel extends BuildingCard implements ActionListener {

	private static final long serialVersionUID = 1255285244678935863L;
	
	private static BufferedImage restaurantZhangBackgroundImage = null;
	private static BufferedImage restaurantZhangTableImage = null;
	private static BufferedImage restaurantZhangGrillImage = null;
	private static BufferedImage restaurantZhangPlatingImage = null;
	private static BufferedImage restaurantZhangWaitingAreaImage = null;

	private Collection<RestaurantZhangTable> tables = new ArrayList<RestaurantZhangTable>();

	public RestaurantZhangPanel(Color color) {
		super(color);

		setVisible(true);

		Timer timer = new Timer(delayMS, this);
		timer.start();
		
		try {
			if(restaurantZhangBackgroundImage == null) {
				restaurantZhangBackgroundImage = ImageIO.read(RestaurantZhangPanel.class.getResource("/icons/restaurantZhangPanel/RestaurantZhangBackgroundImage.png"));
				restaurantZhangGrillImage = ImageIO.read(RestaurantZhangPanel.class.getResource("/icons/restaurantZhangPanel/RestaurantZhangGrillImage.png"));
				restaurantZhangPlatingImage = ImageIO.read(RestaurantZhangPanel.class.getResource("/icons/restaurantZhangPanel/RestaurantZhangPlatingImage.png"));
				restaurantZhangTableImage = ImageIO.read(RestaurantZhangPanel.class.getResource("/icons/restaurantZhangPanel/RestaurantZhangTableImage.png"));
				restaurantZhangWaitingAreaImage = ImageIO.read(RestaurantZhangPanel.class.getResource("/icons/restaurantZhangPanel/RestaurantZhangWaitingAreaImage.png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D)graphics;
		// Clear the screen by painting a rectangle the size of the frame
		graphics2D.setColor(Color.yellow);
		graphics2D.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
		graphics2D.drawImage(restaurantZhangBackgroundImage, 0, 0, null);
		graphics.setColor(Color.black);
		graphics.drawImage(restaurantZhangWaitingAreaImage, 20, 15, null);
		graphics.setColor(Color.GRAY);
		for(int i = 0; i < 3; i++) {
			graphics.drawImage(restaurantZhangGrillImage, 100 + 70 * i, 10, null);
			graphics.drawString("Grill " + i, 100 + 70 * i, 10);
		}
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.drawImage(restaurantZhangPlatingImage, 100, 100, null);
		graphics.drawString("Plating", 100, 100);
		graphics.setColor(Color.ORANGE);

		graphics.setColor(Color.ORANGE);
		for(RestaurantZhangTable t : tables) {
			graphics.drawImage(restaurantZhangTableImage, t.getX(), t.getY(), null);
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
