package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import utilities.RestaurantZhangTable;
import city.animations.interfaces.RestaurantZhangAnimatedWaiter;
import city.bases.Animation;
import city.gui.exteriors.CityViewApt;
import city.roles.interfaces.RestaurantZhangWaiter;

public class RestaurantZhangWaiterAnimation extends Animation implements RestaurantZhangAnimatedWaiter {

	private RestaurantZhangWaiter waiter = null;

	public final int BASEX;
	public final int BASEY;
	public static final int BREAKX = 600;
	public static final int BREAKY = 600;
	public static final int RESTAURANTENTRANCEX = 30;
	public static final int RESTAURANTENTRANCEY = 30;

	private int xPos , yPos;//default waiter position
	private int xDestination, yDestination;//default start position

	private String foodString = null;
	private static final int TEXTHEIGHT = 14;

	private boolean atDestination = true;

	public static final int MOVEMENTINTERVAL = 20;

	private static BufferedImage restaurantZhangWaiterWaitingAreaImage = null;

	private static BufferedImage restaurantZhangWaiterRegularNorthImage = null;
	private static BufferedImage restaurantZhangWaiterRegularEastImage = null;
	private static BufferedImage restaurantZhangWaiterRegularSouthImage = null;
	private static BufferedImage restaurantZhangWaiterRegularWestImage = null;
	private BufferedImage imageToRender;

	public RestaurantZhangWaiterAnimation(RestaurantZhangWaiter waiter, int baseX, int baseY) {
		this.waiter = waiter;
		BASEX = baseX;
		BASEY = baseY;
		xPos = -20;
		yPos = -20;
		xDestination = BASEX;
		yDestination = BASEY;
		try {
			if(restaurantZhangWaiterWaitingAreaImage == null) {
				restaurantZhangWaiterWaitingAreaImage = ImageIO.read(RestaurantZhangWaiterAnimation.class.getResource("/icons/restaurantZhangPanel/restaurantZhangWaiterWaitingAreaImage.png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if(restaurantZhangWaiterRegularNorthImage == null || restaurantZhangWaiterRegularEastImage == null || restaurantZhangWaiterRegularSouthImage == null || restaurantZhangWaiterRegularWestImage == null) {
				restaurantZhangWaiterRegularNorthImage = ImageIO.read(CityViewApt.class.getResource("/icons/restaurantZhangPanel/restaurantZhangWaiterRegularNorthImage.png"));
				restaurantZhangWaiterRegularEastImage = ImageIO.read(CityViewApt.class.getResource("/icons/restaurantZhangPanel/restaurantZhangWaiterRegularEastImage.png"));
				restaurantZhangWaiterRegularSouthImage = ImageIO.read(CityViewApt.class.getResource("/icons/restaurantZhangPanel/restaurantZhangWaiterRegularSouthImage.png"));
				restaurantZhangWaiterRegularWestImage = ImageIO.read(CityViewApt.class.getResource("/icons/restaurantZhangPanel/restaurantZhangWaiterRegularWestImage.png"));
				imageToRender = restaurantZhangWaiterRegularNorthImage;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			xPos++;
			imageToRender = restaurantZhangWaiterRegularEastImage;
		} else if (xPos > xDestination) {
			xPos--;
			imageToRender = restaurantZhangWaiterRegularWestImage;
		}
		if (yPos < yDestination) {
			yPos++;
			imageToRender = restaurantZhangWaiterRegularSouthImage;
		} else if (yPos > yDestination) {
			yPos--;
			imageToRender = restaurantZhangWaiterRegularNorthImage;
		}

		if(xPos == xDestination && yPos == yDestination && atDestination == false) {
			atDestination = true;
			waiter.msgAtDestination();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.PINK);
		g.drawImage(restaurantZhangWaiterWaitingAreaImage, BASEX, BASEY, null);
		g.drawString(waiter.getPerson().getName(), BASEX, BASEY);
		g.setColor(Color.MAGENTA);
		g.drawImage(imageToRender, xPos, yPos, null);
		if(foodString != null)
			g.drawString(foodString, xPos, yPos + MOVEMENTINTERVAL + TEXTHEIGHT);
	}

	public boolean isPresent() {
		return true;
	}

	public void GoToTable(RestaurantZhangTable t) {
		//currentTable = t;
		xDestination = t.getX() + MOVEMENTINTERVAL;
		yDestination = t.getY() - MOVEMENTINTERVAL;
		atDestination = false;
	}

	public void GoToDestination(int x, int y) {
		xDestination = x;
		yDestination = y;
		atDestination = false;
	}

	public void GoToCustomer(int pos) {
		xDestination = RESTAURANTENTRANCEX  + MOVEMENTINTERVAL;
		yDestination = RESTAURANTENTRANCEY + pos * 30  + MOVEMENTINTERVAL;
		atDestination = false;
	}

	public void GoToBreak() {
		xDestination = BREAKX;
		yDestination = BREAKY;
		atDestination = false;
	}

	public boolean ReturnToBase() {
		if(xDestination != BASEX || yDestination != BASEY) {
			xDestination = BASEX;
			yDestination = BASEY;
			atDestination = false;
			return true;
		}
		return false;
	}

	public void setFoodLabel(String choice, boolean isHere) {
		foodString = choice;
		if(isHere == false) {
			foodString += "?";
		}
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	@Override
	public int getBaseX() {
		return BASEX;
	}

	@Override
	public int getBaseY() {
		return BASEY;
	}
}
