package city.interfaces;

import java.util.Date;

import city.Agent;
import city.Role;

public interface Person {

	// Data
	
	// Constructor
	
	// Messages
	public void guiAtDestination();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	public void setAnimation(city.animations.interfaces.Person p);
	public void setDate(Date d);
	public void setOccupation(Role r);
	public void setCar(Agent c); // TODO replace this with the appropriate object type
	
	// Utilities
	
	public void addRole(Role r);
	
	// Classes
	
}
