package city;

import city.interfaces.RoleInterface;
import city.interfaces.Person;

/**
 * The base class for all SimCity201 roles.
 * 
 * Roles are like unthreaded agents, having messages, actions, and a scheduler.
 */
public abstract class Role implements RoleInterface {
	
	// Data
	
	private Person person;
	private boolean active;
	
	// Constructor
	
	public Role() {
		active = false;
	}
	
	public Role(Person p) {
		person = p;
		active = false;
	}
	
	// Messages
	
	// Scheduler
	
	public abstract boolean runScheduler();	
	
	// Actions
	
	// Getters
	
	public Person getPerson() {
		return person;
	}
	
	public boolean getActive() {
		return active;
	}
	
	// Setters
	
	public void setPerson(Person p) {
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
		person.stateChanged();
	}
	
    protected void print(String msg) {
        person.print(msg);
    }

    protected void print(String msg, Throwable e) {
        person.print(msg, e);
    }

}
