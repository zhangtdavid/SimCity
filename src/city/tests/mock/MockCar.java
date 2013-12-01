package city.tests.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.BuildingInterface;
import city.abstracts.MockAgent;
import city.interfaces.Car;
import city.interfaces.CarPassenger;

public class MockCar extends MockAgent implements Car {
	
	public EventLog log = new EventLog();

	public MockCar(String name) {
		super();
	}

	@Override
	public void msgIWantToDrive(CarPassenger carPassenger, BuildingInterface destination) {
		log.add(new LoggedEvent("CarPassenger " + carPassenger.getPerson().getName() + " is going to " + destination.getName()));
		carPassenger.msgImAtDestination();
	}

	@Override
	public void msgAtDestination() {
		// TODO
	}

}
