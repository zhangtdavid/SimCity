package city.interfaces;

public interface RoleInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	public boolean runScheduler();
	
	// Actions
	
	// Getters
	
	public Person getPerson();
	public boolean getActive();
	public boolean getActivity();
	
	// Setters
	
	public void setPerson(Person p);
	public void setActive();
	public void setInactive();
	public void setActivityBegun();
	public void setActivityFinished();
	
	// Utilities

}
