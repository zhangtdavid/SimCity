package city;

import city.interfaces.Person;
import city.interfaces.RoleInterface;

/**
 * The base class for all SimCity201 roles.
 * 
 * Roles are like unthreaded agents, having messages, actions, and a scheduler.
 */
public abstract class Role implements RoleInterface {
	
	// Data
	
	private Person person; // The person who owns the role
	private int salary; // FOR OCCUPATIONS - how much the role is paid by its job
	private int shiftStart; // FOR OCCUPATIONS - when the role starts work
	private int shiftEnd; // FOR OCCUPATIONS - when the role can leave work
	private boolean active;
	private boolean activity;
	private Building workplace; // FOR OCCUPATIONS - the building where the role works. used for transportation and banking.
	private Animation animation;
	
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
	public  <T extends Animation> T getAnimation(Class<T> type) {
		return type.cast(animation);
	}
	
	@Override
	public int getSalary() {
		return salary;
	}
	
	@Override
	public  <T extends Building> T getWorkplace(Class<T> type) {
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
	
	// Setters
	
	@Override
	public void setPerson(Person p) {
		this.person = p;
	}
	
	@Override
	public void setAnimation(Animation a) {
		this.animation = a;
	}
	
	@Override
	public void setSalary(int s) {
		this.salary = s;
	}
	
	@Override
	public void setWorkplace(Building b) {
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
		activity = true;
		person.stateChanged();
	}
	
    protected void print(String msg) {
        person.print(msg);
    }

    protected void print(String msg, Throwable e) {
        person.print(msg, e);
    }

}
