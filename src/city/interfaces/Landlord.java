package city.interfaces;

import city.Application;
import city.agents.PersonAgent;

public interface Landlord {

	// Data
	public static long MURPHY_INTERVAL = 
			(long)(Math.floor(1+Math.random()*5))*(Application.INTERVAL * 336); // 7~42 days
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
