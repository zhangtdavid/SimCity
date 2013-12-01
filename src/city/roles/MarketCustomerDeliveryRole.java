package city.roles;

import java.util.HashMap;
import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import city.abstracts.RestaurantBuildingBase;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.Application.FOOD_ITEMS;
import city.Role;

public class MarketCustomerDeliveryRole extends Role implements MarketCustomerDelivery {

//  Data
//	=====================================================================	
	private RestaurantBuildingBase restaurant;
	private MarketBuilding market;
	private MarketCustomerDeliveryPayment restaurantCashier;
	private MarketOrder order;
	private Map<FOOD_ITEMS, Integer> receivedItems = new HashMap<FOOD_ITEMS, Integer>();
	
	// TODO Change these to private and add getters/setters
	public EventLog log = new EventLog();
	public MarketCustomerState state;
	public enum MarketCustomerState {None, Ordering};
	
//	Constructor
//	---------------------------------------------------------------
	public MarketCustomerDeliveryRole(RestaurantBuildingBase r, MarketOrder o, MarketCustomerDeliveryPayment marketCustomerDeliveryPayment) {
		super(); // TODO
		restaurant = r;
		order = new MarketOrder(o);
        for (FOOD_ITEMS s: o.orderItems.keySet()) {
        	receivedItems.put(s, 0); // initialize all values in receivedItems to 0
        }
        restaurantCashier = marketCustomerDeliveryPayment;
        state = MarketCustomerState.None;
    }

//  Messages
//	=====================================================================	
	@Override
	public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("Market CustomerDelivery received msgHereIsOrder from Market DeliveryPerson."));
		System.out.println("Market customerDelivery received msgHereIsOrder from Market DeliveryPerson.");
		if (order.orderId == id) {
	        for (FOOD_ITEMS item: collectedItems.keySet()) {
	            receivedItems.put(item, collectedItems.get(item)); // Create a deep copy of the order map
	        }
	        restaurant.incrementFoodQuantity(receivedItems);
		}
		super.setInactive(); // set role inactive after receiving order
	}
	
//  Scheduler
//	=====================================================================
	@Override
	public boolean runScheduler() {
		if (state == MarketCustomerState.Ordering) {
			callMarket();
			return true;
		}
		
		return false;
	}
	
//  Actions
//	=====================================================================	
	private void callMarket() {
		state = MarketCustomerState.None;
		market.manager.msgIWouldLikeToPlaceADeliveryOrder(this, restaurantCashier, order.orderItems, order.orderId);
		super.setInactive(); // set role inactive after placing order
	}

//  Getters and Setters
//	=====================================================================
	// Restaurant
	@Override
	public RestaurantBuildingBase getRestaurant() {
		return restaurant;
	}
	
	@Override
	public void setRestaurant(RestaurantBuildingBase restaurant) {
		this.restaurant = restaurant;
	}
	
	// Market
	@Override
	public MarketBuilding getMarket() {
		return market;
	}
	
	@Override
	public void setMarket(MarketBuilding market) {
		this.market = market;
	}
	
	@Override
	public void setActive(){
		// this.setActivityBegun();
        state = MarketCustomerState.Ordering;
        runScheduler();
	}
	
//  Utilities
//	=====================================================================
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketCustomerDeliveryRole " + this.getPerson().getName(), msg);
    }
}
