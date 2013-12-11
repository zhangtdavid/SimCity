package city.bases;

import java.beans.PropertyChangeSupport;

import city.agents.interfaces.Person;
import city.bases.interfaces.AnimationInterface;
import city.bases.interfaces.RoleInterface;

/**
 * The base class for all SimCity201 roles.
 * 
 * Roles are like unthreaded agents, having messages, actions, and a scheduler.
 */
public abstract class Role implements RoleInterface {
	
	// Data
	
	private Person person; // The person who owns the role
	private boolean active;
	private boolean activity;
	private AnimationInterface animation;
	private PropertyChangeSupport propertyChangeSupport;
	
	// Constructor
	
	public Role() {
		propertyChangeSupport = new PropertyChangeSupport(this);
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
    	return propertyChangeSupport;
    }
    
	@Override
	public String getStateString() {
		return "Not Implemented";
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
		getPropertyChangeSupport().firePropertyChange(ACTIVE, this.active, true);
		this.active = true;
		setActivityBegun();
	}
	
	@Override
	public void setInactive() {
		getPropertyChangeSupport().firePropertyChange(ACTIVE, this.active, false);
		this.active = false;
	}
	
	@Override
	public void setActivityBegun() {
		getPropertyChangeSupport().firePropertyChange(ACTIVITY, this.activity, true);
		this.activity = true;
	}
	
	@Override
	public void setActivityFinished() {
		getPropertyChangeSupport().firePropertyChange(ACTIVITY, this.activity, false);
		this.activity = false;
	}
	
	
	// Utilities
	
	/**
	 * When the person owning this role dies, this method will be called.
	 * You should override this method.
	 * (For now it will just throw an error, eventually I will declare it abstract after
	 * everyone has had time to modify their code.)
	 */
	@Override
	public void definitelyDead() {
		// throw new UnsupportedOperationException("HEY YOU! OVERRIDE THIS METHOD!");
		return;
	}
	
	protected void stateChanged() {
		activity = true;
		person.stateChanged();
	}
	
    public void print(String msg) {
        person.print(msg);
    }

    public void print(String msg, Throwable e) {
    	person.print(msg, e);
    }

}
