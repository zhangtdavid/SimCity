package city.tests.animations.mocks;

import java.awt.Graphics2D;

import city.agents.interfaces.Car;
import city.animations.interfaces.AnimatedCar;
import city.bases.interfaces.BuildingInterface;
import city.gui.CityRoad;
import city.tests.bases.mocks.MockAnimation;

public class MockAnimatedCar extends MockAnimation implements AnimatedCar {

	private Car car;

	public MockAnimatedCar(Car c) {
		this.car = c;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToDestination(BuildingInterface destination) {
		// TODO Auto-generated method stub
		car.msgAtDestination();
	}

	@Override
	public void setXPos(int x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setYPos(int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BuildingInterface getDestinationBuilding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CityRoad getEndRoad() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getAtDestinationRoad() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CityRoad getStartingRoad() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Car getCar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDestinationBuilding(BuildingInterface destinationBuilding) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEndRoad(CityRoad endRoad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAtDestinationRoad(boolean atDestinationRoad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStartingRoad(CityRoad startingRoad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setUgly(boolean newUgly) {
		// TODO Auto-generated method stub
		
	}
}