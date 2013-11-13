package city.tests.mock;

import utilities.EventLog;
import base.Mock;
import city.interfaces.MarketCustomerDelivery;

public class MockMarketCustomerDelivery extends Mock implements MarketCustomerDelivery {
	
	public EventLog log = new EventLog();

	public MockMarketCustomerDelivery(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	

}
