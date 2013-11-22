package city.tests.mock;

import utilities.LoggedEvent;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangTable;
import city.MockRole;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangWaiter;

public class MockRestaurantZhangWaiterRegular extends MockRole implements RestaurantZhangWaiter {
	RestaurantZhangCustomer customer;
	
	public MockRestaurantZhangWaiterRegular() {
		super();
	}
	
	public void msgHereIsWaiterCheck(RestaurantZhangCheck c) {
		log.add(new LoggedEvent("Received check from cashier."));
		customer.msgHereIsCustCheck(c);
	}
	
	public void setCustomer(RestaurantZhangCustomer c) {
		customer = c;
	}

	@Override
	public void msgReadyToOrder(RestaurantZhangCustomer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(RestaurantZhangCustomer customer, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereWasMyOrder(RestaurantZhangCustomer customer, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingTable(RestaurantZhangCustomer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumberCustomers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberCustomersServed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void msgSeatCustomer(RestaurantZhangTable t, RestaurantZhangCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoOnBreak(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(RestaurantZhangTable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(String choice, RestaurantZhangTable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}
}
