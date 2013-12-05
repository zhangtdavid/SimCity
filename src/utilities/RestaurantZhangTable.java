package utilities;

import city.roles.interfaces.RestaurantZhangCustomer;

public class RestaurantZhangTable {
	RestaurantZhangCustomer occupiedBy;
	public int tableNumber;
	final int tableX;
	final int tableY;
	final int tableW;
	final int tableH;
	static final int DEFAULTTABLEX = ((int)Math.random())%200;
	static final int DEFAUTLTABLEY = ((int)Math.random())%200;
	static final int DEFAULTTABLEW = 40;
	static final int DEFAULTTABLEH = 40;
	
	// Constructors
	public RestaurantZhangTable(int number) {
		tableNumber = number;
		tableX = DEFAULTTABLEX;
		tableY = DEFAUTLTABLEY;
		tableW = DEFAULTTABLEW;
		tableH = DEFAULTTABLEH;
	}
	
	public RestaurantZhangTable(int number, int x, int y, int w, int h) {
		tableNumber = number;
		tableX = x;
		tableY = y;
		tableW = w;
		tableH = h;
	}

	// Handle Customer
	public void setOccupant(RestaurantZhangCustomer c) {
		occupiedBy = c;
	}

	public void setUnoccupied() {
		occupiedBy = null;
	}

	RestaurantZhangCustomer getOccupant() {
		return occupiedBy;
	}

	public boolean isOccupied() {
		return occupiedBy != null;
	}
	
	// Utilities
	public String toString() {
		return "table " + tableNumber;
	}
	
	public int getX() {
		return tableX;
	}
	
	public int getY() {
		return tableY;
	}
	
	public int getW() {
		return tableW;
	}
	
	public int getH() {
		return tableH;
	}
}