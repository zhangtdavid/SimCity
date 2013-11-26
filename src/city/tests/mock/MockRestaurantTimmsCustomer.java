package city.tests.mock;

import utilities.LoggedEvent;
import city.Application;
import city.Application.FOOD_ITEMS;
import city.MockRole;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsWaiter;
import city.roles.RestaurantTimmsCustomerRole.State;

public class MockRestaurantTimmsCustomer extends MockRole implements RestaurantTimmsCustomer {
	
	public MockRestaurantTimmsCustomer() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgRestaurantFull() { 
		log.add(new LoggedEvent("Received msgRestaurantFull from Host.")); 
	}
	
	@Override
	public void msgGoToTable(RestaurantTimmsWaiter w, int position) { 
		log.add(new LoggedEvent("Received msgGoToTable. Position: " + position)); 
	}

	@Override
	public void msgOrderFromWaiter() { 
		log.add(new LoggedEvent("Received msgOrderFromWaiter.")); 
	}
	
	@Override
	public void msgWaiterDeliveredFood(Application.FOOD_ITEMS stockItem) { 
		log.add(new LoggedEvent("Received msgWaiterDeliveredFood. Item: " + stockItem.toString())); 
	}

	@Override
	public void msgPaidCashier(int change) { 
		log.add(new LoggedEvent("Received msgPaidCashier. Change: " + change)); }
	
	@Override
	public void guiAtTable() { 
		log.add(new LoggedEvent("Received guiAtTable.")); 
	}
	
	@Override
	public void guiAtCashier() { 
		log.add(new LoggedEvent("Received guiAtCashier.")); 
	}

	@Override
	public void guiAtLine() { 
		log.add(new LoggedEvent("Received guiAtLine.")); 
	}

	@Override
	public void guiAtExit() { 
		log.add(new LoggedEvent("Received guiAtExit.")); 
	}

	@Override
	public State getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FOOD_ITEMS getOrderItem() {
		// TODO Auto-generated method stub
		return null;
	}

}