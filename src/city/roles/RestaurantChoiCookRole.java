package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import utilities.RestaurantChoiOrder;
import utilities.RestaurantChoiRevolvingStand;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.animations.interfaces.RestaurantChoiAnimatedCook;
import city.interfaces.RestaurantChoiCook;
import city.buildings.RestaurantChoiBuilding;

public class RestaurantChoiCookRole extends Role implements RestaurantChoiCook {

	//Data
	RestaurantChoiAnimatedCook cookGui;
	public int marketIndex=0;
	Semaphore inProgress = new Semaphore(0, true);
	String name = "LeChef";
	RestaurantChoiRevolvingStand orderqueue;
	boolean checkback;
	RestaurantChoiBuilding building;
	boolean wantsToLeave;
	public List<RestaurantChoiOrder> orders = Collections.synchronizedList(new ArrayList<RestaurantChoiOrder>());
	Timer timer = new Timer(); // for cooking!
	public ConcurrentHashMap <FOOD_ITEMS, FoodData> foods = new ConcurrentHashMap<FOOD_ITEMS, FoodData>(); // how much of each food cook has
	public List<myMarket> markets = Collections.synchronizedList(new ArrayList<myMarket>());

	//Constructor
	/**
	 * Initializes Cook for RestaurantChoi
	 * @param b : for RestaurantChoiBuilding
	 * @param t1 : Start of shift
	 * @param t2 : End of shift
	 */
	public RestaurantChoiCookRole(RestaurantChoiBuilding b, int t1, int t2) {
		super();
		building = b;
		foods.put(FOOD_ITEMS.chicken, new FoodData(FOOD_ITEMS.chicken)); // foods = what the cook has
		foods.put(FOOD_ITEMS.pizza, new FoodData(FOOD_ITEMS.pizza));
		foods.put(FOOD_ITEMS.salad, new FoodData(FOOD_ITEMS.salad));
		foods.put(FOOD_ITEMS.steak, new FoodData(FOOD_ITEMS.steak)); 
		//ordering on initialization if needed
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChoiBuilding.getWorkerSalary());
	}
	
	public RestaurantChoiCookRole(){ // to just test mechanics
		super();
		foods.put(FOOD_ITEMS.chicken, new FoodData(FOOD_ITEMS.chicken)); // foods = what the cook has
		foods.put(FOOD_ITEMS.pizza, new FoodData(FOOD_ITEMS.pizza));
		foods.put(FOOD_ITEMS.salad, new FoodData(FOOD_ITEMS.salad));
		foods.put(FOOD_ITEMS.steak, new FoodData(FOOD_ITEMS.steak)); 
	}
	//Messages
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
	}

	@Override
	public void msgFoodReceived(int choice, int amount, Market m) {
		synchronized(foods){
			foods.get(choice).inventory+=amount;
			System.out.println("Received " + amount + " of item " + choice);
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
	}TODO*/

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

	//Scheduler
	@Override
	public boolean runScheduler() {
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
		}//if you have no orders in the queue, we check for orders in revolving stand (orderqueue).
		/*if(orderqueue != null){
			synchronized(orderqueue){ // TODO do i need this? will it present problems?
				if(orderqueue.peek() != null){
					DoGoToPlates(); // go to stand/plating
					DoGoToRefrig(); // graphically prepare yourself for AnalyzeCookOrder.
					RestaurantChoiOrder temp = orderqueue.poll(); // take&remove 1st in queue
					synchronized(orders){
						orders.add(temp); // add 1st in queue to INTERNAL orders list
					}
					AnalyzeCookOrder(temp); // analyze 1st in queue
					return true;
				}
			}
		}*/
		cookGui.DoLeave();
		if(!checkback){
			CheckBack();
			checkback = true;
		}
		return false;
	}
	//Actions
	@Override
	public void MoveFoodToPlating(RestaurantChoiOrder o) {
		synchronized(orders){
			o.setState(RestaurantChoiOrder.READY_FOR_PICKUP);
		}
		o.getWaiter().msgOrderComplete(o); //send this to waiter!
		System.out.println("Moved plate to plating area. Told " + o.getWaiter().getName() + " that an item is done");
	}

	@Override
	public void CheckBack() { // check every 5000 seconds
		System.out.println("checkback (REVOLVING STAND per 5000ticks)");
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

	public boolean AnalyzeCookOrder(RestaurantChoiOrder o) {
		synchronized(orders){
			o.setState(RestaurantChoiOrder.CHECKING);
		}
		System.out.println("Checking if ingredients");
		//this is the internal food object corresponding to the choice in order (below).
		FoodData tempFood = foods.get(o.getChoice());
		if(tempFood.inventory <= tempFood.threshold){
			if(tempFood.amountOrdered == 0){ // if i haven't already ordered the food
				//ask for food; use marketIndex, but it goes to infinity so take mod of markets.size();
				//and we ask for the difference between the threshold and the capacity.
				int outOfcounter = 0;
				while(markets.get(marketIndex%markets.size()).outOf.get(tempFood.choiceID) == true){
					marketIndex++;
					outOfcounter++;
					if(outOfcounter == markets.size()){
						System.out.println("Out of choice " + tempFood + " permanently (All markets out)");
						return false;
					}
				}
				/*TODO synchronized(markets){
					markets.get(marketIndex%markets.size()).market.msgHeresAnOrder(tempFood.choiceID, tempFood.capacity-tempFood.threshold);
				}*/
				tempFood.amountOrdered = tempFood.capacity-tempFood.threshold;
				System.out.println("Asked Market " + marketIndex%markets.size() + " for choice #" + tempFood.choiceID);
			}
		}
		if(tempFood.inventory == 0){ // if we happen to be at 0 of the stock...
			synchronized(orders){
				o.getWaiter().msgOutOfThisFood(o);
				orders.remove(o); // if we can't cook it, remove this obj and tell waiter.
			}
			return false;
		} // this is so we don't get to negative inventory
		o.setState(RestaurantChoiOrder.TO_COOK);
		System.out.println("Can cook item");
		return true;
	}

	@Override
	public boolean CookOrder(RestaurantChoiOrder o) {
		synchronized(orders){
			o.setState(RestaurantChoiOrder.COOKING);
		}
		//this is the internal food object corresponding to the choice in order (below).
		FoodData tempFood = foods.get(o.getChoice());
		tempFood.inventory--;
		print("Cooking now: "+ tempFood.inventory + " of item " + o.getChoice() +" left");
		final RestaurantChoiOrder finalO = o;
		if(o.getChoice() == FOOD_ITEMS.steak){
			timer.schedule(new TimerTask() {
				public void run() {
					msgFoodsDone(finalO);
				}
			}, tempFood.cookingTime);
		}else if(o.getChoice() == FOOD_ITEMS.pizza){
			timer.schedule(new TimerTask() {
				public void run() {
					msgFoodsDone(finalO);
				}
			}, tempFood.cookingTime);
		}else if(o.getChoice() == FOOD_ITEMS.chicken){
			
			timer.schedule(new TimerTask() {
				public void run() {      
					msgFoodsDone(finalO);
				}
			}, tempFood.cookingTime);
		}else if(o.getChoice() == FOOD_ITEMS.salad){
			timer.schedule(new TimerTask() {
				public void run() {
					msgFoodsDone(finalO);
				}
			}, tempFood.cookingTime);
		}
		return true; // <<why? lol
	}

	@Override
	public void DoGoToRefrig() {
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

	@Override
	public void DoGoToGrills() {
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

	@Override
	public void DoGoToPlates() {
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

	//Getters	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public RestaurantChoiAnimatedCook getGui() {
		return cookGui;
	}
	//@Override
	public RestaurantChoiRevolvingStand getRevolvingStand() {
		return orderqueue;
	}

	//Setters
	@Override
	public void setGui(RestaurantChoiAnimatedCook gui) {
		cookGui = gui;	
	}
	
	public void setRevolvingStand(RestaurantChoiRevolvingStand in){
		orderqueue = in;
	}
	@Override
	public void setInactive(){
		if(orders.isEmpty() && building.seatedCustomers == 0)
			super.setInactive();
		else wantsToLeave = true;
	}


	/*@Override
	public void addMarket(Market m) {
		myMarket mm = new myMarket(m);
		synchronized(markets){
			markets.add(mm);            
		}		
	}TODO */	
	//Utilities
	public void hackNoFood(){
		foods.get(FOOD_ITEMS.chicken).inventory = 0;
		foods.get(FOOD_ITEMS.pizza).inventory = 0;
		foods.get(FOOD_ITEMS.salad).inventory = 0;
		foods.get(FOOD_ITEMS.steak).inventory = 0;
	}
}
