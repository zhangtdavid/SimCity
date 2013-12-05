package city.abstracts;

import java.beans.PropertyChangeSupport;

import utilities.EventLog;
import city.AnimationInterface;
import city.BuildingInterface;
import city.RoleInterface;
import city.interfaces.Person;

/**
 * The base class for all SimCity201 role mocks.
 * 
 * This class implements most things required by RoleInterface so that
 * mocks themselves may focus only on their particular behaviors.
 * 
 */
public abstract class MockRole implements RoleInterface {
	
	// Data

	private Person person; // The person who owns the role
	private int salary; // FOR OCCUPATIONS - how much the role is paid by its job
	private int shiftStart; // FOR OCCUPATIONS - when the role starts work
	private int shiftEnd; // FOR OCCUPATIONS - when the role can leave work
	private boolean active;
	private boolean activity;
	private BuildingInterface workplace; // FOR OCCUPATIONS - the building where the role works. used for transportation and banking.
	private AnimationInterface animation;
	
	public EventLog log = new EventLog();
	
	// Constructor
	
	public MockRole() {
		active = false;
		activity = false;
	}
	
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
	public  <T extends AnimationInterface> T getAnimation(Class<T> type) {
		return type.cast(animation);
	}
	
	@Override
	public int getSalary() {
		return salary;
	}
	
	@Override
	public  <T extends BuildingInterface> T getWorkplace(Class<T> type) {
		return type.cast(workplace);
	}
	
	@Override
	public int getShiftStart() {
		return shiftStart;
	}
	
	@Override
	public int getShiftEnd() {
		return shiftEnd;
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
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}
    
	// Setters
	
	@Override
	public void setPerson(Person p) {
		this.person = p;
	}
	
	@Override
	public void setAnimation(AnimationInterface a) {
		this.animation = a;
	}
	
	@Override
	public void setSalary(int s) {
		this.salary = s;
	}
	
	@Override
	public void setWorkplace(BuildingInterface b) {
		this.workplace = b;
	}
	
	@Override
	public void setShift(int shiftStart, int shiftEnd) {
		this.shiftStart = shiftStart;
		this.shiftEnd = shiftEnd;
	}
	
	@Override
	public void setActive() {
		this.active = true;
		setActivityBegun();
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

}
