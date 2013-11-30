package city.interfaces;

import java.util.Date;

import city.Application;
import city.buildings.ResidenceBaseBuilding;

public interface Resident extends RoleInterface {
	
	// Data
	
	public static enum STATE {needToPayRent, none}; 
	public static final long RENT_DUE_INTERVAL = (Application.INTERVAL * 336); // 7 days
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	// Actions

	public void payRent();
	
	// Getters
	
	public Date getRentDueDate();
	public boolean isLandlord();
	
	// Setters
	
	public void setActive();
	public void setResidence(ResidenceBaseBuilding b);
	public void setLandlord(Landlord l);
	
	// Utilities
	
	public boolean rentIsDue();

}
