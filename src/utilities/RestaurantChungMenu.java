package utilities;

import java.util.ArrayList;
import java.util.List;

public class RestaurantChungMenu {
	public List<Item> items = new ArrayList<Item>();
	
	public class Item {
		public String item;
		public double price;
		public boolean available;
		
		public Item(String item, double price) {
			this.item = item;
			this.price = price;
			available = true;
		}
	}
	
	public RestaurantChungMenu() {
		items.add(new Item("steak", 15.99));
		items.add(new Item("chicken", 10.99));
		items.add(new Item("salad", 5.99));
		items.add(new Item("pizza", 8.99));
	}
	
//	Copy constructor, called by the customer
	public RestaurantChungMenu(final RestaurantChungMenu original) {
		for (int i = 0; i < original.items.size(); i++) {
			if (original.items.get(i).available == true) {
				items.add(new Item(original.items.get(i).item, original.items.get(i).price));
			}
		}
	}
	
	public Item findItem(String food) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).item == food) return items.get(i);
		}
		return null;
	}
}