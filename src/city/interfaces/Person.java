package city.interfaces;

import java.util.Date;

import city.Building;
import city.Role;

public interface Person extends AgentInterface {

	// Data
	
	enum State {none, goingToWork, goingToBank, goingToPayRent, goingToRestaurant, goingToMarket, goingHome, atWork, atBank, atRentPayment, atRestaurant, atMarket, atHome, leavingWork };
	static final int BANK_DEPOSIT_THRESHOLD = 100;
	static final int RESTAURANT_DINING_THRESHOLD = 80;
	static final int RENT_MIN_THRESHOLD = 200;
	static final int RENT_MAX_THRESHOLD = 500;
	
	// Constructor
	
	// Messages
	
	public void guiAtDestination();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public String getName();
	public Date getDate();
	public int getSalary();
	public int getCash();
	
	// Setters
	
	public void setAnimation(city.animations.interfaces.AnimatedPerson p);
	public void setCar(Car c);
	public void setDate(Date d);
	public void setOccupation(Role r);
	public void setWorkplace(Building b);
	public void setCash(int c);
	
	// Utilities
	
	public void addRole(Role r);
	
	// Classes
	
}
