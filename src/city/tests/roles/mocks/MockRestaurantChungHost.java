package city.tests.roles.mocks;


import java.beans.PropertyChangeSupport;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.roles.interfaces.RestaurantChungCashier;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungHost;
import city.roles.interfaces.RestaurantChungWaiter;
import city.tests.bases.mocks.MockRole;

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
		log.add(new LoggedEvent("RestaurantChungHost received msgIWantToEat from RestaurantChungCustomer"));		
		System.out.println("RestaurantChungHost received msgIWantToEat from RestaurantChungCustomer");			
	}

	@Override
	public void msgDecidedToStay(RestaurantChungCustomer c) {
		log.add(new LoggedEvent("RestaurantChungHost received msgDecidedToStay from RestaurantChungCustomer"));		
		System.out.println("RestaurantChungHost received msgDecidedToStay from RestaurantChungCustomer");			
	}

	@Override
	public void msgLeaving(RestaurantChungCustomer c) {
		log.add(new LoggedEvent("RestaurantChungHost received msgLeaving from RestaurantChungCustomer"));		
		System.out.println("RestaurantChungHost received msgLeaving from RestaurantChungCustomer");			
	}

	@Override
	public void msgTakingCustomerToTable(RestaurantChungCustomer c) {
		log.add(new LoggedEvent("RestaurantChungHost received msgTakingCustomerToTable from RestaurantChungWaiter"));		
		System.out.println("RestaurantChungHost received msgTakingCustomerToTable from RestaurantChungWaiter");					
	}

	@Override
	public void msgIWantToGoOnBreak(RestaurantChungWaiter w) {
		log.add(new LoggedEvent("RestaurantChungHost received msgIWantToGoOnBreak from RestaurantChungWaiter"));		
		System.out.println("RestaurantChungHost received msgIWantToGoOnBreak from RestaurantChungWaiter");			
	}

	@Override
	public void msgIAmReturningToWork(RestaurantChungWaiter w) {
		log.add(new LoggedEvent("RestaurantChungHost received msgIAmReturningToWork from RestaurantChungWaiter"));		
		System.out.println("RestaurantChungHost received msgIAmReturningToWork from RestaurantChungWaiter");			
	}

	@Override
	public void msgTableIsFree(RestaurantChungWaiter waiter, int t, RestaurantChungCustomer c) {
		log.add(new LoggedEvent("RestaurantChungHost received msgTableIsFree from RestaurantChungWaiter"));		
		System.out.println("RestaurantChungHost received msgTableIsFree from RestaurantChungWaiter");			
	}

	@Override
	public void msgFlakeAlert(RestaurantChungCustomer c, int d) {
		log.add(new LoggedEvent("Host received msgFlakeAlert from Cashier. Debt of " + d));
		System.out.println("Host received msgFlakeAlert from Cashier. Debt of " + d);				
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}

}
