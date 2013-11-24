package city.buildings;

import city.Building;
import city.roles.RestaurantJPCashierRole;
import city.roles.RestaurantJPCookRole;
import city.roles.RestaurantJPHostRole;

public class RestaurantJPBuilding extends Building{
	public RestaurantJPCookRole cook;
	public RestaurantJPCashierRole cashier;
	public RestaurantJPHostRole host;
	
	public RestaurantJPBuilding(String name){
		super(name);
	}

}
