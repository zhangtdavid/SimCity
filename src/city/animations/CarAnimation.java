package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import city.Application;
import city.agents.interfaces.Car;
import city.animations.interfaces.AnimatedCar;
import city.bases.Animation;
import city.bases.interfaces.BuildingInterface;
import city.gui.CityRoad;

public class CarAnimation extends Animation implements AnimatedCar {
	
	// Data
	
	private Car car = null;

	private int xPos, yPos;
	private int xDestination, yDestination;

	private BuildingInterface currentBuilding;
	private BuildingInterface destinationBuilding = null;
	private CityRoad startingRoad = null;
	private CityRoad endRoad = null;
	private Rectangle rectangle;
	
	private boolean atDestinationRoad = false;
	private boolean atDestination = true;

	public CarAnimation(Car c, BuildingInterface startingBuilding) {
		car = c;
		xDestination = xPos = startingBuilding.getCityViewBuilding().getX();
		yDestination = yPos = startingBuilding.getCityViewBuilding().getY();
		rectangle = new Rectangle(xPos, yPos, SIZE, SIZE);
		currentBuilding = startingBuilding;
	}
	
	// Paint
	
	public void updatePosition() {
		// Getting on the first road
		if(startingRoad != null) {
			if(startingRoad.setVehicle(this) == false && startingRoad.getVehicle() != this) {
				return;
			}
			if (xPos < startingRoad.getX() && !startingRoad.getHorizontal())
				xPos++;
			else if (xPos > startingRoad.getX() && !startingRoad.getHorizontal())
				xPos--;

			if (yPos < startingRoad.getY() && startingRoad.getHorizontal())
				yPos++;
			else if (yPos > startingRoad.getY() && startingRoad.getHorizontal())
				yPos--;
			if(xPos == startingRoad.getX() && yPos == startingRoad.getY())
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
		g.setColor(Color.red);
		rectangle.setLocation(xPos, yPos);
		if(car != null)
			g.drawString(car.getClass().getSimpleName(), xPos, yPos);
	}
	
	// Action
	
	public void goToDestination(BuildingInterface destination) {
		destinationBuilding = destination;
		startingRoad = Application.CityMap.findClosestRoad(currentBuilding);
		startingRoad.setVehicle(this);
		xDestination = destination.getCityViewBuilding().getX();
		yDestination = destination.getCityViewBuilding().getY();
		endRoad = Application.CityMap.findClosestRoad(destination);
		atDestination = false;
		this.car.print("In Destination: " + endRoad);
	}
	
	// Getters
	
	@Override
	public int getXPos() {
		return xPos;
	}

	@Override
	public int getYPos() {
		return yPos;
	}
	
	@Override
	public Car getCar() {
		return car;
	}
	
	@Override
	public BuildingInterface getDestinationBuilding() {
		return destinationBuilding;
	}
	
	@Override
	public CityRoad getEndRoad() {
		return endRoad;
	}
	
	@Override
	public boolean getAtDestinationRoad() {
		return atDestinationRoad;
	}
	
	@Override
	public CityRoad getStartingRoad() {
		return startingRoad;
	}
	
	// Setters
	
	@Override
	public void setXPos(int x) {
		xPos = x;
	}

	@Override
	public void setYPos(int y) {
		yPos = y;
	}

	@Override
	public void setDestinationBuilding(BuildingInterface destinationBuilding) {
		this.destinationBuilding = destinationBuilding;
	}

	@Override
	public void setEndRoad(CityRoad endRoad) {
		this.endRoad = endRoad;
	}

	@Override
	public void setAtDestinationRoad(boolean atDestinationRoad) {
		this.atDestinationRoad = atDestinationRoad;
	}

	@Override
	public void setStartingRoad(CityRoad startingRoad) {
		this.startingRoad = startingRoad;
	}
	
	// Utilities
	
	@Override
	public boolean contains(int x, int y) {
		return rectangle.contains(x, y);
	}
}
