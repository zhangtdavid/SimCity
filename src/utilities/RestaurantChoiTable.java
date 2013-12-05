package utilities;

import city.animations.RestaurantChoiFurnitureAnimation;
import city.roles.interfaces.RestaurantChoiCustomer;


public class RestaurantChoiTable {

	RestaurantChoiCustomer occupiedBy;
	private int tableNumber = 0;
	private int xCoord;
	private int yCoord;
	//private int xstart = 50; // RestaurantChoiFurnitureAnimation.TABLEX_INIT
	//private int ystart = 250; // RestaurantChoiFurnitureAnimation.TABLEY;

	public RestaurantChoiTable(int tableNumber) {
		this.tableNumber = tableNumber;
		xCoord = RestaurantChoiFurnitureAnimation.TABLEX_INIT - RestaurantChoiFurnitureAnimation.TABLEX_INCR*2*tableNumber;
		yCoord = RestaurantChoiFurnitureAnimation.TABLEY;
	}

	public int getTableNumber(){
		return tableNumber;
	}

	void setLocation(int in1, int in2) {
		xCoord = in1;
		yCoord = in2;
	}

	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	public void setOccupant(RestaurantChoiCustomer c) {
		occupiedBy = c;
	}

	RestaurantChoiCustomer getOccupant() {
		return occupiedBy;
	}

	public boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}

}
