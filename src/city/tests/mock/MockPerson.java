package city.tests.mock;

import java.util.Date;

import utilities.EventLog;
import city.Agent;
import city.Mock;
import city.Role;
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

	@Override
	public void guiAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOccupation(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRole(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAnimation(city.animations.interfaces.Person p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCar(Agent c) {
		// TODO Auto-generated method stub
		
	}

}
