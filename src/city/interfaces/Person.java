package city.interfaces;

import java.util.Date;

import city.Application;
import city.Building;
import city.Role;
import city.buildings.HouseBuilding;

public interface Person extends AgentInterface {

	// Data
	
	enum State {none, goingToWork, goingToBank, goingToPayRent, goingToRestaurant, goingToMarket, goingToCook, goingToSleep, atWork, atBank, atRentPayment, atRestaurant, atMarket, atCooking, atSleep, leavingWork };
	static final int BANK_DEPOSIT_THRESHOLD = 100;
	static final int BANK_DEPOSIT_SUM = 50;
	static final int RESTAURANT_DINING_THRESHOLD = 80;
	static long RESTAURANT_DINING_INTERVAL = (Application.INTERVAL * 144); // 3 days
	static long WAKE_UP_THRESHOLD = (Application.INTERVAL * 24); // 12 hours
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
	public HouseBuilding getHome();
	
	// Setters
	
	public void setAnimation(city.animations.interfaces.AnimatedPerson p);
	public void setCar(Car c);
	public void setDate(Date d);
	public void setOccupation(Role r);
	public void setWorkplace(Building b);
	public void setCash(int c);
	public void setHome(HouseBuilding h);
	
	// Utilities
	
	public void addRole(Role r);
	
	// Classes
	
}
