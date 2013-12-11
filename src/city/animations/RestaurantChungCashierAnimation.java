package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.animations.interfaces.RestaurantChungAnimatedCashier;
import city.bases.Animation;
import city.gui.interiors.RestaurantChungPanel;
import city.roles.interfaces.RestaurantChungCashier;

public class RestaurantChungCashierAnimation extends Animation implements RestaurantChungAnimatedCashier {
	
//	Location Information
//	=====================================================================
	private int xPos = RestaurantChungPanel.CASHIERX, yPos = RestaurantChungPanel.CASHIERY;
	private int xDestination = xPos, yDestination = yPos;

	public RestaurantChungCashierAnimation(RestaurantChungCashier c) {
		// agent = c;
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
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, RestaurantChungPanel.RECTDIM, RestaurantChungPanel.RECTDIM);
	}

	public boolean isPresent() {
		return true;
	}
}
