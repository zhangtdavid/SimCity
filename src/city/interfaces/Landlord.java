package city.interfaces;

import city.Application;
import city.buildings.ResidenceBaseBuilding;

public interface Landlord {

	// Data
	//calculate random interval every time you load up a program~
	// Every (more than 1 interval) the house needs maintenance.
	public static long MURPHY_INTERVAL = 
			(long)(Math.floor(1+Math.random()*5))*(Application.INTERVAL * 336); // 7~42 days
	
	// Constructor
	
	// Messages
	public abstract void msgHeresRent(int d);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	public abstract void addResident(Resident r);
	public abstract void removeResident(Resident r);
	/**
	 * "Hands off" the Landlord role to another resident.
	 * @param r
	 */
	public abstract void changeLandlord(Resident r);
	public abstract void setResidence(ResidenceBaseBuilding b);
	public abstract void setRent(int d);
	// Utilities


	
	// Classes	
}
