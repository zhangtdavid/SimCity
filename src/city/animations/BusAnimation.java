package city.animations;

import java.awt.*;

import city.Animation;
import city.Application;
import city.Building;
import city.animations.interfaces.AnimatedBus;
import city.animations.interfaces.AnimatedCar;
import city.buildings.BusStopBuilding;
import city.interfaces.Bus;
import city.interfaces.Car;

public class BusAnimation extends Animation implements AnimatedBus {

	private Bus bus = null;

	private int xPos , yPos;//default waiter position
	private int xDestination, yDestination;//default start position

	public boolean atDestination = true;

	public static final int SIZE = 25;

	public BusAnimation(Bus b, Building startingBuilding) {
		bus = b;
		xDestination = xPos = Application.CityMap.findClosestRoad(startingBuilding).x;
		yDestination = yPos = Application.CityMap.findClosestRoad(startingBuilding).y;
	}

	public void updatePosition() {
		if(xPos <= xDestination + 1 && xPos >= xDestination - 1
				&& yPos <= yDestination + 1 && yPos >= yDestination - 1 && atDestination == false) {
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

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	@Override
	public void goToDestination(Building destination) {
		xDestination = destination.cityBuilding.x;
		yDestination = destination.cityBuilding.y;
		atDestination = false;
	}

	@Override
	public void DoGoToNextStop(BusStopBuilding nextStop) {
		xDestination = Application.CityMap.findClosestRoad(nextStop).x;
		yDestination = Application.CityMap.findClosestRoad(nextStop).y;
		atDestination = false;
	}

	public void setXPos(int x) {
		xPos = x;
		
	}

	public void setYPos(int y) {
		yPos = y;
		
	}
}
