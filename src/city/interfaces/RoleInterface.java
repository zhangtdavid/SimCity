package city.interfaces;

import city.Building;

public interface RoleInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	public boolean runScheduler();
	
	// Actions
	
	// Getters
	
	public Person getPerson();
	public int getSalary();
	public Building getBusiness();
	public int getShiftStart();
	public int getShiftEnd();
	public boolean getActive();
	public boolean getActivity();
	
	// Setters
	
	public void setPerson(Person p);
	public void setSalary(int s);
	public void setBusiness(Building b);
	public void setShift(int shiftStart, int shiftEnd);
	public void setActive();
	public void setInactive();
	public void setActivityBegun();
	public void setActivityFinished();
	
	// Utilities

}
