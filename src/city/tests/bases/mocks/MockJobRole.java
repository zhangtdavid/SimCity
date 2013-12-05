package city.tests.bases.mocks;

import utilities.EventLog;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.JobRoleInterface;

public abstract class MockJobRole extends MockRole implements JobRoleInterface {

	// Data
	
	private int salary; // FOR OCCUPATIONS - how much the role is paid by its job
	private int shiftStart; // FOR OCCUPATIONS - when the role starts work
	private int shiftEnd; // FOR OCCUPATIONS - when the role can leave work
	private BuildingInterface workplace; // FOR OCCUPATIONS - the building where the role works. used for transportation and banking.
	
	public static final int SALARY = 500;
	
	public EventLog log = new EventLog();
	
	// Constructor
	
	public MockJobRole(BuildingInterface b, int shiftStart, int shiftEnd) {
		super();
		this.setWorkplace(b);
		this.setShift(shiftStart, shiftEnd);
		this.setSalary(SALARY);
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
	
}
