package city.tests.mock;

import city.abstracts.MockResidenceBuilding;
import city.interfaces.House;

public class MockHouse extends MockResidenceBuilding implements House {

	public MockHouse(String name) {
		super(name);
	}

}
