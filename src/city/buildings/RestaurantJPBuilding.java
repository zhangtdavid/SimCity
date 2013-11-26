package city.buildings;

import city.Building;
import city.Role;
import city.roles.RestaurantJPCashierRole;
import city.roles.RestaurantJPCookRole;
import city.roles.RestaurantJPHostRole;

public class RestaurantJPBuilding extends Building {
	
	// Data
	
	public RestaurantJPCookRole cook;
	public RestaurantJPCashierRole cashier;
	public RestaurantJPHostRole host;
	public int seatedCustomers = 0;
	
	// Constructor
	
	public RestaurantJPBuilding(String name){
		super(name);
	}
	
	// Utilities
	
	@Override
	public void addRole(Role r) {
		// TODO
		return;
	}

}
