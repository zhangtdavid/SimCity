package city.interfaces;
import city.AgentInterface;
import city.buildings.BusStopBuilding;

public interface Bus extends AgentInterface {

	// Data
	
	public static final int BUS_FARE = 2;
	
	// Constructor
	
	// Messages
	
	void msgImOnBus(BusPassenger busPassengerRole,BusStopBuilding destination);
	void msgImOffBus(BusPassenger busPassengerRole);
	void msgAtDestination();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes	
	
}
