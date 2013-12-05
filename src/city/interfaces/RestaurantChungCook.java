package city.interfaces;

import java.util.List;
import java.util.Map;

import utilities.RestaurantChungOrder;
import utilities.RestaurantChungRevolvingStand;
import city.Role;
import city.RoleInterface;
import city.Application.FOOD_ITEMS;
import city.abstracts.RestaurantBuildingInterface.Food;
import city.roles.RestaurantChungCookRole.MyMarketOrder;

public interface RestaurantChungCook extends RoleInterface {
	public enum WorkingState {Working, GoingOffShift, NotWorking};
    public enum MarketOrderState {Pending, Ordered};
	
	// Messages
	public void msgHereIsAnOrder(RestaurantChungWaiter restaurantChungWaiterBase, String choice, int table);
	public void msgSelfLowFoodsIdentified();
	public void msgSelfDoneCooking(RestaurantChungOrder o);
	public void msgSelfDonePlating(RestaurantChungOrder o);
	public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> marketOrder, int id);
	// public void msgCannotFulfill(int iD, Map<String, Integer> unfulfilled);
	public void msgAnimationAtCookHome();
	public void msgAnimationAtGrill();
	public void msgAnimationAtPlating();
	
	// Getters
	RestaurantChungRevolvingStand getRevolvingStand();
	List<RestaurantChungOrder> getOrders();
	List<Role> getMarketCustomerDeliveryRoles();
	List<MyMarketOrder> getMarketOrders();	
	// Setters
	public void setRevolvingStand(RestaurantChungRevolvingStand stand);

	// Utilities
	RestaurantChungWaiter findWaiter(RestaurantChungOrder order);
	Food findFood(String choice);
	MyMarketOrder findMarketOrder(int id);
	void removeOrderFromList(RestaurantChungOrder order);
	void removeMarketOrderFromList(MyMarketOrder order);
}