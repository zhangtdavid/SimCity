package city.tests.mock;

import utilities.EventLog;
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
		
	}

}
