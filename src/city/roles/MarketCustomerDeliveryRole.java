package city.roles;

import java.util.HashMap;
import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import city.abstracts.RestaurantBuildingBase;
import city.abstracts.RestaurantBuildingInterface;
import city.buildings.MarketBuilding;
import city.interfaces.Market;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.Application.FOOD_ITEMS;
import city.Role;

public class MarketCustomerDeliveryRole extends Role implements MarketCustomerDelivery {
//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private RestaurantBuildingInterface restaurant;
	private Market market;
	private MarketCustomerDeliveryPayment restaurantCashier;
	
	private MarketOrder order;
	private Map<FOOD_ITEMS, Integer> receivedItems = new HashMap<FOOD_ITEMS, Integer>();
	
	private MarketCustomerState state;
	
//	Constructor
//	=====================================================================
	public MarketCustomerDeliveryRole(RestaurantBuildingInterface r, MarketOrder o, MarketCustomerDeliveryPayment marketCustomerDeliveryPayment) {
		super(); // TODO
		restaurant = r;
		order = new MarketOrder(o);
        for (FOOD_ITEMS s: o.orderItems.keySet()) {
        	receivedItems.put(s, 0); // initialize all values in receivedItems to 0
        }
        restaurantCashier = marketCustomerDeliveryPayment;
        state = MarketCustomerState.None;
    }

//  Activity
//	=====================================================================
	@Override
	public void setActive(){
		// this.setActivityBegun();
        state = MarketCustomerState.Ordering;
        runScheduler(); // direct call to scheduler necessary for nested roles
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
		market.getManager().msgIWouldLikeToPlaceADeliveryOrder(this, restaurantCashier, order.orderItems, order.orderId);
		super.setInactive(); // set role inactive after placing order
	}

//  Getters
//	=====================================================================
	@Override
	public RestaurantBuildingInterface getRestaurant() {
		return restaurant;
	}

	@Override
	public Market getMarket() {
		return market;
	}
	
	@Override
	public MarketCustomerState getState() {
		return state;
	}
	
//  Setters
//	=====================================================================
	@Override
	public void setRestaurant(RestaurantBuildingBase restaurant) {
		this.restaurant = restaurant;
	}
	
	@Override
	public void setMarket(MarketBuilding market) {
		this.market = market;
	}

	@Override
	public void setState(MarketCustomerState state) {
		this.state = state;
	}
	
//  Utilities
//	=====================================================================
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketCustomerDeliveryRole " + this.getPerson().getName(), msg);
    }
}
