package city.interfaces;

import city.Building;

public interface Car extends AgentInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	abstract void msgIWantToDrive(CarPassenger carPassenger, Building destination);

	abstract void msgAtDestination();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes	
	
}
