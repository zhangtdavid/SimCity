package city.bases.interfaces;

import java.beans.PropertyChangeSupport;

import city.agents.interfaces.Person;

public interface RoleInterface {
	
	// Data
	
	public static final String ACTIVE = "active";
	public static final String ACTIVITY = "activity";
	public static final String STATE_STRING = "state string";
	public static final String SALARY = "salary";
	public static final String SHIFT_START = "shift start";
	public static final String SHIFT_END = "shift end";
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	public boolean runScheduler();
	
	// Actions
	
	// Getters
	
	public Person getPerson();
	public <T extends AnimationInterface> T getAnimation(Class<T> type);
	public boolean getActive();
	public boolean getActivity();
	public PropertyChangeSupport getPropertyChangeSupport();
	public String getStateString();
	
	// Setters
	
	public void setPerson(Person p);
	public void setAnimation(AnimationInterface a);
	public void setActive();
	public void setInactive();
	public void setActivityBegun();
	public void setActivityFinished();
	
	// Utilities
	
	public void definitelyDead();

}
