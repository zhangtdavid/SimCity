package city.interfaces;

import city.agents.PersonAgent;

public interface Landlord {

	// Data
	
	// Constructor
	
	// Messages
	public void msgHeresRent(double d);
	public void msgHeresMaintenanceFee(double d);
	public void msgFoundProblem();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	public void addResident(Resident r);
	public void removeResident(Resident r);
	
	// Utilities
	
	// Classes	
}
