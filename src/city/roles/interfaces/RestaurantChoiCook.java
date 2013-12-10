package city.roles.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.RestaurantChoiOrder;
import utilities.RestaurantChoiRevolvingStand;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCook;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Market;

public interface RestaurantChoiCook extends RoleInterface {
	
	// Data

	// Messages
	
	public void msgRelease();
	public void msgHeresAnOrder(RestaurantChoiOrder or);
	public void msgFoodsDone(RestaurantChoiOrder o);
	// public void msgOutOfThisFood(Market m, int choice); TODO
	public void msgFoodReceived(Map<FOOD_ITEMS, Integer> marketOrder, int id);
	public void msgAtRefrigerator();
	public void msgAtGrills();
	public void msgAtPlatingArea();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public RestaurantChoiAnimatedCook getGui();
	public RestaurantChoiRevolvingStand getRevolvingStand();
	public List<RestaurantChoiOrder> getOrders();
	
	// Setters
	
	public void setGui(RestaurantChoiAnimatedCook gui);
	// public void setRevolvingStand(RestaurantChoiRevolvingStand r);
	// public RestaurantChoiRevolvingStand getRevolvingStand();
	public void addMarket(Market m);
	public void setRevolvingStand(RestaurantChoiRevolvingStand in);

	// Utilities
	
	public void hackNoFood();
	public void CheckBack();
	
	// Classes
	
	class myMarket{
		public Market market;
		public HashMap<String, Boolean> outOf;
		
		public myMarket(Market m){
			market = m;
			outOf = new HashMap<String, Boolean>();
		}
		
		public Market getMarket(){
			return market;
		}
	}

	public class FoodData{
		public FOOD_ITEMS choiceID;
		public int cookingTime; // how long it takes to cook choice
		public int inventory; // how many to have
		public int capacity; // amount to order to
		public int threshold;
		public int amountOrdered; // amount ordered that's being processed
		
		public FoodData(FOOD_ITEMS i){ 
			choiceID = i;
			cookingTime = (int)Math.ceil(Math.random()*6)*1000;
			inventory = 3+(int)Math.ceil(4*Math.random()); // starting inventory between 3-7
			//inventory = 0; // to test out of food
			capacity = 7+(int)Math.ceil(10*Math.random()); // capacity between 7-17
			threshold = (int)Math.floor(capacity*0.2); // when you ask market for restock.
			amountOrdered = 0;
		}
	}
}
