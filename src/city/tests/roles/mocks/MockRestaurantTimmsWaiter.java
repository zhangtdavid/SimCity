package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;
import java.util.List;

import utilities.LoggedEvent;
import city.Application;
import city.roles.RestaurantTimmsWaiterRole.InternalCustomer;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.roles.interfaces.RestaurantTimmsWaiter;
import city.tests.bases.mocks.MockRole;

public class MockRestaurantTimmsWaiter extends MockRole implements RestaurantTimmsWaiter {

	// Constructor
	
	public MockRestaurantTimmsWaiter() {}

	// Messages
	
	@Override
	public void msgWantBreak() {
		log.add(new LoggedEvent("Received msgWantBreak."));
		
	}

	@Override
	public void msgAllowBreak(Boolean r) {
		log.add(new LoggedEvent("Received msgAllowBreak from Host. Disposition: " + r.toString()));
		
	}

	@Override
	public void msgSeatCustomer(RestaurantTimmsCustomer c, int n) {
		log.add(new LoggedEvent("Received msgSeatCustomer from Host. Table: " + n));
		
	}

	@Override
	public void msgWantFood(RestaurantTimmsCustomer c) {
		log.add(new LoggedEvent("Received msgWantFood from Customer."));
		
	}

	@Override
	public void msgOrderFood(RestaurantTimmsCustomer c, Application.FOOD_ITEMS s) {
		log.add(new LoggedEvent("Received msgOrderFood from Customer. Item: " + s.toString()));
		
	}

	@Override
	public void msgOrderPlaced(RestaurantTimmsCustomer c, Boolean inStock) {
		log.add(new LoggedEvent("Received msgOrderPlaced from Cook. In Stock: " + inStock.toString()));

	}

	@Override
	public void msgFoodReady(RestaurantTimmsCustomer c) {
		log.add(new LoggedEvent("Received msgFoodReady from Cook."));

	}

	@Override
	public void msgCheckReady() {
		log.add(new LoggedEvent("Received msgCheckReady from Cashier."));

	}

	@Override
	public void msgDoNotWantFood(RestaurantTimmsCustomer c) {
		log.add(new LoggedEvent("Received msgDoNotWantFood from Customer."));

	}

	@Override
	public void guiAtCustomer() {
		log.add(new LoggedEvent("Received guiAtHost."));

	}

	@Override
	public void guiAtTable() {
		log.add(new LoggedEvent("Received guiAtTable."));

	}

	@Override
	public void guiAtKitchen() {
		log.add(new LoggedEvent("Received guiAtKitchen."));

	}

	@Override
	public void guiAtHome() {
		log.add(new LoggedEvent("Received guiAtHome"));
	}
	
	// Getters

	@Override
	public Boolean getWantsBreak() {
		// This may not be suitable in the future.
		return false;
	}

	@Override
	public List<InternalCustomer> getCustomers() {
		return null;
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
