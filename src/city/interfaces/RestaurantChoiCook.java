package city.interfaces;

import city.Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import city.animations.interfaces.RestaurantChoiAnimatedCook;
import city.roles.RestaurantChoiOrder;
import city.roles.RestaurantChoiRevolvingStand;
public interface RestaurantChoiCook extends RoleInterface{

	//Data
	public List<RestaurantChoiOrder> orders = Collections.synchronizedList(new ArrayList<RestaurantChoiOrder>());
	Timer timer = new Timer(); // for cooking!
	public ConcurrentHashMap <Integer, Food> foods = new ConcurrentHashMap<Integer,Food>();
	public List<myMarket> markets = Collections.synchronizedList(new ArrayList<myMarket>());
	//RestaurantChoiRevolvingStand orderqueue = null; // this needs to be in the role specifically
	
	//Constructor
	
	//Messages
	public void msgRelease();
	public void msgOrderInQueue();
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
	
	
	//Getters
	public String getName();
	public RestaurantChoiAnimatedCook getGui();
	
	//Setters
	public void setGui(RestaurantChoiAnimatedCook gui);
	//public void setRevolvingStand(RestaurantChoiRevolvingStand r);
	//public RestaurantChoiRevolvingStand getRevolvingStand();
	//public void addMarket(Market m);
	
	public void MoveFoodToPlating(RestaurantChoiOrder o);
	public boolean AnalyzeCookOrder(RestaurantChoiOrder o);
	public boolean CookOrder(RestaurantChoiOrder o);
	public void DoGoToRefrig();
	public void DoGoToGrills();
	public void DoGoToPlates();
	
	//Utilities
	public void hackNoFood();
	
	class myMarket{
		//Market market; TODO
		public boolean[] outOf; // 4 foods, so 4 booleans

		myMarket(/*Market m TODO*/){
			//market = m; TODO
			outOf = new boolean[4];
		}
	}

	public class Food{
		public int choiceID;
		public int cookingTime; // how long it takes to cook choice
		public int inventory; // how many to have
		public int capacity; // amount to order to
		public int threshold;
		public int amountOrdered; // amount ordered that's being processed
		public Food(int i){ 
			choiceID = i;
			cookingTime = (i+2)*1000;
			inventory = 3+(int)Math.ceil(4*Math.random()); // starting inventory between 3-7
			//inventory = 0; // to test out of food
			threshold = (int)Math.floor(capacity*0.2); // when you ask market for restock.
			capacity = 7+(int)Math.ceil(10*Math.random()); // capacity between 7-17
			amountOrdered = 0;
		}
	}

}
