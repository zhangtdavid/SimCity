package city.tests.mock;

import java.util.Date;

import city.Role;
import city.abstracts.MockAgent;
import city.abstracts.ResidenceBuildingBase;
import city.interfaces.Car;
import city.interfaces.Person;

public class MockPerson extends MockAgent implements Person {
	
	// Data
	
	private String name;
	public ResidenceBuildingBase home;
	private int cash;
	
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
	public int getCash() {
		return cash;
	}

	@Override
	public ResidenceBuildingBase getHome() {
		return home;
	}
	
	@Override
	public Role getOccupation() {
		// TODO Auto-generated method stub
		return null;
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
	public void setCash(int c) {
		cash = c;
	}
	
	@Override
	public void setHome(ResidenceBuildingBase h) {
		home = h;		
	}
	
	// Utilities

	@Override
	public void addRole(Role r) {
		// TODO Auto-generated method stub
		
	}

}
