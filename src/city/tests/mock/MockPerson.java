package city.tests.mock;

import java.util.ArrayList;
import java.util.Date;

import city.RoleInterface;
import city.abstracts.MockAgent;
import city.abstracts.ResidenceBuildingInterface;
import city.interfaces.Car;
import city.interfaces.Person;
import city.roles.BusPassengerRole;
import city.roles.CarPassengerRole;
import city.roles.MarketCustomerRole;

public class MockPerson extends MockAgent implements Person {
	
	// Data
	
	private String name;
	public ResidenceBuildingInterface home;
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
	public ResidenceBuildingInterface getHome() {
		return home;
	}
	
	@Override
	public RoleInterface getOccupation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ArrayList<RoleInterface> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Car getCar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CarPassengerRole getCarPassengerRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BusPassengerRole getBusPassengerRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketCustomerRole getMarketCustomerRole() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public RoleInterface getRestaurantCustomerRole() {
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
	public void setOccupation(RoleInterface r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCash(int c) {
		cash = c;
	}
	
	@Override
	public void setHome(ResidenceBuildingInterface h) {
		home = h;		
	}
	
	// Utilities

	@Override
	public void addRole(RoleInterface r) {
		// TODO Auto-generated method stub
		
	}

}
