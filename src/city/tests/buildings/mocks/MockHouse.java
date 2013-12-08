package city.tests.buildings.mocks;

import city.Application.FOOD_ITEMS;
import city.agents.interfaces.Person;
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
	public void removeFood(Person p, FOOD_ITEMS f, int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeResident(Resident r) {
		// TODO Auto-generated method stub
		
	}

}
