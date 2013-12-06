package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChoiOrder;
import utilities.RestaurantChoiRevolvingStand;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCook;
import city.bases.JobRole;
import city.bases.RestaurantBuilding.FoodOrderState;
import city.bases.interfaces.RestaurantBuildingInterface.Food;
import city.buildings.RestaurantChoiBuilding;
import city.buildings.interfaces.Market;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.RestaurantChoiCook;

public class RestaurantChoiCookRole extends JobRole implements RestaurantChoiCook {

	// Data
	
	private RestaurantChoiAnimatedCook cookGui;
	private int marketIndex=0;
	private Semaphore inProgress = new Semaphore(0, true);
	private RestaurantChoiRevolvingStand orderqueue;
	private boolean checkback;
	private RestaurantChoiBuilding building;
	private boolean wantsToLeave;
	private List<RestaurantChoiOrder> orders = Collections.synchronizedList(new ArrayList<RestaurantChoiOrder>());
	private Timer timer = new Timer(); // for cooking!
	private List<myMarket> markets = Collections.synchronizedList(new ArrayList<myMarket>()); // myMarket has a MarketBuilding AND goodies.
    private MarketCustomerDelivery marketCustomerDelivery;
    // private RestaurantChoiCashier cashier; // THIS IS REQUIRED FOR MARKETS.
    // private List<MyMarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MyMarketOrder>()); 
    
	// Constructor
    
