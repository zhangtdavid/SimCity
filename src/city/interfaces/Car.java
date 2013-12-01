package city.interfaces;

import city.AgentInterface;
import city.Building;

public interface Car extends AgentInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	public void msgIWantToDrive(CarPassenger carPassenger, Building destination);
	public void msgAtDestination();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes	
	
}
