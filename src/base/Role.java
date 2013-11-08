package base;

import city.agents.PersonAgent;

/**
 * The base class for all SimCity201 roles.
 * 
 * Roles are like unthreaded agents, having messages, actions, and a scheduler.
 */
public abstract class Role {
	
	// Data
	
	public PersonAgent person;
	public Boolean active;
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	public abstract boolean runScheduler();	
	
	// Actions
	
	// Getters
	
	public PersonAgent getPerson() {
		return person;
	}
	
	// Setters
	
	public void setPerson(PersonAgent p) {
		this.person = p;
	}
	
	// Utilities
	
	protected void stateChanged() {
		person.stateChange.release();
	}

}
