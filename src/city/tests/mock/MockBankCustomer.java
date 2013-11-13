package city.tests.mock;

import utilities.EventLog;
import city.Mock;
import city.interfaces.BankCustomer;

public class MockBankCustomer extends Mock implements BankCustomer {
	
	public EventLog log = new EventLog();

	public MockBankCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
