package city.tests.mock;

import utilities.EventLog;
import city.Mock;
import city.interfaces.MarketCustomer;

public class MockMarketCustomer extends Mock implements MarketCustomer {
	
	public EventLog log = new EventLog();

	public MockMarketCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	

}
