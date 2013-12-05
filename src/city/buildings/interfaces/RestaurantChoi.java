package city.buildings.interfaces;

import city.bases.interfaces.RestaurantBuildingInterface;

public interface RestaurantChoi extends RestaurantBuildingInterface {

	public static final int DAILY_CAPITAL = 1000;
	public static final int DEPOSIT_THRESHOLD = 1005; // low enough so that I can see depositing behavior
	public static final int WITHDRAW_THRESHOLD = 200;

}