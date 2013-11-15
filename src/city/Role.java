package city;

import utilities.StringUtil;
import city.agents.PersonAgent;
import city.buildings.MarketBuilding;

/**
 * The base class for all SimCity201 roles.
 * 
 * Roles are like unthreaded agents, having messages, actions, and a scheduler.
 */
public abstract class Role {
	
	// Data
	
	private PersonAgent person;
	private boolean active;
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	public Role() {
		active = false;
    }
	
	public abstract boolean runScheduler();	
	
	// Actions
	
	// Getters
	
	public PersonAgent getPerson() {
		return person;
	}
	
	public boolean getActive() {
		return active;
	}
	
	// Setters
	
	public void setPerson(PersonAgent p) {
		this.person = p;
	}
	
	public void setActive() {
		this.active = true;
	}
	
	public void setInactive() {
		this.active = false;
	}
	
	// Utilities
	
	protected void stateChanged() {
		person.stateChange.release();
	}
	
    protected void print(String msg) {
        person.print(msg);
    }

    protected void print(String msg, Throwable e) {
        person.print(msg, e);
    }

}
