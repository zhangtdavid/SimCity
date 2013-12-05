package city.bases.interfaces;


public interface JobRoleInterface extends RoleInterface {
	
	// Data
	
	// Getters
	
	public int getSalary();
	public <T extends BuildingInterface> T getWorkplace(Class<T> type);
	public int getShiftStart();
	public int getShiftEnd();
	
	// Setters
	
	public void setSalary(int s);
	public void setWorkplace(BuildingInterface b);
	public void setShift(int shiftStart, int shiftEnd);
	
	// Utilities

}
