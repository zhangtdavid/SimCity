package city.tests.animations.mock;

import java.awt.Graphics2D;

import utilities.RestaurantZhangTable;
import city.Building;
import city.MockAnimation;
import city.animations.interfaces.AnimatedBus;
import city.animations.interfaces.RestaurantZhangAnimatedCustomer;
import city.buildings.BusStopBuilding;
import city.interfaces.Bus;
import city.interfaces.RestaurantZhangCustomer;

public class MockAnimatedBus extends MockAnimation implements AnimatedBus {

	private Bus bus;

	public MockAnimatedBus(Bus b) {
		this.bus = b;
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
		bus.msgAtDestination();
	}

	@Override
	public void DoGoToNextStop(BusStopBuilding nextStop) {
		bus.msgAtDestination();
	}
}
