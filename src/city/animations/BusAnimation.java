package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Application;
import city.agents.interfaces.Bus;
import city.animations.interfaces.AnimatedBus;
import city.bases.Animation;
import city.bases.Building;
import city.buildings.interfaces.BusStop;

public class BusAnimation extends Animation implements AnimatedBus {

	private Bus bus = null;

	private int xPos , yPos;//default waiter position
	private int xDestination, yDestination;//default start position

	private boolean atDestination = true;

	public BusAnimation(Bus b, Building startingBuilding) {
		bus = b;
		xDestination = xPos = Application.CityMap.findClosestRoad(startingBuilding).getX();
		yDestination = yPos = Application.CityMap.findClosestRoad(startingBuilding).getY();
		Application.CityMap.findClosestRoad(startingBuilding).setVehicle(this);
		this.setVisible(true);
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
	
	// Actions

	@Override
	public void goToDestination(Building destination) {
		xDestination = destination.getCityViewBuilding().getX();
		yDestination = destination.getCityViewBuilding().getY();
		atDestination = false;
	}

	@Override
	public void doGoToNextStop(BusStop nextStop) {
		xDestination = Application.CityMap.findClosestRoad(nextStop).getX();
		yDestination = Application.CityMap.findClosestRoad(nextStop).getY();
		atDestination = false;
	}
	
	// Getters
	
	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
	
	public Bus getBus() {
		return bus;
	}
	
	public boolean isAtDestination() {
		return atDestination;
	}
	
	// Setters
	
	public void setXPos(int x) {
		xPos = x;
	}

	public void setYPos(int y) {
		yPos = y;
		
	}
}
