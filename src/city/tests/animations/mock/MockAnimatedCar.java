package city.tests.animations.mock;

import java.awt.Graphics2D;

import city.Building;
import city.MockAnimation;
import city.animations.interfaces.AnimatedCar;
import city.interfaces.Car;

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