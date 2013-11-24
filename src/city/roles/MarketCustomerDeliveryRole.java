package city.roles;

import java.util.HashMap;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketManager;
import city.Application.FOOD_ITEMS;
import city.Role;

public class MarketCustomerDeliveryRole extends Role implements MarketCustomerDelivery {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private MarketBuilding market;
	private MarketManager manager;
	
	private MarketCustomerDeliveryPayment restaurantCashier;
	
	private MarketOrder order;
	private Map<FOOD_ITEMS, Integer> receivedItems = new HashMap<FOOD_ITEMS, Integer>();
	
	private enum MarketCustomerState
	{None, Ordering};
	MarketCustomerState state;
	
//	Constructor
//	---------------------------------------------------------------
	public MarketCustomerDeliveryRole(MarketOrder o, MarketCustomerDeliveryPayment marketCustomerDeliveryPayment) {
		super(); // TODO
        for (FOOD_ITEMS s: order.orderItems.keySet()) {
        	receivedItems.put(s, 0); // initialize all values in receivedItems to 0
        }
        restaurantCashier = marketCustomerDeliveryPayment;
        state = MarketCustomerState.Ordering;
        active = true;
    }
	
//  Messages
//	=====================================================================	
	public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("Market CustomerDelivery received msgHereIsOrder from Market DeliveryPerson."));
		System.out.println("Market customerDelivery received msgHereIsOrder from Market DeliveryPerson.");
		if (order.orderId == id) {
	        for (FOOD_ITEMS item: collectedItems.keySet()) {
	            receivedItems.put(item, collectedItems.get(item)); // Create a deep copy of the order map
	        }
		}
        active = false; // set role inactive after receiving order
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
		manager.msgIWouldLikeToPlaceADeliveryOrder(this, restaurantCashier, order.orderItems, order.orderId);
		active = false; // set role inactive after placing order
	}

//  Getters and Setters
//	=====================================================================
	// Market
	public MarketBuilding getMarket() {
		return market;
	}
	
	public void setMarket(MarketBuilding market) {
		this.market = market;
	}
	
	// Manager
	public MarketManager getManager() {
		return manager;
	}
	
	public void setManager(MarketManager manager) {
		this.manager = manager;
	}
	
//  Utilities
//	=====================================================================

}
