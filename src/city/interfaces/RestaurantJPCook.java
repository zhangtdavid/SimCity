package city.interfaces;

import utilities.RestaurantJPTableClass;

public interface RestaurantJPCook extends RoleInterface {
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the waiter prompting the customer to give cashier check after the customer is done eating.
	 */
	public void msgHereIsAnOrder(RestaurantJPWaiter wait, String c, RestaurantJPTableClass t);
	
	public void msgOrderCannotBeFulfilled(String food, MarketManager m);
	
	public void msgShipmentReady(String f);
	
	public void msgMarketDry(MarketManager m);
	
	public void msgFoodRetrieved(String f);
	
}