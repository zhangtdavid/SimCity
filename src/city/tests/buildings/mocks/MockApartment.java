package city.tests.buildings.mocks;

import city.Application.FOOD_ITEMS;
import city.agents.interfaces.Person;
import city.buildings.interfaces.Apt;
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
	public void removeFood(Person p, FOOD_ITEMS f, int i) {
		// TODO Auto-generated method stub
		
	}

}
