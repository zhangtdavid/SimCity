package city.interfaces;

import java.util.HashMap;

import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCashier;

public interface RestaurantChoiCashier extends RoleInterface{

	//Data
	public static HashMap <FOOD_ITEMS, Integer> foodCost = new HashMap<FOOD_ITEMS,Integer>();

	//ConcurrentHashMap<Market, Integer> marketBills = new ConcurrentHashMap<Market, Integer>(); TODO
	//ArrayList<Market> markets = new ArrayList<Market>(); // TODO match with Market in simcity201 TODO
	final static int NOT_IN_TRANSIT = 0;
	final static int IN_TRANSIT = 1;
    
	//Messages
	// public abstract void msgPayMarket(Market m, int money); // TODO
	public abstract void msgCompute(FOOD_ITEMS food_ITEMS, RestaurantChoiCustomer c,
			RestaurantChoiWaiter restaurantChoiWaiter);
	public abstract void msgHeresMyPayment(RestaurantChoiCustomer c, int allHisCash);
	//public void msgHeresYourMarketBill(Market m, int type, int amount); TODO
	public abstract void msgHeresYourMoney(int withdrawal);
	public abstract void msgDoneWithDishes(RestaurantChoiCustomer c);

	//Actions

	public abstract void depositMoney();
	public abstract void getMoney();
	//public void payMarketBill(Market m, int payment); TODO

	//Getters
	public abstract RestaurantChoiAnimatedCashier getAnimation();
	
	//Setters
	//public void addMarket(Market m);
	public abstract void setInactive();
	
	//Utilities

}
