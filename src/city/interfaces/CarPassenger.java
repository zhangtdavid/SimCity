package city.interfaces;

import city.BuildingInterface;
import city.RoleInterface;

public interface CarPassenger extends RoleInterface {

	// Data
	
	static enum CarPassengerState {NOTDRIVING, DRIVING};
	static enum CarPassengerEvent {NONE, ATCAR, ATDESTINATION};
	
	// Constructor
	
	// Messages
	
	public void msgImAtCar();
	public void msgImAtDestination();

	// Scheduler
	
	// Actions
	
	// Getters
	
	public CarPassengerState getState();
	public CarPassengerEvent getEvent();
	public Car getCar();
	public BuildingInterface getDestination();
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
