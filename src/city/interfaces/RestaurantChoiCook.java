package city.interfaces;


import java.util.HashMap;

import utilities.RestaurantChoiOrder;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCook;
import city.buildings.MarketBuilding;
public interface RestaurantChoiCook extends RoleInterface{
	//Constructor
	
	//Messages
	public abstract void msgRelease();
	public abstract void msgHeresAnOrder(RestaurantChoiOrder or);
	public abstract void msgFoodsDone(RestaurantChoiOrder o);
	//public abstract void msgOutOfThisFood(Market m, int choice); // ? need market to be settled first TODO fix market here
	//public abstract void msgFoodReceived(int choice, int amount, Market m);
	//msgs from gui
	public abstract void msgAtRefrigerator();
	public abstract void msgAtGrills();
	public abstract void msgAtPlatingArea();
	
	//Scheduler
	
	//Actions
	public abstract void CheckBack();
	public abstract void MoveFoodToPlating(RestaurantChoiOrder o);
	public abstract boolean AnalyzeCookOrder(RestaurantChoiOrder o);
	public abstract boolean CookOrder(RestaurantChoiOrder o);
	public abstract void DoGoToRefrig();
	public abstract void DoGoToGrills();
	public abstract void DoGoToPlates();
	
	//Getters
	public abstract String getName();
	public abstract RestaurantChoiAnimatedCook getGui();
	
	//Setters
	public abstract void setGui(RestaurantChoiAnimatedCook gui);
	//public abstract void setRevolvingStand(RestaurantChoiRevolvingStand r);
	//public abstract RestaurantChoiRevolvingStand getRevolvingStand();
	public abstract void addMarket(MarketBuilding m);
	public abstract void setInactive();

	
	//Utilities
	public abstract void hackNoFood();
	
	class myMarket{
		MarketBuilding market;
		public HashMap<String, Boolean> outOf;
		
		public myMarket(MarketBuilding m){
			market = m;
			outOf.put("Chicken", false);
			outOf.put("Pizza", false);
			outOf.put("Salad", false);
			outOf.put("Steak", false);
		}
		public MarketBuilding getMarket(){
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
