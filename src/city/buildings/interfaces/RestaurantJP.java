package city.buildings.interfaces;

import city.bases.interfaces.RestaurantBuildingInterface;
import city.roles.interfaces.RestaurantJPCashier;

public interface RestaurantJP extends RestaurantBuildingInterface {

	public static final int WORKER_SALARY = 200;

	public void setCashier(RestaurantJPCashier c);

}