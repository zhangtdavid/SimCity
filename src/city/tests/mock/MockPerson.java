package city.tests.mock;

import java.util.Date;

import utilities.EventLog;
import city.Mock;
import city.interfaces.Person;

public class MockPerson extends Mock implements Person {
	
	public EventLog log = new EventLog();

	public MockPerson(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setDate(Date d) {
		// TODO Auto-generated method stub
		
	}

}