	/**
	 * Initializes Cook for RestaurantChoi
	 * @param b : for RestaurantChoiBuilding
	 * @param t1 : Start of shift
	 * @param t2 : End of shift
	 */
	public RestaurantChoiCookRole(RestaurantChoiBuilding b, int t1, int t2) {
		super();
		building = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChoiBuilding.getWorkerSalary());
	}
	
	public RestaurantChoiCookRole(){ // to just test mechanics
		super(); 
	}
	
	// Messages
	
	@Override
	public void msgRelease() {
		inProgress.release();// = true;
		stateChanged();		
	}

	@Override
	public void msgHeresAnOrder(RestaurantChoiOrder or) {
		synchronized(orders){
			orders.add(or);
			or.setState(RestaurantChoiOrder.RECOGNIZED);
		}
		stateChanged();
	}

	@Override
	public void msgFoodsDone(RestaurantChoiOrder o) {
		o.setState(RestaurantChoiOrder.COOKED);
		stateChanged();
	}

	/*@Override
	public void msgOutOfThisFood(Market m, int choice) {
		synchronized(markets){
			for(int i = 0; i < markets.size(); i++){
				if(markets.get(i).equals(m)){
					markets.get(i).outOf[choice-1] = true; // set them as out of
				}
			}
		}
	}*/
	
	@Override
	public void msgFoodReceived(HashMap<FOOD_ITEMS, Integer> marketOrder, int id) {
        print("Cook received msgFoodReceived");
        //MyMarketOrder mo = findMarketOrder(id); //TODO THIS IS FOR MARKET INTEGRATION
        //removeMarketOrderFromList(mo);
        
        for (FOOD_ITEMS i: marketOrder.keySet()) {
            Food f = findFood(i.toString());
            f.setAmount(f.getAmount() + marketOrder.get(i));
            print("New Inventory: " + f.getItem() + " " + f.getAmount());
            f.setFoodOrderState(FoodOrderState.None);
        }
        stateChanged();
    }
	
	/*@Override
	public void msgFoodReceived(int choice, int amount, Market m) {
		synchronized(foods){
			foods.get(choice).amount+=amount;
			print("Received " + amount + " of item " + choice);
			foods.get(choice).amountOrdered = 0; // mark the order as none.
			synchronized(markets){
				if(amount < foods.get(choice).amountOrdered){ // if you don't get as many as you ordered
					for(int i = 0; i < markets.size(); i++){
						if(m.equals(markets.get(i))){
							markets.get(i).outOf[choice-1] = true; // set the market as out of the food you ordered
						}
					}
				}
			}
		}
	}*/

	@Override
	public void msgAtRefrigerator() {
		stateChanged();
	}

	@Override
	public void msgAtGrills() {
		stateChanged();
	}

	@Override
	public void msgAtPlatingArea() {
		stateChanged();
	} // these 3 functions didn't do anything in v2.2 anyways

	// Scheduler
	
	@Override
	public boolean runScheduler() {
		boolean blocking = false;
		// Role Scheduler
		if (marketCustomerDelivery != null && marketCustomerDelivery.getActive() && marketCustomerDelivery.getActivity()) {
			blocking  = true;
			boolean activity = marketCustomerDelivery.runScheduler();
			if (!activity) {
				marketCustomerDelivery.setActivityFinished();
			}
		}
		if(wantsToLeave && building.seatedCustomers == 0){
			wantsToLeave = false;
			super.setInactive();
		}
		synchronized(orders){ // take from grill to plates
			for(int i = 0; i < orders.size(); i ++){
				if(orders.get(i).getState() == RestaurantChoiOrder.COOKED){
					DoGoToGrills();
					cookGui.setOrderIcon(orders.get(i).getChoice());
					DoGoToPlates();
					MoveFoodToPlating(orders.get(i));
					cookGui.setOrderIcon(null);
					return true;
				}
			}
		}
		synchronized(orders){ // found ingredients, go to grill and cook
			for(int i = 0; i < orders.size(); i ++){
				if(orders.get(i).getState() == RestaurantChoiOrder.TO_COOK){
					cookGui.setOrderIcon(orders.get(i).getChoice());
					DoGoToGrills();
					CookOrder(orders.get(i));
					cookGui.setOrderIcon(null);
					return true;
				}
			}
		}
		synchronized(orders){ // received an order from waiter and check refrigerator if can cook
			for(int i = 0; i < orders.size(); i ++){
				if(orders.get(i).getState() == RestaurantChoiOrder.RECOGNIZED){
					DoGoToRefrig();
					AnalyzeCookOrder(orders.get(i));
					return true;
				}
			}
		}
		if(!checkback){
			CheckBack();
			checkback = true;		
		}
		cookGui.DoLeave();
		return blocking;
	}
	
	// Actions
	
	private void MoveFoodToPlating(RestaurantChoiOrder o) {
		synchronized(orders){
			o.setState(RestaurantChoiOrder.READY_FOR_PICKUP);
		}
		o.getWaiter().msgOrderComplete(o); //send this to waiter!
		print("Moved plate to plating area. Told " + o.getWaiter().getName() + " that an item is done");
	}

	private boolean AnalyzeCookOrder(RestaurantChoiOrder o) {
		synchronized(orders){
			o.setState(RestaurantChoiOrder.CHECKING);
		}
		print("Checking if I have ingredients");
		//this is the internal food object corresponding to the choice in order (below).
		Food tempFood = building.getFoods().get(o.getChoice());
		if(tempFood.getAmount() <= tempFood.getLow()){
			if(tempFood.getFoodOrderState() == FoodOrderState.None){ // if i haven't already ordered the food
				//ask for food; use marketIndex, but it goes to infinity so take mod of markets.size();
				//and we ask for the difference between the low and the capacity.
				int outOfcounter = 0;
				while(markets.get(marketIndex%markets.size()).outOf.get(tempFood.getItem()) == true){ // true = Out of that food
					marketIndex++;
					outOfcounter++;
					if(outOfcounter == markets.size()){
						print("Out of choice " + tempFood + " permanently (All markets out)");
						return false;
					}
				}
				int t = tempFood.getCapacity()-tempFood.getLow();
				synchronized(markets){
					//markets.get(marketIndex%markets.size()).getMarket().msgHeresAnOrder(tempFood.item, tempFood.capacity-tempFood.low);
					//give a MarketOrder to a MarketBuilding // THIS IS A NON NORM (V2)
					//markets.get(marketIndex%markets.size()).getMarket().getCashier().msg  (tempFood.item, tempFood.capacity-tempFood.low);
					HashMap<FOOD_ITEMS, Integer> forOrder = new HashMap<FOOD_ITEMS, Integer>();
					forOrder.put(o.getChoice(), t);
					//MarketOrder mo = new MarketOrder(forOrder); // since this is direct: one order at a time, deal with it~
				}
				tempFood.setFoodOrderState(FoodOrderState.Pending);
				print("Asked Market " + marketIndex%markets.size() + " for " + t + " " + tempFood.getItem());
			}
		}
		if(tempFood.getAmount() == 0){ // if we happen to be at 0 of the stock...
			synchronized(orders){
				o.getWaiter().msgOutOfThisFood(o);
				orders.remove(o); // if we can't cook it, remove this obj and tell waiter.
			}
			return false;
		} // this is so we don't get to negative amount
		o.setState(RestaurantChoiOrder.TO_COOK);
		print("Can cook item");
		return true;
	}

	private boolean CookOrder(RestaurantChoiOrder o) {
		synchronized(orders){
			o.setState(RestaurantChoiOrder.COOKING);
		}
		//this is the internal food object corresponding to the choice in order (below).
		Food tempFood = building.getFoods().get(o.getChoice());
		tempFood.setAmount(tempFood.getAmount() - 1);
		print("Cooking now: "+ tempFood.getAmount() + " of item " + o.getChoice() +" left");
		final RestaurantChoiOrder finalO = o;
		if(o.getChoice() == FOOD_ITEMS.steak){
			timer.schedule(new TimerTask() {
				public void run() {
					msgFoodsDone(finalO);
				}
			}, tempFood.getCookingTime());
		}else if(o.getChoice() == FOOD_ITEMS.pizza){
			timer.schedule(new TimerTask() {
				public void run() {
					msgFoodsDone(finalO);
				}
			}, tempFood.getCookingTime());
		}else if(o.getChoice() == FOOD_ITEMS.chicken){
			
			timer.schedule(new TimerTask() {
				public void run() {      
					msgFoodsDone(finalO);
				}
			}, tempFood.getCookingTime());
		}else if(o.getChoice() == FOOD_ITEMS.salad){
			timer.schedule(new TimerTask() {
				public void run() {
					msgFoodsDone(finalO);
				}
			}, tempFood.getCookingTime());
		}
		return true; // <<why? lol
	}

	private void DoGoToRefrig() {
		cookGui.setAcquired();
		cookGui.toRef();
		try {
			inProgress.acquire();
		} catch (InterruptedException e) {
			// not supported yet
			e.printStackTrace();
		}
		stateChanged();
	}

	private void DoGoToGrills() {
		cookGui.setAcquired();
		cookGui.toGrill();
		try {
			inProgress.acquire();
		} catch (InterruptedException e) {
			// not supported yet
			e.printStackTrace();
		}
		stateChanged();
	}

	private void DoGoToPlates() {
		cookGui.setAcquired();
		cookGui.toPlate();
		try {
			inProgress.acquire();
		} catch (InterruptedException e) {
			// not supported yet
			e.printStackTrace();
		}
		stateChanged();
	}

	// Getters	
	
	@Override
	public RestaurantChoiAnimatedCook getGui() {
		return cookGui;
	}
	
	@Override
	public RestaurantChoiRevolvingStand getRevolvingStand() {
		return orderqueue;
	}
	
	@Override
	public List<RestaurantChoiOrder> getOrders() {
		return orders;
	}

	// Setters
	
	@Override
	public void setGui(RestaurantChoiAnimatedCook gui) {
		cookGui = gui;	
	}
	
	@Override
	public void setRevolvingStand(RestaurantChoiRevolvingStand in){
		orderqueue = in;
	}
	
	@Override
	public void setInactive(){
		if(orders.isEmpty() && building.seatedCustomers == 0)
			super.setInactive();
		else wantsToLeave = true;
	}

	@Override
	public void addMarket(Market m) {
		myMarket mm = new myMarket(m);
		synchronized(markets){
			markets.add(mm);            
			mm.outOf.put("Steak", false);
			mm.outOf.put("Pizza", false);
			mm.outOf.put("Chicken", false);
			mm.outOf.put("Salad", false);
		}		
	}
	
	// Utilities
	
	@Override
	public void hackNoFood() {
		building.setFoodQuantity(FOOD_ITEMS.chicken, 0);
		building.setFoodQuantity(FOOD_ITEMS.pizza, 0);
		building.setFoodQuantity(FOOD_ITEMS.salad, 0);
		building.setFoodQuantity(FOOD_ITEMS.steak, 0);
	}
	
	/* TODO THIS IS FOR MARKET INTEGRATION
    public MyMarketOrder findMarketOrder(int id) {
        for(MyMarketOrder o: marketOrders){
            if(o.order.orderId == id) {
                return o;
            }
        }
        return null;
    }
    public void removeMarketOrderFromList(MyMarketOrder order) {
        for(int i = 0; i < marketOrders.size(); i++) {
            if(marketOrders.get(i) == order) {
                marketOrders.remove(order);
            }
        }
    }*/
	
    private Food findFood(String choice) {
        for(Food f: building.getFoods().values()){
            if(f.getItem().equals(choice)) {
                return f;
            }
        }
        return null;
    }
    
	@Override
	public void CheckBack() { // check every 5000 seconds
		if(orderqueue != null){
			timer.schedule(new TimerTask() {
				public void run() {
					synchronized(orderqueue){
						if(!orderqueue.isEmpty()){
							RestaurantChoiOrder o = orderqueue.poll();
							orders.add(o);
							DoGoToRefrig();
							AnalyzeCookOrder(o);
						}
					}
					//CheckBack();
					checkback = false;
					stateChanged();
				}
			}, 5000);
		}
	}
    
    @Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHOI, "RestaurantChoiCookRole " + this.getPerson().getName(), msg);
    }
    
	// Classes
    
    /*     // TODO THIS IS FOR MARKET INTEGRATION
	   private class MyMarketOrder{
	    	MarketOrder order;
	        MarketOrderState s;
	        
	        public MyMarketOrder(MarketOrder o) {
	        	order = new MarketOrder(o);
	            s = MarketOrderState.Pending;
	        }
	    }
	    private enum MarketOrderState
	    {Pending, Ordered}; */
    
    
}
