package city;

import utilities.EventLog;
import city.interfaces.Person;
import city.interfaces.RoleInterface;

/**
 * The base class for all SimCity201 role mocks.
 * 
 * This class implements most things required by RoleInterface so that
 * mocks themselves may focus only on their particular behaviors.
 * 
 */
public abstract class MockRole implements RoleInterface {
	
	// Data

	public Person person;
	private int salary;
	private boolean active;
	private boolean activity;
	private Building business;
	
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
	public int getSalary() {
		return salary;
	}
	
	@Override
	public Building getBusiness() {
		return business;
	}
	
	@Override
	public boolean getActive() {
		return active;
	}
	
    @Override
    public boolean getActivity() {
    	return activity;
    }
    
	@Override
	public int getShiftStart() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getShiftEnd() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	// Setters
	
	@Override
	public void setPerson(Person p) {
		this.person = p;
	}
	
	@Override
	public void setSalary(int s) {
		this.salary = s;
	}
	
	@Override
	public void setBusiness(Building b) {
		this.business = b;
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

	@Override
	public void setShift(int shiftStart, int shiftEnd) {
		// TODO Auto-generated method stub
		
	}
	
	// Utilities

}
