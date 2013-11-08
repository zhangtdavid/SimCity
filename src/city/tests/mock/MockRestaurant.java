package city.tests.mock;

import utilities.EventLog;
import base.Mock;
import city.interfaces.Restaurant;

public class MockRestaurant extends Mock implements Restaurant {
	
	public EventLog log = new EventLog();
	
	public MockRestaurant(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
