package utilities;

import java.util.ArrayList;
import java.util.List;

import city.Application.FOOD_ITEMS;

public class RestaurantChungMenu {
	public List<Item> items = new ArrayList<Item>();
	
	public class Item {
		private FOOD_ITEMS item;
		private int price;
		private boolean available;
		
		public Item(FOOD_ITEMS item, int price) {
			this.item = item;
			this.price = price;
			available = true;
		}
		
		// Getters
		public FOOD_ITEMS getItem() {
			return item;
		}
		
		public int getPrice() {
			return price;
		}
		
//		private boolean getAvailable() {
//			return available;
//		}
		
		// Setters
		public void setItem(FOOD_ITEMS item) {
			this.item = item;
		}
		
		public void setPrice(int price) {
			this.price = price;
		}
		
		public void setAvailable(boolean available) {
			this.available = available;
		}
	}
	
	public RestaurantChungMenu() {
		items.add(new Item(FOOD_ITEMS.chicken, 12));
		items.add(new Item(FOOD_ITEMS.pizza, 10));
		items.add(new Item(FOOD_ITEMS.salad, 6));
		items.add(new Item(FOOD_ITEMS.steak, 16));
	}
	
//	Copy constructor, called by the customer
	public RestaurantChungMenu(final RestaurantChungMenu original) {
		for (int i = 0; i < original.items.size(); i++) {
			if (original.items.get(i).available == true) {
				items.add(new Item(original.items.get(i).item, original.items.get(i).price));
			}
		}
	}
	
	public Item findItem(FOOD_ITEMS food) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).item == food) return items.get(i);
		}
		return null;
	}
}