package city.roles.interfaces;

import city.bases.ResidenceBuilding;

public interface Landlord {

	// Data

	
	// Constructor
	
	// Messages
	
	public void msgHeresRent(int d);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	public void setRent(int d);
	
	// Utilities
	public void addResidence(ResidenceBuilding b);
	public void removeResidence(ResidenceBuilding b);

}
