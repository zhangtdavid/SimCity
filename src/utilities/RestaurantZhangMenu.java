package utilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class RestaurantZhangMenu {
	private static Map<String, Integer> menuItemsMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private static Map<String, Integer> allMenuItems;
//	private RestaurantPanel panel;
	
	public RestaurantZhangMenu() {
		menuItemsMap.put("chicken", 9);
		menuItemsMap.put("steak", 13);
		menuItemsMap.put("pizza", 17);
		allMenuItems = Collections.synchronizedMap(new HashMap<String, Integer>(menuItemsMap));
	}
	
	public void addItem(String name, int price) {
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
			Iterator<Map.Entry<String, Integer>> it = menuItemsMap.entrySet().iterator();
			for(int i = 0; i < menuItemsMap.size(); i++) {
				Map.Entry<String, Integer> currentItem = it.next();
				if(currentItem.getValue() <= money) {
					optionsArray[optionsArraySize] = currentItem.getKey();
					optionsArraySize++;
				}
			}
			// Small chance to order something too expensive, might actually not be too expensive
			// Depends on what stuff is available in the menu
			if(optionsArraySize < menuItemsMap.size() && money < 9) {
				if(new Random().nextInt(5) == 0)
					if(menuItemsMap.containsKey("pizza")) {
						return "pizza";
					} else if(menuItemsMap.containsKey("steak")) {
						return "steak";
					} else if(menuItemsMap.containsKey("chicken")) {
						return "chicken";
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
	
	public int getPrice(String choice) {
		return menuItemsMap.get(choice);
	}
	
	public Map<String, Integer> getMenu() {
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
