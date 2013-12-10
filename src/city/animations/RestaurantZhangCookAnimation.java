package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import city.animations.interfaces.RestaurantZhangAnimatedCook;
import city.bases.Animation;
import city.gui.exteriors.CityViewApt;
import city.roles.interfaces.RestaurantZhangCook;

public class RestaurantZhangCookAnimation extends Animation implements RestaurantZhangAnimatedCook {

	private RestaurantZhangCook cook = null;

	public static final int BASEX = 190;
	public static final int BASEY = 50;

	String[] platingAreaStrings = new String[3];
	String[] grillStrings = new String[3];

	private int xPos , yPos;//default waiter position
	private int xDestination, yDestination;//default start position

	private static final int TEXTHEIGHT = 14;

	private boolean atDestination = true;

	public static final int MOVEMENTINTERVAL = 20;

	private static BufferedImage restaurantZhangCookNorthImage = null;
	private static BufferedImage restaurantZhangCookEastImage = null;
	private static BufferedImage restaurantZhangCookSouthImage = null;
	private static BufferedImage restaurantZhangCookWestImage = null;
	private BufferedImage imageToRender;

	public RestaurantZhangCookAnimation(RestaurantZhangCook cook) {
		this.cook = cook;
		xPos = -20;
		yPos = -20;
		xDestination = BASEX;
		yDestination = BASEY;
		for(int i = 0; i < platingAreaStrings.length; i++) {
			platingAreaStrings[i] = "";
		}
		for(int i = 0; i < grillStrings.length; i++) {
			grillStrings[i] = "";
		}
		try {
			if(restaurantZhangCookNorthImage == null || restaurantZhangCookEastImage == null || restaurantZhangCookSouthImage == null || restaurantZhangCookWestImage == null) {
				restaurantZhangCookNorthImage = ImageIO.read(CityViewApt.class.getResource("/icons/restaurantZhangPanel/restaurantZhangCookNorthImage.png"));
				restaurantZhangCookEastImage = ImageIO.read(CityViewApt.class.getResource("/icons/restaurantZhangPanel/restaurantZhangCookEastImage.png"));
				restaurantZhangCookSouthImage = ImageIO.read(CityViewApt.class.getResource("/icons/restaurantZhangPanel/restaurantZhangCookSouthImage.png"));
				restaurantZhangCookWestImage = ImageIO.read(CityViewApt.class.getResource("/icons/restaurantZhangPanel/restaurantZhangCookWestImage.png"));
				imageToRender = restaurantZhangCookNorthImage;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			xPos++;
			imageToRender = restaurantZhangCookEastImage;
		} else if (xPos > xDestination) {
			xPos--;
			imageToRender = restaurantZhangCookWestImage;
		}
		if (yPos < yDestination) {
			yPos++;
			imageToRender = restaurantZhangCookSouthImage;
		} else if (yPos > yDestination) {
			yPos--;
			imageToRender = restaurantZhangCookNorthImage;
		}

		if(xPos == xDestination && yPos == yDestination && atDestination == false) {
			atDestination = true;
			cook.msgAtDestination();
		}
	}

	public void draw(Graphics2D g) {
		for(int i = 0; i < platingAreaStrings.length; i++) {
			g.drawString(platingAreaStrings[i], 100 + 70 * i, 100 + TEXTHEIGHT);
		}
		for(int i = 0; i < grillStrings.length; i++) {
			g.drawString(grillStrings[i], 100 + 70 * i, 10 + TEXTHEIGHT);
		}
		g.setColor(Color.BLUE);
		g.drawImage(imageToRender, xPos, yPos, null);   
	}

	public boolean isPresent() {
		return true;
	}

	public void GoToDestination(int x, int y) {
		xDestination = x;
		yDestination = y;
		atDestination = false;
	}

	public void addToPlatingArea(String s, int pos) {
		platingAreaStrings[pos] = s;
	}

	public void removeFromPlatingArea(int pos) {
		platingAreaStrings[pos] = "";
	}

	public void addToGrill(String s, int pos) {
		grillStrings[pos] = s;
	}

	public void removeFromGrill(int pos) {
		grillStrings[pos] = "";
	}

	public void goToBase() {
		xDestination = BASEX;
		yDestination = BASEY;
		atDestination = false;
	}

	public void goToPlating() {
		xDestination = 190;
		yDestination = 80;
		atDestination = false;
	}

	public void goToGrill(int grillNumber) {
		xDestination = 100 + 70 * grillNumber;
		yDestination = 30;
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
		/*foodString = choice;
		if(isHere == false) {
			foodString += "?";
		}*/
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
}
