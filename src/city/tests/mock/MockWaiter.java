package city.tests.mock;

import utilities.EventLog;
import city.Mock;
import city.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {

	public EventLog log = new EventLog();
	
	public MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
