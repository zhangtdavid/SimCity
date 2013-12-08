package utilities;

import java.util.HashMap;
import java.util.Map;

import city.Application.FOOD_ITEMS;

public class MarketOrder {
	public static int currentID = 0;
	
	private Map<FOOD_ITEMS, Integer> orderItems = new HashMap<FOOD_ITEMS, Integer>();
	private int orderId;
	
	public MarketOrder(Map<FOOD_ITEMS, Integer> o) {
        for (FOOD_ITEMS f: o.keySet()) {
        	orderItems.put(f, o.get(f)); // copy all items in the order hash map
        }
        orderId = currentID++;
	}
	
	// copy constructor
	public MarketOrder(MarketOrder o) {
        for (FOOD_ITEMS f: o.orderItems.keySet()) {
        	orderItems.put(f, o.orderItems.get(f));
        }
        orderId = o.orderId;
	}
	
	// Getters
	public Map<FOOD_ITEMS, Integer> getOrderItems() {
		return orderItems;
	}
	
	public int getOrderId() {
		return orderId;
	}
	
	// Setters
	public static void setCurrentID(int currentID) {
		MarketOrder.currentID = currentID;
	}
}