package city.tests.mock;

import utilities.EventLog;
import city.Mock;
import city.interfaces.MarketDeliveryPerson;

public class MockMarketDeliveryPerson extends Mock implements MarketDeliveryPerson {
	
	public EventLog log = new EventLog();

	public MockMarketDeliveryPerson(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	

}
