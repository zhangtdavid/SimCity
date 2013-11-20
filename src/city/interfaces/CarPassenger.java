package city.interfaces;

import city.Building;

public interface CarPassenger extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	abstract void msgImGoingToDrive();
	abstract void msgImAtCar();
	abstract void msgImAtDestination();
	
	public abstract boolean getActive(); // TODO Why is this necessary?
	public abstract boolean runScheduler();

	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
