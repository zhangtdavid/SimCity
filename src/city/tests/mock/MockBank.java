package city.tests.mock;

import utilities.EventLog;
import base.Mock;
import city.interfaces.Bank;

public class MockBank extends Mock implements Bank {
	
	public EventLog log = new EventLog();

	public MockBank(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
