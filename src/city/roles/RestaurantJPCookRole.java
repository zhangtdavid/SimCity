package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import utilities.MarketOrder;
import utilities.RestaurantJPRevolvingStand;
import utilities.RestaurantJPTableClass;
import city.Application.FOOD_ITEMS;
import city.animations.RestaurantJPCookAnimation;
import city.bases.JobRole;
import city.buildings.RestaurantJPBuilding;
import city.buildings.interfaces.RestaurantJP;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketManager;
import city.roles.interfaces.RestaurantJPCook;
import city.roles.interfaces.RestaurantJPWaiter;

public class RestaurantJPCookRole extends JobRole implements RestaurantJPCook {
	
	//DATA	
	private RestaurantJPCookAnimation gui;
	private RestaurantJPBuilding building;
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<MyMarket> Markets = Collections.synchronizedList(new ArrayList<MyMarket>());
	boolean ordering = false;
	private Semaphore atDestination = new Semaphore(0,true);
	boolean wantsInactive = false;
	RestaurantJPRevolvingStand revolvingStand;
	boolean waitingToCheckStand = false;
	String name;
	public enum state{pending, cooking, done, finished, taken};
	Timer timer = new Timer();
	Map<String, Food> Foods = new HashMap<String, Food>();
	int low = 2;
	MarketCustomerDelivery deliveryRole;

	//Constructor
	
