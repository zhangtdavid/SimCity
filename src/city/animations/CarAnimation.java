package city.animations;

import java.awt.*;

import utilities.RestaurantZhangTable;
import city.Animation;
import city.Application;
import city.Building;
import city.agents.CarAgent;
import city.animations.interfaces.AnimatedCar;
import city.gui.CityRoad;
import city.gui.CityViewBuilding;
import city.interfaces.Car;
import city.interfaces.CarPassenger;

public class CarAnimation extends Animation implements AnimatedCar {

	private Car car = null;

	private int xPos , yPos;//default waiter position
	private int xDestination, yDestination;//default start position
	
	Building currentBuilding;
	Building destinationBuilding = null;
	CityRoad currentRoad = null;
	
	private boolean atRoad = false;
	private boolean atDestination = true;

	public static final int SIZE = 25;

	public CarAnimation(Car c, Building startingBuilding) {
		car = c;
		xDestination = xPos = startingBuilding.cityBuilding.x;
		yDestination = yPos = startingBuilding.cityBuilding.y;
		currentBuilding = startingBuilding;
	}

	public void updatePosition() {
//		if (xPos < xDestination)
//			xPos++;
//		else if (xPos > xDestination)
//			xPos--;
//
//		if (yPos < yDestination)
//			yPos++;
//		else if (yPos > yDestination)
//			yPos--;
		if(currentRoad != null) {
			if(currentRoad.addVehicle(this) == false) {
				return;
			}
			if (xPos < currentRoad.x)
				xPos++;
			else if (xPos > currentRoad.x)
				xPos--;
	
			if (yPos < currentRoad.y)
				yPos++;
			else if (yPos > currentRoad.y)
				yPos--;
			if(xPos == currentRoad.x && yPos == currentRoad.y)
				currentRoad = null;
		}
		if(xPos == xDestination && yPos == yDestination && atDestination == false) {
			atDestination = true;
			car.msgAtDestination();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.PINK);
		g.fillRect(xPos, yPos, SIZE, SIZE);
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void goToDestination(Building destination) {
		destinationBuilding = destination;
		currentRoad = Application.CityMap.findClosestRoad(currentBuilding);
		currentRoad.addVehicle(this);
		xDestination = destination.cityBuilding.x;
		yDestination = destination.cityBuilding.y;
		atDestination = false;
	}

	@Override
	public void setXPos(int x) {
		xPos = x;
	}

	@Override
	public void setYPos(int y) {
		yPos = y;
	}
}
