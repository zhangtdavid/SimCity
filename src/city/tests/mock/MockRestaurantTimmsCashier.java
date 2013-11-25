package city.tests.mock;

import utilities.LoggedEvent;
import city.MockRole;
import city.animations.RestaurantTimmsCashierAnimation;
import city.animations.interfaces.RestaurantTimmsAnimatedCashier;
import city.interfaces.RestaurantTimmsCashier;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsWaiter;

public class MockRestaurantTimmsCashier extends MockRole implements RestaurantTimmsCashier {
	
	public MockRestaurantTimmsCashier() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgComputeCheck(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, int money) {
		log.add(new LoggedEvent("Received msgComputeCheck from Waiter. Amount: " + money));

	}

	@Override
	public void msgMakePayment(RestaurantTimmsCustomer c, int money) {
		log.add(new LoggedEvent("Received msgMakePayment from Customer. Money: " + money));
		c.msgPaidCashier(money);
	}
	
/*	@Override
	public void msgPayMarket(Market m, int money) {
		log.add(new LoggedEvent("Received msgPayMarket from Market. Money: " + money));

	}*/

}
