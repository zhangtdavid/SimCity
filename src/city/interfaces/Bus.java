package city.interfaces;
import java.util.List;

import city.AgentInterface;
import city.agents.BusAgent.MyBusPassenger;

public interface Bus extends AgentInterface {

	// Data
	
	static final int BUS_FARE = 2;
	static enum BUSSTATE {DRIVING, DROPPINGPASSENGERSOFF, PICKINGPEOPLEUP};
	static enum BUSEVENT {NONE, ATSTOP};
	
	// Constructor
	
	// Messages
	
	void msgAtBusDestination();
	void msgImOnBus(BusPassenger busPassengerRole,BusStop destination);
	void msgImOffBus(BusPassenger busPassengerRole);
	void msgAtDestination();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public BUSSTATE getState();
	public BUSEVENT getEvent();
	public List<MyBusPassenger> getPassengerList();
	public BusStop getCurrentStop();
	public BusStop getNextStop();
	public int getEarnedMoney();
	
	// Setters
	
	// Utilities
	
	// Classes	
	
}
