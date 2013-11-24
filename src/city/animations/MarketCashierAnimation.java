package city.animations;

import java.awt.*;

import city.Animation;
import city.animations.interfaces.MarketAnimatedCashier;
import city.interfaces.MarketCashier;

public class MarketCashierAnimation extends Animation implements MarketAnimatedCashier {
	private MarketCashier cashier = null;

//	Fixed Numbers
//	=====================================================================
	private static final int CRECTDIM = 20;
	
//	Location Information
//	=====================================================================	
	private int xPos = 50, yPos = 40;
	private int xDestination = xPos, yDestination = yPos;

	public MarketCashierAnimation(MarketCashier c) {
		cashier = c;
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
