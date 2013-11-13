package city.tests.mock;

import utilities.EventLog;
import city.Mock;
import city.interfaces.BankTeller;

public class MockBankTeller extends Mock implements BankTeller {
	
	public EventLog log = new EventLog();

	public MockBankTeller(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
