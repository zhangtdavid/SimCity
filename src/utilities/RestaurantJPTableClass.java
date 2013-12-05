package utilities;

import city.roles.interfaces.RestaurantJPCustomer;

public class RestaurantJPTableClass {
	public RestaurantJPCustomer occupiedBy;
	public int tableNumber;

	public RestaurantJPTableClass(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	public void setOccupant(RestaurantJPCustomer customer) {
		occupiedBy = customer;
	}

	public void setUnoccupied() {
		occupiedBy = null;
	}

	RestaurantJPCustomer getOccupant() {
		return occupiedBy;
	}

	public boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}
}