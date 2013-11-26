package city.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCashier;

public interface RestaurantChoiCashier extends RoleInterface{

	//Data
	HashMap <Integer, Integer> foodCost = new HashMap<Integer,Integer>();
    ArrayList<Check> checks = new ArrayList<Check>();

	//ConcurrentHashMap<Market, Integer> marketBills = new ConcurrentHashMap<Market, Integer>(); TODO
	//ArrayList<Market> markets = new ArrayList<Market>(); // TODO match with Market in simcity201 TODO
	final static int NOT_IN_TRANSIT = 0;
	final static int IN_TRANSIT = 1;
	//Banker restaurantBanker; // TODO match with banker

    
	//Messages
	// public abstract void msgPayMarket(Market m, int money); // TODO
	public void msgCompute(FOOD_ITEMS food_ITEMS, RestaurantChoiCustomer c,
			RestaurantChoiWaiter restaurantChoiWaiter);
	public void msgHeresMyPayment(RestaurantChoiCustomer c, int allHisCash);
	//public void msgHeresYourMarketBill(Market m, int type, int amount); TODO
	public void msgHeresYourMoney(int withdrawal);
	public void msgDoneWithDishes(RestaurantChoiCustomer c);

	//Actions
	public void returnCheck(Check c);
	public void sendToDishes(Check ch);
	public void returnChange(Check ch);
	//public void getMoney(Banker b); TODO
	//public void payMarketBill(Market m, int payment); TODO

	//Getters
	public RestaurantChoiAnimatedCashier getAnimation();
	
	//Setters
	//public void addMarket(Market m);
	//public void setBanker(Banker b); // TODO fix this with simcity201 matching
	public void setInactive();
	
	//Utilities
	public class Check{

		public final static int RECEIVED = 0;
		public final static int GIVEN_TO_WAITER = 1;
		public final static int GET_PAID = 2;
		public final static int NOT_FULFILLED = -5;
		public final static int FULFILL_BY_DISHES = 3;
		public int bill;
		private int payment;
		private int state;
		private RestaurantChoiCustomer ca;
		private RestaurantChoiWaiter wa;
		public Check(FOOD_ITEMS choice, RestaurantChoiCustomer c, RestaurantChoiWaiter w){
			this.setwa(w);
			setBill(foodCost.get(choice)); // takes choice, gives cost.
			this.setca(c);
			setState(RECEIVED);
			setPayment(0);
		}

		public RestaurantChoiCustomer getca() {
			return ca;
		}
		public void setca(RestaurantChoiCustomer ca) {
			this.ca = ca;
		}
		public int getBill() {
			return bill;
		}
		public void setBill(int bill) {
			this.bill = bill;
		}
		public int getPayment() {
			return payment;
		}
		public void setPayment(int payment) {
			this.payment = payment;
		}
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
		public RestaurantChoiWaiter getwa() {
			return wa;
		}
		public void setwa(RestaurantChoiWaiter wa) {
			this.wa = wa;
		}
	
	}
}
