package city.tests.mock;


import city.MockRole;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungHost;
import city.interfaces.RestaurantChungWaiter;
import utilities.EventLog;
import utilities.LoggedEvent;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockRestaurantChungHost extends MockRole implements RestaurantChungHost {
	public EventLog log = new EventLog();

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public RestaurantChungCashier cashier;

//	public MockRestaurantChungHost(String name) {
//		super(name);
//
//	}

	@Override
	public void msgIWantToEat(RestaurantChungCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDecidedToStay(RestaurantChungCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeaving(RestaurantChungCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTakingCustomerToTable(RestaurantChungCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWaiterAvailable(RestaurantChungWaiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantToGoOnBreak(RestaurantChungWaiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIAmReturningToWork(RestaurantChungWaiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTableIsFree(RestaurantChungWaiter waiter, int t, RestaurantChungCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFlakeAlert(RestaurantChungCustomer c, int d) {
		log.add(new LoggedEvent("Host received msgFlakeAlert from Cashier. Debt of " + d));
		System.out.println("Host received msgFlakeAlert from Cashier. Debt of " + d);				
	}

}
