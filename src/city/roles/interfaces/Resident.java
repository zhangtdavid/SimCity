package city.roles.interfaces;

import java.util.Date;

import city.Application;
import city.bases.ResidenceBuilding;
import city.bases.interfaces.RoleInterface;

public interface Resident extends RoleInterface {
	
	// Data
	
	public static enum STATE {needToPayRent, none}; 
	public static final long RENT_DUE_INTERVAL = (Application.HALF_HOUR * 336); // 7 days
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	// Actions

	public void payRent();
	
	// Getters
	
	public Date getRentDueDate();
	
	// Setters
	
	public void setActive();
	public void setResidence(ResidenceBuilding b);
	public void setRentLastPaid(Date d);
	
	// Utilities
	
	public boolean rentIsDue();

}
