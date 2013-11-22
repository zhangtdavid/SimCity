package utilities;

import city.roles.RestaurantJPCustomerRole;

public class RestaurantJPTableClass {
	public RestaurantJPCustomerRole occupiedBy;
	public int tableNumber;

	public RestaurantJPTableClass(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	public void setOccupant(RestaurantJPCustomerRole cust) {
		occupiedBy = cust;
	}

	public void setUnoccupied() {
		occupiedBy = null;
	}

	RestaurantJPCustomerRole getOccupant() {
		return occupiedBy;
	}

	public boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}
}