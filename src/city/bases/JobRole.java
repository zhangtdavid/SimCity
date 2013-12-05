package city.bases;

import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.JobRoleInterface;

public abstract class JobRole extends Role implements JobRoleInterface {
	
	// Data
	
	private BuildingInterface workplace; // FOR OCCUPATIONS - the building where the role works. used for transportation and banking.
	private int salary; // FOR OCCUPATIONS - how much the role is paid by its job
	private int shiftStart; // FOR OCCUPATIONS - when the role starts work
	private int shiftEnd; // FOR OCCUPATIONS - when the role can leave work
	
	// Constructor
	
	public JobRole() {
		super();
		salary = -1;
		shiftStart = -1;
		shiftEnd = -1;
	}
	
	// Getters
	
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
	
	// Setters
	
	@Override
	public void setSalary(int s) {
		getPropertyChangeSupport().firePropertyChange(SALARY, this.salary, s);
		this.salary = s;
	}
	
	@Override
	public void setWorkplace(BuildingInterface b) {
		this.workplace = b;
	}
	
	@Override
	public void setShift(int shiftStart, int shiftEnd) {
		getPropertyChangeSupport().firePropertyChange(SHIFT_START, this.shiftStart, shiftStart);
		getPropertyChangeSupport().firePropertyChange(SHIFT_END, this.shiftEnd, shiftEnd);
		this.shiftStart = shiftStart;
		this.shiftEnd = shiftEnd;
	}
	
	// Utilities

}
