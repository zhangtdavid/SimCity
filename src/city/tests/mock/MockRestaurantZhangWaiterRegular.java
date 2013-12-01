package city.tests.mock;

import utilities.LoggedEvent;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.abstracts.MockRole;
import city.animations.interfaces.RestaurantZhangAnimatedWaiter;
import city.interfaces.RestaurantZhangCashier;
import city.interfaces.RestaurantZhangCook;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangHost;
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
		log.add(new LoggedEvent("Order for table " + t.tableNumber + " and choice " + choice + " received"));
	}

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAnimation(RestaurantZhangAnimatedWaiter gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCook(RestaurantZhangCook c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(RestaurantZhangHost h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(RestaurantZhangCashier c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMenu(RestaurantZhangMenu m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRevolvingStand(RestaurantZhangRevolvingStand rs) {
		// TODO Auto-generated method stub
		
	}
}
