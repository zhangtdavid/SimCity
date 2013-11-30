package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Animation;
import city.animations.interfaces.MarketAnimatedEmployee;
import city.interfaces.MarketEmployee;

public class MarketEmployeeAnimation extends Animation implements MarketAnimatedEmployee {
	private MarketEmployee employee = null;

//	Fixed Numbers
//	=====================================================================
	private static final int CRECTDIM = 20;
	
//	Location Information
//	=====================================================================	
	private int xPos = 20, yPos = 20;
	private int xDestination = xPos, yDestination = yPos;

	public MarketEmployeeAnimation(MarketEmployee e) {
		employee = e;
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

	public void doGoToPhone() {
		// TODO Auto-generated method stub
		
	}
	
	public void doDeliverItems() {
		// TODO Auto-generated method stub
		
	}

	public void doCollectItems() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doGoToCounter() {
		// TODO Auto-generated method stub
		
	}
}
