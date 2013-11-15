package city.interfaces;

import java.util.Map;

import city.Building;
import city.agents.CarAgent;
import city.roles.MarketCustomerDeliveryRole;

public interface CarPassenger {

	// Data
	
	// Constructor
	
	// Messages
	public abstract void msgImGoingToDrive(Building dest, CarAgent c);
	public abstract void msgImAtCar();
	public abstract void msgImAtDestination();
	
	public abstract boolean getActive(); // TODO Why is this necessary?
	public abstract boolean runScheduler();

	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
