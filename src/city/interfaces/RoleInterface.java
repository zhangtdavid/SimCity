package city.interfaces;

import city.Animation;
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
	public <T extends AnimationInterface> T getAnimation(Class<T> type);
	public int getSalary();
	public <T extends Building> T getWorkplace(Class<T> type);
	public int getShiftStart();
	public int getShiftEnd();
	public boolean getActive();
	public boolean getActivity();
	
	// Setters
	
	public void setPerson(Person p);
	public void setAnimation(AnimationInterface a);
	public void setSalary(int s);
	public void setWorkplace(Building b);
	public void setShift(int shiftStart, int shiftEnd);
	public void setActive();
	public void setInactive();
	public void setActivityBegun();
	public void setActivityFinished();
	
	// Utilities

}
