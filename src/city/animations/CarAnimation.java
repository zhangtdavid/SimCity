package city.animations;

import java.awt.*;

import utilities.RestaurantZhangTable;
import city.Animation;
import city.Building;
import city.agents.CarAgent;
import city.animations.interfaces.AnimatedCar;
import city.gui.CityViewBuilding;
import city.interfaces.Car;
import city.interfaces.CarPassenger;

public class CarAnimation extends Animation implements AnimatedCar {

	private Car car = null;

	private int xPos , yPos;//default waiter position
	private int xDestination, yDestination;//default start position

	private String foodString = null;
	private static final int TEXTHEIGHT = 14;

	private boolean atDestination = true;

	public static final int SIZE = 50;

	public CarAnimation(Car c, Building startingBuilding) {
		car = c;
		xDestination = xPos = startingBuilding.cityBuilding.x;
		yDestination = yPos = startingBuilding.cityBuilding.y;
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

		if(xPos == xDestination && yPos == yDestination && atDestination == false) {
			atDestination = true;
			car.msgAtDestination();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.PINK);
		g.fillRect(xPos, yPos, SIZE, SIZE);
	}

	public boolean isPresent() {
		return true;
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
	public void setXPos(int x) {
		xPos = x;
	}

	@Override
	public void setYPos(int y) {
		yPos = y;
	}
}
