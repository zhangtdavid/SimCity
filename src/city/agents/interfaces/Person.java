package city.agents.interfaces;

import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.List;

import city.Application;
import city.animations.interfaces.AnimatedPerson;
import city.bases.interfaces.AgentInterface;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.JobRoleInterface;
import city.bases.interfaces.ResidenceBuildingInterface;
import city.bases.interfaces.RoleInterface;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.BusPassenger;
import city.roles.interfaces.CarPassenger;
import city.roles.interfaces.MarketCustomer;
import city.roles.interfaces.Resident;

public interface Person extends AgentInterface {

	// Data
	
	public static enum STATES {none, goingToWork, goingToBank, goingToPayRent, goingToRestaurant, goingToMarket, goingToCook, goingToSleep, atWork, atBank, atRentPayment, atRestaurant, atMarket, atCooking, atSleep, leavingWork };
	public static final String STATE = "state";
	public static final String NAME = "name";
	public static final String CASH = "cash";
	public static final String ROLES = "roles";
	public static final String CAR = "car";
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
	public JobRoleInterface getOccupation();
	public Resident getResidentRole();
	public RoleInterface getRestaurantCustomerRole();
	public List<RoleInterface> getRoles();
	public STATES getState();
	public PropertyChangeSupport getPropertyChangeSupport();
	public int getRoomNumber();
	public AnimatedPerson getAnimation();
	public BuildingInterface getCurrentLocation();
	
	// Setters
	
	public void setCar(Car c);
	public void setCash(int c);
	public void setDate(Date d);
	public void setHome(ResidenceBuildingInterface h);
	public void setName(String n);
	public void setRoomNumber(int i);
	public void setAnimation(AnimatedPerson a);
	public void setOccupation(JobRoleInterface r);
	public void setResidentRole(Resident r);
	public void setCurrentLocation(BuildingInterface b);
	
	// Utilities
	
	public void addRole(RoleInterface r);
	// public void forceSleep();
	public void terminateWithExtremePrejudice();
	public void printViaRole(String role, String msg);

	
}
