package city.tests.animations.mocks;

import java.awt.Graphics2D;

import city.agents.interfaces.Bus;
import city.animations.interfaces.AnimatedBus;
import city.bases.Building;
import city.buildings.interfaces.BusStop;
import city.tests.bases.mocks.MockAnimation;

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
	public void doGoToNextStop(BusStop nextStop) {
		bus.msgAtDestination();
	}

	@Override
	public void setUgly(boolean newUgly) {
		// TODO Auto-generated method stub
		
	}
}
