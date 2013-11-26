package utilities;

import java.util.ArrayList;
import java.util.HashMap;

import city.Application.FOOD_ITEMS;

public class RestaurantChoiMenu {
	public HashMap<FOOD_ITEMS, Integer> foodPrice = new HashMap<FOOD_ITEMS, Integer>();
	// these keep the orders simple
	
	public ArrayList<FOOD_ITEMS> dontHaveThis = new ArrayList<FOOD_ITEMS>();
	
	public RestaurantChoiMenu(){ 
		foodPrice.put(FOOD_ITEMS.steak,16);
		foodPrice.put(FOOD_ITEMS.pizza,11);
		foodPrice.put(FOOD_ITEMS.chicken,9);
		foodPrice.put(FOOD_ITEMS.salad,6);
	}

	public double getPrice(int index){
		return foodPrice.get(index);
	}
	public int getNumberOfItems(){
		return foodPrice.size() - dontHaveThis.size(); // what we can have - what we don't have
	}

	//if food is on the list of things that aren't available, can't order.
	public boolean isAvailable(FOOD_ITEMS food){
		return !dontHaveThis.contains(food); 
	}

}
