package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import utilities.LoggedEvent;
import city.Application;
import city.buildings.interfaces.RestaurantTimms;
import city.roles.RestaurantTimmsCustomerRole.State;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.roles.interfaces.RestaurantTimmsWaiter;
import city.tests.bases.mocks.MockRole;

public class MockRestaurantTimmsCustomer extends MockRole implements RestaurantTimmsCustomer {
	
	// Constructor
	
	public MockRestaurantTimmsCustomer() {}
	
	// Messages
	
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
		log.add(new LoggedEvent("Received msgPaidCashier. Change: " + change)); 
	}
	
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
	
	// Getters
	
	@Override
	public State getState() {
		return null;
	}

	@Override
	public Application.FOOD_ITEMS getOrderItem() {
		return null;
	}

	// Setters
	
	@Override
	public void setRestaurantTimmsBuilding(RestaurantTimms b) {
		return;
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