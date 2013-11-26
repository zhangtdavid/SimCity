package city.interfaces;
import city.buildings.BusStopBuilding;

public interface Bus extends AgentInterface {
	
	public static final int busFare = 2; // Fare price of bus
	
	void msgImOnBus(BusPassenger busPassengerRole,BusStopBuilding destination);

	void msgImOffBus(BusPassenger busPassengerRole);

	void msgAtDestination();

	// Data
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes	
	
}
