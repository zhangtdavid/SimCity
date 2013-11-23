package city.interfaces;

import utilities.RestaurantZhangCheck;

public interface RestaurantZhangCashier extends RoleInterface {
	// Sent from waiter to create check to be given to the customer by the waiter
		public abstract void msgComputeBill(RestaurantZhangWaiter waiter, RestaurantZhangCustomer customer, String choice);
		// Sent from customer to play for check
		public abstract void msgHereIsPayment(RestaurantZhangCheck c, double cash);
}
