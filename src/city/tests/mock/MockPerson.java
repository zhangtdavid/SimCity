package city.tests.mock;

import java.util.Date;

import city.Building;
import city.MockAgent;
import city.Role;
import city.interfaces.Car;
import city.interfaces.Person;

public class MockPerson extends MockAgent implements Person {
	
	// Data
	
	private String name;
	
	// Constructor
	
	public MockPerson(String name) {
		this.name = name;
	}
	
	// Messages
	
	@Override
	public void guiAtDestination() {
		// TODO Auto-generated method stub
		
	}
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	@Override
	public String getName() {
		return name;
	}
	
    @Override
    public Date getDate() { // TODO
    	return new Date(0);
    }
    
	@Override
	public int getSalary() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	// Setters
	
	@Override
	public void setAnimation(city.animations.interfaces.AnimatedPerson p) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setCar(Car c) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setDate(Date d) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setOccupation(Role r) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setWorkplace(Building b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCash(int c) {
		// TODO Auto-generated method stub
		
	}
	
	// Utilities

	@Override
	public void addRole(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCash() {
		// TODO Auto-generated method stub
		return 0;
	}

}
