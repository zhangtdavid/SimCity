package city.buildings;

import city.Role;
import city.roles.RestaurantJPCashierRole;
import city.roles.RestaurantJPCookRole;
import city.roles.RestaurantJPHostRole;

public class RestaurantJPBuilding extends RestaurantBaseBuilding {
	
	// Data
	
	public RestaurantJPCookRole cook;
	public RestaurantJPCashierRole cashier;
	public RestaurantJPHostRole host;
	public int seatedCustomers = 0;
	public int funds = 2000;
	public static final int WORKER_SALARY = 200;
	
	
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
