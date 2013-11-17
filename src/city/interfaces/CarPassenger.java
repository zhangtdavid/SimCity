package city.interfaces;

import city.Building;

public interface CarPassenger extends AbstractRole {

	// Data
	
	// Constructor
	
	// Messages
	public abstract void msgImGoingToDrive(Building dest, Car c);
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
