package city.interfaces;

import java.util.Date;

import city.Role;

public interface Person extends AgentInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public void guiAtDestination();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public String getName();
	
	// Setters
	
	public void setAnimation(city.animations.interfaces.AnimatedPerson p);
	public void setCar(Car c);
	public void setDate(Date d);
	public void setOccupation(Role r);
	
	// Utilities
	
	public void addRole(Role r);
	
	// Classes
	
}
