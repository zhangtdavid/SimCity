package city.interfaces;


import java.util.HashMap;

import utilities.RestaurantChoiOrder;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCook;
public interface RestaurantChoiCook extends RoleInterface{
	//Constructor
	
	//Messages
	public void msgRelease();
	public void msgHeresAnOrder(RestaurantChoiOrder or);
	public void msgFoodsDone(RestaurantChoiOrder o);
	//public void msgOutOfThisFood(Market m, int choice); // ? need market to be settled first TODO fix market here
	//public void msgFoodReceived(int choice, int amount, Market m);
	//msgs from gui
	public void msgAtRefrigerator();
	public void msgAtGrills();
	public void msgAtPlatingArea();
	
	//Scheduler
	
	//Actions
	public void CheckBack();
	public void MoveFoodToPlating(RestaurantChoiOrder o);
	public boolean AnalyzeCookOrder(RestaurantChoiOrder o);
	public boolean CookOrder(RestaurantChoiOrder o);
	public void DoGoToRefrig();
	public void DoGoToGrills();
	public void DoGoToPlates();
	
	//Getters
	public String getName();
	public RestaurantChoiAnimatedCook getGui();
	
	//Setters
	public void setGui(RestaurantChoiAnimatedCook gui);
	//public void setRevolvingStand(RestaurantChoiRevolvingStand r);
	//public RestaurantChoiRevolvingStand getRevolvingStand();
	//public void addMarket(Market m);
	public void setInactive();

	
	//Utilities
	public void hackNoFood();
	
	class myMarket{
		//Market market; TODO
		public HashMap<FOOD_ITEMS, Boolean> outOf;
		
		myMarket(/*Market m TODO*/){
			//market = m; TODO
			outOf.put(FOOD_ITEMS.chicken, false);
			outOf.put(FOOD_ITEMS.pizza, false);
			outOf.put(FOOD_ITEMS.salad, false);
			outOf.put(FOOD_ITEMS.steak, false);
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
