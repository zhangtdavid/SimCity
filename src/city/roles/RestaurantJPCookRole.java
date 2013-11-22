package city.roles;

import utilities.RestaurantJPRevolvingStand;
import utilities.RestaurantJPTableClass;
import utilities.RestaurantZhangOrder;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.Role;
import city.animations.RestaurantJPCookAnimation;
import city.animations.RestaurantJPHostAnimation;
import city.interfaces.MarketManager;
import city.interfaces.RestaurantJPCook;
import city.interfaces.RestaurantJPWaiter;

public class RestaurantJPCookRole extends Role implements RestaurantJPCook {
															//DATA	
	private RestaurantJPCookAnimation gui;
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<MyMarket> Markets = Collections.synchronizedList(new ArrayList<MyMarket>());
	boolean ordering = false;
	private Semaphore atDestination = new Semaphore(0,true);
	
	RestaurantJPRevolvingStand revolvingStand;
	boolean waitingToCheckStand = false;
	
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
	String name;
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
	public enum state{pending, cooking, done, finished, taken};
	
	public RestaurantJPHostAnimation hostGui = null;
	Timer timer = new Timer();
	Map<String, Food> Foods = new HashMap<String, Food>();
	
	public class Food {
		int cookingTime;
		int inventory;
		String type;
		public Food(int cTime, int count, String t){
			cookingTime = cTime;
			inventory = count;
			type = t;
		}
		public Food(){
		}
	}
	
	int low = 2;
	int marketCount = 0;

	public RestaurantJPCookRole(RestaurantJPCashierRole c) {
		super();
		name = "squidward";
		Foods.put("Steak", new Food(5000, 5, "Steak"));
		Foods.put("Chicken", new Food(4000, 5, "Chicken"));
		Foods.put("Salad", new Food(3000, 5, "Salad"));
		Foods.put("Pizza", new Food(2000, 5, "Pizza"));					//HACKHACKHACK
	}
	public String getName() {
		return name;
	}
	
	public void setGui(RestaurantJPCookAnimation g){
		gui = g;
	}
	public void addMarket(MarketManagerRole m){
		Markets.add(new MyMarket(m));
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
	/*public void msgWaiterHasGoneOnBreak(WaiterAgent oldWaiter, WaiterAgent newWaiter){
		Do("Handling change in waiter");
		for(Order o : orders){
			if(o.w == oldWaiter)
				o.w = newWaiter;
		}
	}*/
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
		if(!ordering){
			for (Map.Entry<String, Food> entry : Foods.entrySet())
			{
				if(entry.getValue().inventory <= low){
					List<String> neededFoods = new ArrayList<String>();
					neededFoods.add(entry.getValue().type);
					for(MyMarket m : Markets){
						for(String food : neededFoods){
							if(m.isStocked(food)){
								//m.m.msgNeedFood(food, this);
								ordering = true;
								return true;
							}
						}
					}
				}
			}
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
			print("Waiting 5 seconds to check the stand");
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
		o.w.msgOrderIsReady(o.choice, o.table);
	}
	
	private void OrderFinished(Order o) {
		//Do("Order finished action");
		gui.DoOrderRemoved(o.choice);
		orders.remove(o);
	}
	public void setAnimation(RestaurantJPCookAnimation g) {
		gui = g;
		
	}
	
	public int getPosOfNewOrder() {
		return orders.size();
	}
}


