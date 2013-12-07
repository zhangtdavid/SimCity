package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;
import java.util.Collection;

import utilities.LoggedEvent;
import utilities.RestaurantZhangTable;
import city.roles.interfaces.RestaurantZhangCustomer;
import city.roles.interfaces.RestaurantZhangHost;
import city.roles.interfaces.RestaurantZhangWaiter;
import city.tests.bases.mocks.MockRole;

public class MockRestaurantZhangHost extends MockRole implements RestaurantZhangHost {
	
	public MockRestaurantZhangHost() {
		super();
	}

	@Override
	public void msgImEntering(RestaurantZhangCustomer cust) {
		log.add(new LoggedEvent(cust.getPerson().getName() + " entered restaurant"));
		
	}

	@Override
	public void msgIWantFood(RestaurantZhangCustomer cust) {
		log.add(new LoggedEvent("Customer " + cust.getPerson().getName() + " wants food"));
	}

	@Override
	public void msgImLeaving(RestaurantZhangCustomer cust) {
		log.add(new LoggedEvent("Customer " + cust.getPerson().getName() + " is leaving"));
	}

	@Override
	public void msgTableOpen(RestaurantZhangTable t) {
		log.add(new LoggedEvent("Table " + t.tableNumber + " is empty"));
	}

	@Override
	public void msgWaiterRequestBreak(RestaurantZhangWaiter w) {
		log.add(new LoggedEvent("Waiter " + w.getPerson().getName() + " requested break"));
	}

	@Override
	public void msgOffBreak(RestaurantZhangWaiter w) {
		log.add(new LoggedEvent("Waiter " + w.getPerson().getName() + " is off break"));
	}

	@Override
	public void addWaiter(RestaurantZhangWaiter wa) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumberOfCustomersInRestaurant() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTables(Collection<RestaurantZhangTable> t) {
		// TODO Auto-generated method stub
		
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
