package city.tests.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Building;
import city.MockAgent;
import city.interfaces.Car;
import city.interfaces.CarPassenger;

public class MockCar extends MockAgent implements Car {
	
	public EventLog log = new EventLog();

	public MockCar(String name) {
		super();
	}

	@Override
	public void msgIWantToDrive(CarPassenger carPassenger, Building destination) {
		log.add(new LoggedEvent("CarPassenger " + carPassenger.getPerson().getName() + " is going to " + destination.getName()));
	}

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub RestaurantZhang 92f655cfd5
		
	}

}
