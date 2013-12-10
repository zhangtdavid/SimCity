package city.tests.animations.mocks;

import java.awt.Graphics2D;

import utilities.LoggedEvent;
import city.animations.interfaces.AnimatedBusPassenger;
import city.bases.interfaces.BuildingInterface;
import city.roles.interfaces.BusPassenger;
import city.tests.bases.mocks.MockAnimation;

public class MockAnimatedBusPassenger extends MockAnimation implements AnimatedBusPassenger {
	
	BusPassenger busPassenger;
	
	public MockAnimatedBusPassenger(BusPassenger busPassenger) {
		this.busPassenger = busPassenger;
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
		log.add(new LoggedEvent("BusPassenger " + busPassenger.getPerson().getName() + " is going to destination " + destination.getName()));
	}

	@Override
	public void getOffBus() {
		log.add(new LoggedEvent("BusPassenger " + busPassenger.getPerson().getName() + " is getting off the bus"));		
	}

	@Override
	public void goToBus() {
		log.add(new LoggedEvent("BusPassenger " + busPassenger.getPerson().getName() + " is getting on the bus"));
	}

	@Override
	public void setUgly(boolean newUgly) {
		// TODO Auto-generated method stub
		
	}
}