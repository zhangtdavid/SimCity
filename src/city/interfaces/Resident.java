package city.interfaces;

public interface Resident {
	
	
	// Data
	enum ResidentState {needToPayRent, needToPayMaintenance, none}; 
	
	// Constructor
	
	// Messages
	public void msgPayForMaintenance(double d);
	
	// Scheduler
	
	// Actions
	public void payMaintenance();
	public void payRent();
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes

}
