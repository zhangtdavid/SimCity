package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.Building;
import city.Role;
import city.interfaces.RestaurantTimmsCashier;
import city.interfaces.RestaurantTimmsCook;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsHost;
import city.interfaces.RestaurantTimmsWaiter;

public class RestaurantTimmsBuilding extends Building {
	
	// Data
	
	public RestaurantTimmsCashier cashier;
	public RestaurantTimmsCook cook;
	public RestaurantTimmsHost host;
	
	public List<RestaurantTimmsCustomer> restaurantCustomers = Collections.synchronizedList(new ArrayList<RestaurantTimmsCustomer>());
	public List<RestaurantTimmsWaiter> restaurantWaiters = Collections.synchronizedList(new ArrayList<RestaurantTimmsWaiter>());
	public List<RestaurantTimmsBuilding.Table> restaurantTables = Collections.synchronizedList(new ArrayList<RestaurantTimmsBuilding.Table>());
	
	private static final int START_CASH_MIN = 1000;
	private static final int START_CASH_MAX = 5000;
	
	public static final int WORKER_SALARY = 200;

	// Constructor
	
	public RestaurantTimmsBuilding(String name) {
		super(name);
		this.setCash((START_CASH_MIN + (int)(Math.random() * ((START_CASH_MAX - START_CASH_MIN) + 1))));
		this.setCustomerRoleName("city.roles.RestaurantTimmsCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantTimmsCustomerAnimation");
		
		int i = 0;
		while (i < 9) {
			restaurantTables.add(new Table(i));
			i++;
		}
	}
	
	// Utilities
	
	@Override
	public void addRole(Role r) {
		// TODO
		return;
	}
	
	// Classes

	public class Table {
		private boolean occupied;
		private int number;

		public Table(int i) {
			this.occupied = false;
			this.number = i;
		}
		
		public int getNumber() {
			return number;
		}
		
		public boolean getOccupied() {
			return occupied;
		}

		public void setOccupied() {
			this.occupied = true;
		}

		public void setUnoccupied() {
			this.occupied = false;
		}
	}

}
