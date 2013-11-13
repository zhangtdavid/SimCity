package city.tests.mock;

import utilities.EventLog;
import city.Mock;
import city.interfaces.MarketCashier;

public class MockMarketCashier extends Mock implements MarketCashier {
	
	public EventLog log = new EventLog();

	public MockMarketCashier(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	

}
