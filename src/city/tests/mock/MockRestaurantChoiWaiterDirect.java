package city.tests.mock;

import java.util.List;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.RestaurantChoiOrder;
import utilities.RestaurantChoiRevolvingStand;
import utilities.RestaurantChoiTable;
import city.Application.FOOD_ITEMS;
import city.MockRole;
import city.animations.RestaurantChoiWaiterAnimation;
import city.interfaces.RestaurantChoiCashier;
import city.interfaces.RestaurantChoiCook;
import city.interfaces.RestaurantChoiCustomer;
import city.interfaces.RestaurantChoiHost;
import city.interfaces.RestaurantChoiWaiter;

public class MockRestaurantChoiWaiterDirect extends MockRole implements RestaurantChoiWaiter  {
	String name;
	public EventLog log = new EventLog();
	
	public MockRestaurantChoiWaiterDirect(String name){
	this.name = name;	
	}
	@Override
	public void msgSeatCustomer(RestaurantChoiCustomer restaurantChoiCustomer,
			RestaurantChoiTable restaurantChoiTable) {
		// TODO Auto-generated method stub
	}

	@Override
	public void msgReadyToOrder(RestaurantChoiCustomer c) {
		// TODO Auto-generated method stub		
	}


	@Override
	public void msgOrderComplete(RestaurantChoiOrder o) {
 		log.add(new LoggedEvent("Received msgOrderComplete from cook."));
 		System.out.println("Order details:\n Table #:" + o.getTableNumber() + " ");
 		System.out.println("State:" + o.getState() + " ");
 		System.out.println("Choice:" + o.getChoice() + " ");		
	}
	
	@Override
	public void msgImDone(RestaurantChoiCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfThisFood(RestaurantChoiOrder o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHeresCheck(int total, RestaurantChoiCustomer ca) {
 		log.add(new LoggedEvent("Received HeresCheck from waiter. Amount, Customer = "+ total + ", " + ca.getPerson().getName()));
		
	}

	@Override
	public void msgRelease() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgBreakOK(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<myCustomer> getMyCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RestaurantChoiTable> getTables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean askedForBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setHost(RestaurantChoiHost h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCook(RestaurantChoiCook c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(RestaurantChoiCashier ca) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgHeresMyOrder(RestaurantChoiCustomer c, FOOD_ITEMS choice) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgCheckPlz(RestaurantChoiCustomer c, FOOD_ITEMS choice) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setAnimation(RestaurantChoiWaiterAnimation r) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setRevolvingStand(RestaurantChoiRevolvingStand rs) {
		// TODO Auto-generated method stub
		
	}

}
