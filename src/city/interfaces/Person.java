package city.interfaces;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;

import city.AgentInterface;
import city.Application;
import city.RoleInterface;
import city.abstracts.ResidenceBuildingInterface;
import city.animations.interfaces.AnimatedPerson;
import city.animations.interfaces.AnimatedPersonAtHome;

public interface Person extends AgentInterface {

	// Data
	
	public static enum STATES {none, goingToWork, goingToBank, goingToPayRent, goingToRestaurant, goingToMarket, goingToCook, goingToSleep, atWork, atBank, atRentPayment, atRestaurant, atMarket, atCooking, atSleep, leavingWork };
	public static final String STATE = "state";
	public static final String NAME = "name";
	public static final String CASH = "cash";
	public static final String ROLES = "roles";
	public static final int BANK_DEPOSIT_THRESHOLD = 100;
	public static final int BANK_DEPOSIT_SUM = 50;
	public static final int RESTAURANT_DINING_THRESHOLD = 80;
	public static long RESTAURANT_DINING_INTERVAL = (Application.HALF_HOUR * 144); // 3 days
	public static long WAKE_UP_THRESHOLD = (Application.HALF_HOUR * 24); // 12 hours
	public static final int RENT_MIN_THRESHOLD = 200;
	public static final int INSIGNIFICANT_CASH = 10; // For tests. Use this to give an amount of cash to a person that won't trigger any special (banking, restaurant) behavior.
	
	// Constructor
	
	// Messages
	
	public void guiAtDestination();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public AnimatedPerson getAnimation();
	public BankCustomer getBankCustomerRole();
	public BusPassenger getBusPassengerRole();
	public Car getCar();
	public CarPassenger getCarPassengerRole();
	public int getCash();
	public Date getDate();
	public boolean getHasEaten();
	public ResidenceBuildingInterface getHome();
	public MarketCustomer getMarketCustomerRole();
	public String getName();
	public RoleInterface getOccupation();
	public Resident getResidentRole();
	public RoleInterface getRestaurantCustomerRole();
	public ArrayList<RoleInterface> getRoles();
	public STATES getState();
	public PropertyChangeSupport getPropertyChangeSupport();
	public int getRoomNumber();
	public AnimatedPersonAtHome getAnimationAtHome();
	public void setOccupation(RoleInterface r);
	
	// Setters
	
	public void setAnimation(AnimatedPerson p);
	public void setCar(Car c);
	public void setCash(int c);
	public void setDate(Date d);
	public void setHome(ResidenceBuildingInterface h);
	public void setName(String n);
	public void setRoomNumber(int i);
	public void setHomeAnimation(AnimatedPersonAtHome anim);
	
	// Utilities
	
	public void addRole(RoleInterface r);
	public void acquireSemaphoreFromAnimation();
	public void releaseSemaphoreFromAnimation();
	
	// Classes
	
}
