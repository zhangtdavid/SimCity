package city.roles;

import java.util.HashMap;
import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import city.Application.FOOD_ITEMS;
import city.bases.RestaurantBuilding;
import city.bases.Role;
import city.bases.interfaces.RestaurantBuildingInterface;
import city.buildings.MarketBuilding;
import city.buildings.interfaces.Market;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketCustomerDeliveryPayment;

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
        for (FOOD_ITEMS s: o.getOrderItems().keySet()) {
        	receivedItems.put(s, 0); // initialize all values in receivedItems to 0
        }
        restaurantCashier = marketCustomerDeliveryPayment;
        state = MarketCustomerState.None;
    }

//  Activity
//	=====================================================================
	@Override
	public void setActive(){
		super.setActive();
		state = MarketCustomerState.Ordering;
		stateChanged();
	}
	
//  Messages
//	=====================================================================	
	@Override
	public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("MarketCustomerDelivery received msgHereIsOrderDelivery from MarketDeliveryPerson."));
		print("MarketCustomerDelivery received msgHereIsOrderDelivery from MarketDeliveryPerson.");
        if (order.getOrderId() == id) {
            for (FOOD_ITEMS item: collectedItems.keySet()) {
	            receivedItems.put(item, collectedItems.get(item)); // Create a deep copy of the order map
	            print("Received " + collectedItems.get(item) + " " + item.toString());
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
		market.getManager().msgIWouldLikeToPlaceADeliveryOrder(this, restaurantCashier, order.getOrderItems(), order.getOrderId());
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
	
	@Override
	public MarketOrder getOrder() {
		return order;
	}
	
//  Setters
//	=====================================================================
	@Override
	public void setRestaurant(RestaurantBuilding restaurant) {
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
		if(this.getPerson() != null){
			this.getPerson().printViaRole("MarketCustomerDeliveryPayment", msg);
        		AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketCustomerDeliveryPaymentRole " + this.getPerson().getName(), msg);
		}
    }
}
