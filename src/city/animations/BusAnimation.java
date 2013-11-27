package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Animation;
import city.Building;
import city.animations.interfaces.AnimatedBus;
import city.buildings.BusStopBuilding;
import city.interfaces.Bus;

public class BusAnimation extends Animation implements AnimatedBus {

	private Bus bus = null;

	private int xPos , yPos;//default waiter position
	private int xDestination, yDestination;//default start position

	private boolean atDestination = true;

	private boolean isPresent = true;

	public static final int SIZE = 25;

	public BusAnimation(Bus b, Building startingBuilding) {
		bus = b;
		xDestination = xPos = startingBuilding.getCityViewBuilding().x;
		yDestination = yPos = startingBuilding.getCityViewBuilding().y;
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

		if(xPos == xDestination && yPos == yDestination && atDestination == false) {
			atDestination = true;
			bus.msgAtDestination();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.CYAN);
		g.fillRect(xPos, yPos, SIZE, SIZE);
		g.setColor(Color.black);
		g.drawString("Bus", xPos, yPos + 10);
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	@Override
	public void goToDestination(Building destination) {
		xDestination = destination.getCityViewBuilding().x;
		yDestination = destination.getCityViewBuilding().y;
		atDestination = false;
	}

	@Override
	public void DoGoToNextStop(BusStopBuilding nextStop) {
		xDestination = nextStop.getCityViewBuilding().x;
		yDestination = nextStop.getCityViewBuilding().y;
		atDestination = false;
	}

	public void setXPos(int x) {
		xPos = x;
		
	}

	public void setYPos(int y) {
		yPos = y;
		
	}
}
