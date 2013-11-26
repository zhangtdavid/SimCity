package city.roles;

/**
 * Restaurant Waiter Agent
 */
//A Waiter tends to the host and customers' requests
public class RestaurantChungWaiterMessageCookRole extends RestaurantChungWaiterBaseRole {
	public RestaurantChungWaiterMessageCookRole() {
		super();
	}

	public void tellCookOrder(WCustomer customer, String choice, int table) {
		waiterGui.DoGoToCook();

		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		print("telling cook order " + choice + " for " + customer.c);
		cook.msgHereIsAnOrder(this, choice, table);
		customer.o.os = OrderStatus.Cooking;
		
		waiterGui.DoReturnToWaiterHome();
	}
}
