package city.tests.mock;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantFood(RestaurantZhangCustomer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImLeaving(RestaurantZhangCustomer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTableOpen(RestaurantZhangTable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWaiterRequestBreak(RestaurantZhangWaiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOffBreak(RestaurantZhangWaiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWaiter(RestaurantZhangWaiter wa) {
		// TODO Auto-generated method stub
		
	}
}
