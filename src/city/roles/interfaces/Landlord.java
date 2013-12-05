package city.roles.interfaces;

import city.Application;
import city.bases.ResidenceBuilding;

public interface Landlord {

	// Data

	public static final long MURPHY_INTERVAL = (long) (Math.floor(1+Math.random()*5))*(Application.INTERVAL * 336); // Interval at which house needs maintenance. 7-42 days. 
	
	// Constructor
	
	// Messages
	
	public void msgHeresRent(int d);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	public void setResidence(ResidenceBuilding b);
	public void setRent(int d);
	
	// Utilities
	
	public void addResident(Resident r);
	public void removeResident(Resident r);
	public void changeLandlord(Resident r);

}
