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
	private boolean activity;
	
	// Constructor
	
	public Role() {
		active = false;
		activity = false;
	}
	
	// Messages
	
	// Scheduler
	
	public abstract boolean runScheduler();	
	
	// Actions
	
	// Getters
	
	@Override
	public Person getPerson() {
		return person;
	}
	
	@Override
	public boolean getActive() {
		return active;
	}
	
    @Override
    public boolean getActivity() {
    	return activity;
    }
	
	
	// Setters
	
	@Override
	public void setPerson(Person p) {
		this.person = p;
	}
	
	@Override
	public void setActive() {
		this.active = true;
	}
	
	@Override
	public void setInactive() {
		this.active = false;
	}
	
	@Override
	public void setActivityBegun() {
		this.activity = true;
	}
	
	@Override
	public void setActivityFinished() {
		this.activity = false;
	}
	
	// Utilities
	
	protected void stateChanged() {
		person.stateChanged();
		activity = true;
	}
	
    protected void print(String msg) {
        person.print(msg);
    }

    protected void print(String msg, Throwable e) {
        person.print(msg, e);
    }

}
