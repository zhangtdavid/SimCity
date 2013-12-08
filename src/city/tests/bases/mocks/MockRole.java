package city.tests.bases.mocks;

import java.beans.PropertyChangeSupport;

import utilities.EventLog;
import city.agents.interfaces.Person;
import city.bases.interfaces.AnimationInterface;
import city.bases.interfaces.RoleInterface;

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
	private boolean active;
	private boolean activity;
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
	
	@Override
	public void definitelyDead() {
		throw new UnsupportedOperationException("HEY YOU! OVERRIDE THIS METHOD!");
	}

}
