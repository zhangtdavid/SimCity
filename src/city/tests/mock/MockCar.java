package city.tests.mock;

import utilities.EventLog;
import city.Building;
import city.Mock;
import city.interfaces.Car;
import city.interfaces.CarPassenger;

public class MockCar extends Mock implements Car {
	
	public EventLog log = new EventLog();

	public MockCar(String name) {
		super(name);
	}

	@Override
	public void msgIWantToDrive(CarPassenger carPassenger, Building destination) {
		
	}

}
