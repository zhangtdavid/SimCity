package city.tests.mock;

import city.BuildingInterface;
import city.abstracts.MockRole;
import city.interfaces.Car;
import city.interfaces.CarPassenger;

public class MockCarPassenger extends MockRole implements CarPassenger {

	@Override
	public void msgImAtCar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BuildingInterface getDestination() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CarPassengerState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CarPassengerEvent getEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Car getCar() {
		// TODO Auto-generated method stub
		return null;
	}

}
