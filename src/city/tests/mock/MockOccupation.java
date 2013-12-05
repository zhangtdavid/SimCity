package city.tests.mock;

import java.beans.PropertyChangeSupport;

import city.BuildingInterface;
import city.abstracts.MockRole;

public class MockOccupation extends MockRole {
	
	// Data
	
	public static final int SALARY = 500;
	
	// Constructor
	
	public MockOccupation(BuildingInterface b, int shiftStart, int shiftEnd) {
		super();
		this.setWorkplace(b);
		this.setShift(shiftStart, shiftEnd);
		this.setSalary(SALARY);
	}
	
	// Setters
	
	@Override
	public void setActive() {
		super.setActive();
	}
	
	@Override
	public void setInactive() {
		super.setInactive();
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

}
