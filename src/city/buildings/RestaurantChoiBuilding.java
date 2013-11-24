package city.buildings;

import city.Building;
import city.roles.RestaurantChoiCashierRole;
import city.roles.RestaurantChoiCookRole;
import city.roles.RestaurantChoiHostRole;

public class RestaurantChoiBuilding extends Building{
	public RestaurantChoiCookRole cook;
	public RestaurantChoiCashierRole cashier;
	public RestaurantChoiHostRole host;
	public int seatedCustomers = 0;
	public RestaurantChoiBuilding(String name){
		super(name);
	}
}
