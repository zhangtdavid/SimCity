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
	public static int getWorkerSalary() {
		return WORKER_SALARY;
	}
	private static final int WORKER_SALARY = 500; 
	// this high value helps accelerate normative testing. Also everyone makes the same amount!
}
