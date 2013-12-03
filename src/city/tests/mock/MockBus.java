package city.tests.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.abstracts.MockAgent;
import city.interfaces.Bus;
import city.interfaces.BusPassenger;
import city.interfaces.BusStop;

public class MockBus extends MockAgent implements Bus {

	public EventLog log = new EventLog();

	public MockBus(String name) {
		super();
	}
	
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

}
