package city.tests.agents.mocks;

import java.util.List;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.agents.BusAgent.MyBusPassenger;
import city.agents.interfaces.Bus;
import city.buildings.interfaces.BusStop;
import city.roles.interfaces.BusPassenger;
import city.tests.bases.mocks.MockAgent;

public class MockBus extends MockAgent implements Bus {

	// Data
	
	public EventLog log = new EventLog();

	// Constructor
	
	public MockBus(String name) {
		super();
	}
	
	// Messages
	
	@Override
	public void msgImOnBus(BusPassenger busPassengerRole, BusStop destination) {
		log.add(new LoggedEvent("BusPassenger " + busPassengerRole.getPerson().getName() + " is going to " + destination.getName()));
		busPassengerRole.msgImAtYourDestination();
	}

	@Override
	public void msgImOffBus(BusPassenger busPassengerRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtBusDestination() {
		// TODO Auto-generated method stub
		
	}

	// Getters
	
	@Override
	public BUSSTATE getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BUSEVENT getEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MyBusPassenger> getPassengerList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BusStop getCurrentStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BusStop getNextStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEarnedMoney() {
		// TODO Auto-generated method stub
		return 0;
	}

}
