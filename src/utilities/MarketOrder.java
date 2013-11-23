package utilities;

import java.util.HashMap;
import java.util.Map;

import city.Application.FOOD_ITEMS;

public class MarketOrder {
	public static int currentID = 0;
	
	public Map<FOOD_ITEMS, Integer> orderItems = new HashMap<FOOD_ITEMS, Integer>();
	public Map<FOOD_ITEMS, Integer> collectedItems = new HashMap<FOOD_ITEMS, Integer>();
	public int id;
	public int bill;
	public int payment;
	
	public MarketOrder(HashMap<FOOD_ITEMS, Integer> o) {
        for (FOOD_ITEMS f: o.keySet()) {
        	orderItems.put(f, o.get(f)); // copy all items in the order hash map
        }
        for (FOOD_ITEMS f: o.keySet()) {
        	collectedItems.put(f, 0); // initialize collectedItems values to 0
        }
        id = currentID++;
		bill = 0;
		payment = 0;
	}
	
	// copy constructor
	public MarketOrder(MarketOrder o) {
        for (FOOD_ITEMS f: o.orderItems.keySet()) {
        	orderItems.put(f, o.orderItems.get(f));
        }
        for (FOOD_ITEMS f: o.orderItems.keySet()) {
        	collectedItems.put(f, o.collectedItems.get(f));
        }
        id = o.id;
		bill = o.bill;
		payment = o.payment;
	}
}