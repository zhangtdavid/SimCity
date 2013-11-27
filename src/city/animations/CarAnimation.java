package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Animation;
import city.Application;
import city.Building;
import city.animations.interfaces.AnimatedCar;
import city.gui.CityRoad;
import city.interfaces.Car;

public class CarAnimation extends Animation implements AnimatedCar {

	private Car car = null;

	private int xPos , yPos;//default waiter position
	private int xDestination, yDestination;//default start position

	Building currentBuilding;
	Building destinationBuilding = null;
	public CityRoad startingRoad = null;
	public CityRoad endRoad = null;
	
	public boolean atDestinationRoad = false;
	private boolean atDestination = true;

	public static final int SIZE = 25;

	public CarAnimation(Car c, Building startingBuilding) {
		car = c;
		xDestination = xPos = startingBuilding.getCityViewBuilding().x;
		yDestination = yPos = startingBuilding.getCityViewBuilding().y;
		currentBuilding = startingBuilding;
	}

	public void updatePosition() {
		// Getting on the first road
		if(startingRoad != null) {
			if(startingRoad.addVehicle(this) == false && startingRoad.vehicle != this) {
				return;
			}
			if (xPos < startingRoad.x && !startingRoad.isHorizontal)
				xPos++;
			else if (xPos > startingRoad.x && !startingRoad.isHorizontal)
				xPos--;

			if (yPos < startingRoad.y && startingRoad.isHorizontal)
				yPos++;
			else if (yPos > startingRoad.y && startingRoad.isHorizontal)
				yPos--;
			if(xPos == startingRoad.x && yPos == startingRoad.y)
				startingRoad = null;
		}
		// Getting on the destination road
		if(atDestinationRoad == true) {
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;
	
			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
		}
		if(xPos == xDestination && yPos == yDestination && atDestination == false) {
			atDestination = true;
			atDestinationRoad = false;
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
		startingRoad = Application.CityMap.findClosestRoad(currentBuilding);
		startingRoad.addVehicle(this);
		xDestination = destination.getCityViewBuilding().x;
		yDestination = destination.getCityViewBuilding().y;
		endRoad = Application.CityMap.findClosestRoad(destination);
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
