package city.tests.buildings.mocks;

import java.beans.PropertyChangeSupport;

import city.buildings.interfaces.House;
import city.roles.interfaces.Resident;
import city.tests.bases.mocks.MockResidenceBuilding;

public class MockHouse extends MockResidenceBuilding implements House {

	public MockHouse(String name) {
		super(name);
	}
	
	@Override
	public boolean getIsFull() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void addResident(Resident r) {
		if (!residents.contains(r)) {
			if (residents.isEmpty()) {
				this.residents.add(r);
				super.addResident(r);
			} else {
				throw new IllegalStateException("Only one person at a time may live in a house.");
			}
		}
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
