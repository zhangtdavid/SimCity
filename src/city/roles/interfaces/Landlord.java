package city.roles.interfaces;

import java.util.List;

import city.bases.ResidenceBuilding;

public interface Landlord {

	// Data

	
	// Constructor
	
	// Messages
	
	public void msgHeresRent(int d);
	
	// Scheduler
	
	// Actions
	
	// Getters
	public List<ResidenceBuilding> getResidences();
	
	// Setters
	public void setRent(int d);
	public void setResident(Resident r);
	
	// Utilities
	public void addResidence(ResidenceBuilding b);
	public void removeResidence(ResidenceBuilding b);
}
