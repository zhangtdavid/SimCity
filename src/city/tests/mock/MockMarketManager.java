package city.tests.mock;

import utilities.EventLog;
import base.Mock;
import city.interfaces.MarketManager;

public class MockMarketManager extends Mock implements MarketManager {
	
	public EventLog log = new EventLog();

	public MockMarketManager(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
}
