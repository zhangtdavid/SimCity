package city.interfaces;

import city.AgentInterface;
import city.BuildingInterface;

public interface Car extends AgentInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	public void msgIWantToDrive(CarPassenger carPassenger, BuildingInterface destination);
	public void msgAtDestination();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes	
	
}
