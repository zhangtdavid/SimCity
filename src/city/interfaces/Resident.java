package city.interfaces;

import java.util.Date;

import city.Application;

public interface Resident extends RoleInterface {
	
	// Data
	
	public static enum STATE {needToPayRent, needToPayMaintenance, none}; 
	public static long RENT_DUE_INTERVAL = (Application.INTERVAL * 336); // 7 days
	
	// Constructor
	
	// Messages
	
	public void msgPayForMaintenance(double d);
	
	// Scheduler
	
	// Actions
	
	public void payMaintenance();
	public void payRent();
	
	// Getters
	
	public Date getRentDueDate();
	
	// Setters
	
	// Utilities
	
	public boolean rentIsDue();
	
	// Classes

}
