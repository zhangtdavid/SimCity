package city.tests.animations.mock;

import java.awt.Graphics2D;

import utilities.RestaurantZhangTable;
import city.Building;
import city.MockAnimation;
import city.animations.interfaces.AnimatedBus;
import city.animations.interfaces.AnimatedCar;
import city.animations.interfaces.RestaurantZhangAnimatedCustomer;
import city.buildings.BusStopBuilding;
import city.interfaces.Bus;
import city.interfaces.Car;
import city.interfaces.RestaurantZhangCustomer;

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
	public void goToDestination(Building destination) {
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
}