package city.roles.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import utilities.EventLog;
import utilities.MarketOrder;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCashier;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Market;
import city.roles.RestaurantChoiCashierRole.Check;

public interface RestaurantChoiCashier extends RoleInterface {

	// Data
	
	public static HashMap <FOOD_ITEMS, Integer> FOOD_COST = new HashMap<FOOD_ITEMS,Integer>();
	public static final int NOT_IN_TRANSIT = 0;
	public static final int IN_TRANSIT = 1;
    
	// Messages
	
	// public void msgPayMarket(Market m, int money); // TODO
	public void msgCompute(FOOD_ITEMS i, RestaurantChoiCustomer c, RestaurantChoiWaiter w);
	public void msgHeresMyPayment(RestaurantChoiCustomer c, int allHisCash);
	// public void msgHeresYourMarketBill(Market m, int type, int amount); TODO
	public void msgHeresYourMoney(int withdrawal);
	public void msgDoneWithDishes(RestaurantChoiCustomer c);
	public void msgAddMarketOrder(Market m, MarketOrder o);

	// Actions

	// Getters
	
	public RestaurantChoiAnimatedCashier getAnimation();
	public ArrayList<Check> getChecks();
	public EventLog getLog();
	public int getMoneyIncoming();
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment();
	
	// Setters
	
	public void setGui(RestaurantChoiAnimatedCashier r);
	public void setMarketCustomerDeliveryPaymentPerson();
	
	// Utilities

}
