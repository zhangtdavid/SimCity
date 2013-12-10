package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import city.agents.interfaces.Car;
import city.bases.interfaces.BuildingInterface;
import city.roles.interfaces.CarPassenger;
import city.roles.interfaces.MarketDeliveryPerson;
import city.tests.bases.mocks.MockRole;

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

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketDeliveryPerson getParent() {
		// TODO Auto-generated method stub
		return null;
	}

}
