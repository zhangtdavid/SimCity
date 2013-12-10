package city.tests.buildings.mocks;

import java.beans.PropertyChangeSupport;

import city.buildings.AptBuilding;
import city.buildings.interfaces.Apt;
import city.roles.interfaces.Resident;
import city.tests.bases.mocks.MockResidenceBuilding;

public class MockApartment extends MockResidenceBuilding implements Apt {

	public MockApartment(String name) {
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
			if (residents.size() < AptBuilding.NUMBER_OF_BEDS) {
				this.residents.add(r);
				super.addResident(r);
			} else {
				throw new IllegalStateException("Only five people at a time may live in an apartment.");
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
