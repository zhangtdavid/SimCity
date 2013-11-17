package city;

import city.interfaces.Person;
import city.interfaces.RoleInterface;
import utilities.EventLog;

/**
 * The base class for all SimCity201 role mocks.
 * 
 * This class implements most things required by RoleInterface so that
 * mocks themselves may focus only on their particular behaviors.
 * 
 * This class also implements the event log used by all mocks.
 */
public abstract class MockRole implements RoleInterface {
	
	// Data
	
	private Person person;
	private boolean active;
	
	public EventLog log = new EventLog();
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	/**
	 * In mocks, the scheduler should always return false ("finished") because no code is being run.
	 */
	@Override
	public boolean runScheduler() {
		return false;
	}
	
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
	
	// Utilities

}