	public RestaurantJPCookRole(RestaurantJPBuilding b, int shiftStart, int shiftEnd) {
		super();
		building = b;
		//name = this.getPerson().getName();
		this.setWorkplace(b);
		this.setSalary(RestaurantJP.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		Foods.put("Steak", new Food(5000, 5, FOOD_ITEMS.steak));
		Foods.put("Chicken", new Food(4000, 5, FOOD_ITEMS.chicken));
		Foods.put("Salad", new Food(3000, 5, FOOD_ITEMS.salad));
		Foods.put("Pizza", new Food(2000, 5, FOOD_ITEMS.pizza));					//HACKHACKHACK
	}
	
//MSGS-----------------------------------------------------------------------------------------------------------
	
	public void msgHereIsAnOrder(RestaurantJPWaiter wait, String c, RestaurantJPTableClass t) {
		//Do("HereIsOrder message received from " + w.getName());
		synchronized(orders){
		orders.add(new Order(wait, c, t));
		}
		stateChanged();
	}
	
	public void msgOrderCannotBeFulfilled(String food, MarketManager m){
		//Do("Out of food message received from " + m.toString());
		synchronized(Markets){
			for(MyMarket myM : Markets){
			if (myM.m == m){
				//Do("Setting " + food + " unstocked for " + m.toString());
				myM.setUnstocked(food);
				}
		}
		}
		stateChanged();
	}
	
	public void msgShipmentReady(String f){
		//Do("Shipment received from Market");
		if(Foods.containsKey(f))
			Foods.get(f).inventory += 5;
		stateChanged();
	}
	
	public void msgMarketDry(MarketManager m){
		//Do("Market dry message received from " + m.getName());
		synchronized(Markets){
		Markets.remove(m);
		}
		stateChanged();
	}
	
	public void msgAtDestination() {//from animation
		atDestination.release();// = true;
		stateChanged();
	}

	public void msgFoodRetrieved(String f){
		//Do("Food retrieved");
		synchronized(orders){
		for(Order o : orders){
			if(o.s == state.finished && o.choice.equals(f))
				o.s = state.taken;
		}
		}
		stateChanged();
	}
	
//SCHEDULER------------------------------------------------------------------------
	
	public boolean runScheduler() {
		if(wantsInactive && orders.size() == 0 && building.seatedCustomers == 0){
			super.setInactive();
			this.getPerson().setCash(this.getPerson().getCash() + RestaurantJP.WORKER_SALARY);
			wantsInactive = false;
		}
		if(!ordering){
			Map<FOOD_ITEMS, Integer> neededFoods = new HashMap<FOOD_ITEMS, Integer>();
			for (Map.Entry<String, Food> entry : Foods.entrySet())
			{
				if(entry.getValue().inventory <= low)
					neededFoods.put(entry.getValue().type, 5);
			}
			deliveryRole = new MarketCustomerDeliveryRole(building, new MarketOrder(neededFoods), building.cashier.getMarketCustomerDeliveryPayment());
		}
		
		synchronized(orders){
		for(Order o : orders){
			if(o.s == state.pending)
			{
				TryToCookIt(o);
				return true;
			}
		}
		}
		synchronized(orders){
		for(Order o : orders){
			if(o.s == state.done)
			{
				PlateIt(o);
				return true;
			}
		}
		}
		synchronized(orders){
		for(Order o : orders){
			if(o.s == state.taken)
			{
				OrderFinished(o);
				return true;
			}
		}
		
		if(!waitingToCheckStand) {
			waitingToCheckStand = true;
			timer.schedule(new TimerTask() {
				public void run() {
					waitingToCheckStand = false;
					Order newOrder = revolvingStand.remove();
					if(newOrder != null) {
						print("Found an item on the stand");
						orders.add(newOrder);
					}
					stateChanged();
				}
			},
			5000);
		}
		
		return false;
		}
		
		
	}

// ACTIONS----------------------------------------------------------------------------------------

	private void TryToCookIt(final Order o) {
		
		Food temp = new Food();
		temp = Foods.get(o.choice);
		
		if(temp.inventory == 0){
			o.w.msgOutOf(o.choice, o.table);
			synchronized(orders){
			orders.remove(o);
			}
		}	
		else{
			//Do("Cook is cooking " + o.choice);
			gui.DoGoToFridge();
			try {
				atDestination.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gui.DoGrillIt(o.choice);
			try {
				atDestination.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			o.s = state.cooking;
		
			timer.schedule(new TimerTask() {
				public void run() {
					gui.DoRemoveFromGrill(o.choice);
					try {
						atDestination.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					gui.DoBringToPlating(o.choice);
					try {
						atDestination.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					gui.DoPlateIt(o.choice);
					o.s = state.done;
					stateChanged();
				}},
				temp.cookingTime);
			temp.inventory--;
		}
	}
	
	
	private void PlateIt(Order o) {
		o.s = state.finished;
		print("HERE");
		o.w.msgOrderIsReady(o.choice, o.table);
	}
	
	private void OrderFinished(Order o) {
		//Do("Order finished action");
		gui.DoOrderRemoved(o.choice);
		orders.remove(o);
	}
	
	
	//Getters
	
	public String getName() {
		return name;
	}
	
	//Setters
	
	public void setRevolvingStand(RestaurantJPRevolvingStand orderStand) {
		// TODO Auto-generated method stub
		revolvingStand = orderStand;
	}
	
	public void setInactive(){
		if(orders.size() == 0 && building.seatedCustomers == 0){
			super.setInactive();
			this.getPerson().setCash(this.getPerson().getCash() + RestaurantJP.WORKER_SALARY);
		}
		else
			wantsInactive = true;
	}
	
	public void setGui(RestaurantJPCookAnimation g){
		gui = g;
	}
	public void addMarket(MarketManagerRole m){
		Markets.add(new MyMarket(m));
	}
	
	public void setAnimation(RestaurantJPCookAnimation g) {
		gui = g;
		
	}
	
	//Utilities
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTJP, "RestaurantJPCookRole " + this.getPerson().getName(), msg);
    }
	
	//Classes
	
	public class Order{
		state s;
		RestaurantJPWaiter w;
		String choice;
		RestaurantJPTableClass table;
		public Order(RestaurantJPWaiter wait, String c, RestaurantJPTableClass table){
			s = state.pending;
			w = wait;
			choice = c;
		}
	};
	
	class MyMarket{
		MarketManagerRole m;
		boolean Steak = true;
		boolean Chicken = true;
		boolean Pizza = true;
		boolean Salad = true;
		public MyMarket(MarketManagerRole mark){
			m = mark;
		}
		public boolean isStocked(String f){
			if(f.equals("Pizza"))
				return Pizza;
			if(f.equals("Steak"))
				return Steak;
			if(f.equals("Chicken"))
				return Chicken;
			if(f.equals("Salad"))
				return Salad;
			return false;
		}
		public void setUnstocked(String f){
			if(f.equals("Pizza"))
				Pizza = false;
			if(f.equals("Steak"))
				Steak = false;
			if(f.equals("Chicken"))
				Chicken = false;
			if(f.equals("Salad"))
				Salad = false;
		}
	}
	public class Food {
		int cookingTime;
		int inventory;
		FOOD_ITEMS type;
		public Food(int cTime, int count, FOOD_ITEMS t){
			cookingTime = cTime;
			inventory = count;
			type = t;
		}
		public Food(){
		}
	}
}


