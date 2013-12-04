package city.animations;

import java.awt.*;

import city.Animation;
import city.animations.interfaces.MarketAnimatedCashier;
import city.gui.buildings.MarketPanel;
import city.interfaces.MarketCashier;

public class MarketCashierAnimation extends Animation implements MarketAnimatedCashier {
	private MarketCashier cashier = null;
	
//	Location Information
//	=====================================================================	
	private int xPos = MarketPanel.CASHIERX, yPos = MarketPanel.CASHIERY;
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
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, MarketPanel.RECTDIM, MarketPanel.RECTDIM);
	}

	public boolean isPresent() {
		return true;
	}
}
