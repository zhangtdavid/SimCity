package city.tests.mock;

import city.abstracts.MockResidenceBuilding;
import city.interfaces.Apartment;

public class MockApartment extends MockResidenceBuilding implements Apartment {

	public MockApartment(String name) {
		super(name);
	}

}
