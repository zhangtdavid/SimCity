package utilities;

import java.util.Collections;
import java.util.Random;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class RestaurantZhangMenu {
	private static Map<String, Double> menuItemsMap = Collections.synchronizedMap(new HashMap<String, Double>());
	private static Map<String, Double> allMenuItems;
//	private RestaurantPanel panel;
	
	public RestaurantZhangMenu() {
		menuItemsMap.put("Chicken", 8.99);
		menuItemsMap.put("Beef", 12.99);
		menuItemsMap.put("Pizza", 16.99);
		allMenuItems = Collections.synchronizedMap(new HashMap<String, Double>(menuItemsMap));
	}
	
	public void addItem(String name, double price) {
		menuItemsMap.put(name, price);
	}
	
	public void reAddItem(String name) {
		menuItemsMap.put(name, allMenuItems.get(name));
//		panel.updateMenu();
	}
	
	public String randomChoice(double money) {
		// Menu is empty, out of inventory
		if(menuItemsMap.isEmpty()) {
			return "None";
		}
		// Set up list of possible menu choices given customer's money
		String optionsArray[] = new String[menuItemsMap.size()];
		int optionsArraySize = 0;
		synchronized(menuItemsMap) {
			Iterator<Map.Entry<String, Double>> it = menuItemsMap.entrySet().iterator();
			for(int i = 0; i < menuItemsMap.size(); i++) {
				Map.Entry<String, Double> currentItem = it.next();
				if(currentItem.getValue() <= money) {
					optionsArray[optionsArraySize] = currentItem.getKey();
					optionsArraySize++;
				}
			}
			// Small chance to order something too expensive, might actually not be too expensive
			// Depends on what stuff is available in the menu
			if(optionsArraySize < menuItemsMap.size() && money < 8.99) {
				if(new Random().nextInt(5) == 0)
					if(menuItemsMap.containsKey("Pizza")) {
						return "Pizza";
					} else if(menuItemsMap.containsKey("Steak")) {
						return "Steak";
					} else if(menuItemsMap.containsKey("Chicken")) {
						return "Chicken";
					} else {
						return "None";
					}
			}
			// Process the list of available choices
			if(optionsArraySize <= 0) {
				return "Expensive";
			} else {
				return optionsArray[new Random().nextInt(optionsArraySize)]; // Get a random index in options array
			}
		}
	}
	
	public double getPrice(String choice) {
		return menuItemsMap.get(choice);
	}
	
	public Map<String, Double> getMenu() {
		return menuItemsMap;
	}
	
	public void remove(String item) {
		menuItemsMap.remove(item);
//		panel.updateMenu();
	}
	
//	public void setRestPanel(RestaurantPanel rp) {
//		panel = rp;
//	}
}
