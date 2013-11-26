package city.interfaces;

import java.util.Date;

import city.Application;
import city.buildings.ResidenceBaseBuilding;

public interface Resident extends RoleInterface {
	
	// Data
	
	public static enum STATE {needToPayRent, none}; 
	public static long RENT_DUE_INTERVAL = (Application.INTERVAL * 336); // 7 days
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	// Actions
	
	//public abstract void payMaintenance();
	public abstract void payRent();
	
	// Getters
	public abstract Date getRentDueDate();
	public abstract boolean isLandlord();
	
	// Setters
	public abstract void setActive();
	public abstract void setResidence(ResidenceBaseBuilding b);
	public abstract void setLandlord(Landlord l);
	// Utilities
	
	public abstract boolean rentIsDue();


	
	// Classes

}
