package city.tests.mock;

import utilities.LoggedEvent;
import utilities.RestaurantZhangTable;
import city.MockRole;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangHost;
import city.interfaces.RestaurantZhangWaiter;

public class MockRestaurantZhangHost extends MockRole implements RestaurantZhangHost {
	
	public MockRestaurantZhangHost() {
		super();
	}

	@Override
	public void msgImEntering(RestaurantZhangCustomer cust) {
		log.add(new LoggedEvent(cust.getName() + " entered restaurant"));
		
	}

	@Override
	public void msgIWantFood(RestaurantZhangCustomer cust) {
		log.add(new LoggedEvent("Customer " + cust.getName() + " wants food"));
	}

	@Override
	public void msgImLeaving(RestaurantZhangCustomer cust) {
		log.add(new LoggedEvent("Customer " + cust.getName() + " is leaving"));
	}

	@Override
	public void msgTableOpen(RestaurantZhangTable t) {
		log.add(new LoggedEvent("Table " + t.tableNumber + " is empty"));
	}

	@Override
	public void msgWaiterRequestBreak(RestaurantZhangWaiter w) {
		log.add(new LoggedEvent("Waiter " + w.getName() + " requested break"));
	}

	@Override
	public void msgOffBreak(RestaurantZhangWaiter w) {
		log.add(new LoggedEvent("Waiter " + w.getName() + " is off break"));
	}

	@Override
	public void addWaiter(RestaurantZhangWaiter wa) {
		// TODO Auto-generated method stub
		
	}
}
