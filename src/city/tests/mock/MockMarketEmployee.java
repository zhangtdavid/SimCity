package city.tests.mock;

import utilities.EventLog;
import base.Mock;
import city.interfaces.MarketEmployee;

public class MockMarketEmployee extends Mock implements MarketEmployee {
	
	public EventLog log = new EventLog();

	public MockMarketEmployee(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
