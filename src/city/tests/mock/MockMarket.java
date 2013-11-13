package city.tests.mock;

import utilities.EventLog;
import city.Mock;
import city.interfaces.Market;

public class MockMarket extends Mock implements Market {
	
	public EventLog log = new EventLog();

	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	

}
