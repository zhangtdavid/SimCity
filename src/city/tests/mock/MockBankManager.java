package city.tests.mock;

import utilities.EventLog;
import city.Mock;
import city.interfaces.BankManager;

public class MockBankManager extends Mock implements BankManager {
	
	public EventLog log = new EventLog();

	public MockBankManager(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
