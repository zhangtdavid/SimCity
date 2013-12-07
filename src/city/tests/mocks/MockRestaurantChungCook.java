package city.tests.mocks;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

import utilities.EventLog;
import utilities.RestaurantChungOrder;
import utilities.RestaurantChungRevolvingStand;
import city.Application.FOOD_ITEMS;
import city.bases.Role;
import city.bases.interfaces.RestaurantBuildingInterface.Food;
import city.roles.RestaurantChungCookRole.MyMarketOrder;
import city.roles.interfaces.RestaurantChungCook;
import city.roles.interfaces.RestaurantChungWaiter;
import city.tests.bases.mocks.MockRole;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockRestaurantChungCook extends MockRole implements RestaurantChungCook {
	public EventLog log = new EventLog();

	@Override
	public void msgHereIsAnOrder(
			RestaurantChungWaiter restaurantChungWaiterBase, String choice,
			int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSelfLowFoodsIdentified() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSelfDoneCooking(RestaurantChungOrder o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSelfDonePlating(RestaurantChungOrder o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> marketOrder,
			int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtCookHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtGrill() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtPlating() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RestaurantChungWaiter findWaiter(RestaurantChungOrder order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Food findFood(String choice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyMarketOrder findMarketOrder(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeOrderFromList(RestaurantChungOrder order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeMarketOrderFromList(MyMarketOrder order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<RestaurantChungOrder> getOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Role> getMarketCustomerDeliveryRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MyMarketOrder> getMarketOrders() {
		// TODO Auto-generated method stub
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
