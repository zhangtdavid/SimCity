package utilities;

import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangWaiter;

public class RestaurantZhangCheck {
	public RestaurantZhangWaiter waiter;
	public RestaurantZhangCustomer cust;
	public int payment;
	public int price;
	public enum CheckStatus{created, atWaiter, atCustomer};
	public CheckStatus status = CheckStatus.created;
	
	public RestaurantZhangCheck(RestaurantZhangWaiter w, RestaurantZhangCustomer c, int p) {
		waiter = w;
		cust = c;
		price = p;
	}
	
	RestaurantZhangCheck(RestaurantZhangCheck c) {
		waiter = c.waiter;
		cust = c.cust;
		payment = c.payment;
		price = c.price;
		status = c.status;
	}
}
