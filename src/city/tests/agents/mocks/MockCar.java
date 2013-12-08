package city.tests.agents.mocks;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.agents.interfaces.Car;
import city.agents.interfaces.Person;
import city.bases.interfaces.BuildingInterface;
import city.roles.interfaces.CarPassenger;
import city.tests.bases.mocks.MockAgent;

public class MockCar extends MockAgent implements Car {
	
	// Data
	
	public EventLog log = new EventLog();
	
	private BuildingInterface destination;

	// Constructor
	
	public MockCar(String name) {
		super();
	}

	// Messages
	
	@Override
	public void msgIWantToDrive(CarPassenger carPassenger, BuildingInterface destination) {
		log.add(new LoggedEvent("CarPassenger " + carPassenger.getPerson().getName() + " is going to " + destination.getName()));
		this.destination = destination;
		carPassenger.msgImAtDestination();
	}

	@Override
	public void msgAtDestination() {
		// TODO
	}

	@Override
	public void msgImAtCarDestination() {
		// TODO Auto-generated method stub
		
	}

	// Getters
	
	@Override
	public CARSTATE getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CAREVENT getEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CarPassenger getPassenger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BuildingInterface getDestination() {
		return destination;
	}

	@Override
	public Person getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

}
