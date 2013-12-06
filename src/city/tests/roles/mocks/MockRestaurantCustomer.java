package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import city.tests.bases.mocks.MockRole;

/**
 * Though there are 5 different restaurants, this mock is just for the use of PersonTest.
 */
public class MockRestaurantCustomer extends MockRole {
	
	@Override
	public boolean runScheduler() {
		// Have the role be done at the restaurant as soon as it arrives
		super.setInactive();
		// Not sure what the behavior of actual restaurants will be. Just have this one always want to continue.
		return true;
	}
	
	@Override
	public void setActive() {
		super.setActive();
	}
	
	@Override
	public void setInactive() {
		// Restaurant customers must set themselves inactive
		return;
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
