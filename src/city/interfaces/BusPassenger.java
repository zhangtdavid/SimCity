package city.interfaces;

import city.BuildingInterface;
import city.RoleInterface;

public interface BusPassenger extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public void msgBusIsHere(Bus b);
	public void msgImAtYourDestination();
	public void msgAtWaitingStop();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public BuildingInterface getDestination(); 
	public BuildingInterface getBusStopToWaitAt();
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
