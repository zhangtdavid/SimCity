package city.animations;

import java.awt.*;

import city.Animation;
import city.animations.interfaces.RestaurantChungAnimatedCashier;
import city.animations.interfaces.RestaurantTimmsAnimatedCashier;
import city.interfaces.RestaurantChungCashier;

public class RestaurantChungCashierAnimation extends Animation implements RestaurantChungAnimatedCashier {
	private RestaurantChungCashier agent = null;

//	Fixed Numbers
//	=====================================================================
	private static final int CRECTDIM = 20;
	
//	Location Information
//	=====================================================================	
	private int xPos = 50, yPos = 40;
	private int xDestination = xPos, yDestination = yPos;

	public RestaurantChungCashierAnimation(RestaurantChungCashier c) {
		agent = c;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

//		if (xPos == xDestination && yPos == yDestination) {
//
//		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, CRECTDIM, CRECTDIM);
	}

	public boolean isPresent() {
		return true;
	}
}
