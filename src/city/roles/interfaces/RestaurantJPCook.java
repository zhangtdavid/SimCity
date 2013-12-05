package city.roles.interfaces;

import utilities.RestaurantJPTableClass;
import city.bases.interfaces.RoleInterface;

public interface RestaurantJPCook extends RoleInterface {
	
	// Messages
	
	public void msgHereIsAnOrder(RestaurantJPWaiter wait, String c, RestaurantJPTableClass t);
	public void msgOrderCannotBeFulfilled(String food, MarketManager m);
	public void msgShipmentReady(String f);
	public void msgMarketDry(MarketManager m);
	public void msgFoodRetrieved(String f);
	
}