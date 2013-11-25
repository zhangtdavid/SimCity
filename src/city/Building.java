package city;

import java.util.Map;

import city.Application.FOOD_ITEMS;

/**
 * The base class for all SimCity201 Buildings.
 * 
 * Buildings are coded for restaurants, banks, markets, homes, bus stops.
 */
public abstract class Building {
	
	// Data
	
	private String name; // Name of the building (e.g. "Market" or "RestaurantJP" or "House 1")
	private String customerRole; // The class name of the role that interacts with this building as a customer
	private String customerAnimation; // The class name of the animation that interacts with this building as a customer
	private String residentRole;
	private String residentAnimation;
	private int cash; // Cash that the building has. Used by restaurants, etc., not houses, bus stops, etc.

	// Constructor

	public Building(String name) {
		this.name = name;
		cash = 0;
	}
    
    // Messages
    
    // Scheduler
	
	// Actions
	
	// Getters
	
	public String getName() {
		return name;
	}

	public int getCash(){
		return cash;
	}
	
	public String getCustomerRole() {
		return customerRole;
	}
	
	public String getCustomerAnimation() {
		return customerAnimation;
	}
	
	// Setters
	
	public void setCash(int c){
		this.cash = c;
	}
	
	public void setCustomerRole(String c) {
		this.customerRole = c;
	}
	
	public void setCustomerAnimation(String c) {
		this.customerAnimation = c;
	}
	public void setResidentRole(String r){
		this.residentRole = r;
	}
	public void setResidentAnimation(String r){
		this.residentAnimation = r;
	}
	
	// Utilities 

}
