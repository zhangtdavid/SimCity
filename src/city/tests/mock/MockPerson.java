package city.tests.mock;

import utilities.EventLog;
import base.Mock;
import city.interfaces.Person;

public class MockPerson extends Mock implements Person {
	
	public EventLog log = new EventLog();

	public MockPerson(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
