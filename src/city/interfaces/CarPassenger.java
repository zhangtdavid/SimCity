package city.interfaces;

import city.BuildingInterface;
import city.RoleInterface;

public interface CarPassenger extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public void msgImAtCar();
	public void msgImAtDestination();

	// Scheduler
	
	// Actions
	
	// Getters
	
	public BuildingInterface getDestination(); 
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
