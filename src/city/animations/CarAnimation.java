package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Animation;
import city.Application;
import city.BuildingInterface;
import city.animations.interfaces.AnimatedCar;
import city.gui.CityRoad;
import city.interfaces.Car;

public class CarAnimation extends Animation implements AnimatedCar {
	
	// Data
	
	private Car car = null;

	private int xPos, yPos;
	private int xDestination, yDestination;

	private BuildingInterface currentBuilding;
	private BuildingInterface destinationBuilding = null;
	private CityRoad startingRoad = null;
	private CityRoad endRoad = null;
	
	private boolean atDestinationRoad = false;
	private boolean atDestination = true;

	public CarAnimation(Car c, BuildingInterface startingBuilding) {
		car = c;
		xDestination = xPos = startingBuilding.getCityViewBuilding().getX();
		yDestination = yPos = startingBuilding.getCityViewBuilding().getY();
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
		atDestinationRoad = false;
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
	
	public BuildingInterface getDestinationBuilding() {
		return destinationBuilding;
	}
	
	public CityRoad getEndRoad() {
		return endRoad;
	}
	
	public boolean getAtDestinationRoad() {
		return atDestinationRoad;
	}
	
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

	public void setDestinationBuilding(BuildingInterface destinationBuilding) {
		this.destinationBuilding = destinationBuilding;
	}

	public void setEndRoad(CityRoad endRoad) {
		this.endRoad = endRoad;
	}

	public void setAtDestinationRoad(boolean atDestinationRoad) {
		this.atDestinationRoad = atDestinationRoad;
	}

	public void setStartingRoad(CityRoad startingRoad) {
		this.startingRoad = startingRoad;
	}
}
