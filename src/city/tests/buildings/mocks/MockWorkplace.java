package city.tests.buildings.mocks;

import java.beans.PropertyChangeSupport;

import city.tests.bases.mocks.MockBuilding;

public class MockWorkplace extends MockBuilding {

	// Constructor
	
	public MockWorkplace(String name) {
		super(name);
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getBusinessIsOpen() {
		// TODO Auto-generated method stub
		return false;
	}

}
