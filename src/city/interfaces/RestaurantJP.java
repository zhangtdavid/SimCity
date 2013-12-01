package city.interfaces;

import city.abstracts.RestaurantBuildingInterface;

public interface RestaurantJP extends RestaurantBuildingInterface {

	public static final int WORKER_SALARY = 200;

	public void setCashier(RestaurantJPCashier c);

}