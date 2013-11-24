package city.roles;

import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import utilities.RestaurantChoiOrder;
import utilities.RestaurantChoiRevolvingStand;
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
		foods.put(1, new Food(1));
		foods.put(2, new Food(2));
		foods.put(3, new Food(3));
		foods.put(4, new Food(4));
		//ordering on initialization if needed
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChoiBuilding.getWorkerSalary());
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
			active = false;
		}
		synchronized(orders){ // take from grill to plates
			for(int i = 0; i < orders.size(); i ++){
				if(orders.get(i).getState() == RestaurantChoiOrder.COOKED){
					DoGoToGrills();
					cookGui.setOrderIcon(orders.get(i).getChoice());
					DoGoToPlates();
					MoveFoodToPlating(orders.get(i));
					cookGui.setOrderIcon(-1);
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
					cookGui.setOrderIcon(-1);
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
		}//if you have no orders in the queue, we check for orders in queue.
		if(orderqueue != null){
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
		}
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
	public void CheckBack() { // check every 2000 seconds
		System.out.println("checkback");
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

	public boolean AnalyzeCookOrder(RestaurantChoiOrder o) {
		synchronized(orders){
			o.setState(RestaurantChoiOrder.CHECKING);
		}
		System.out.println("Checking if ingredients");
		//this is the internal food object corresponding to the choice in order (below).
		Food tempFood = foods.get(o.getChoice());

		if(tempFood.inventory <= tempFood.threshold){
			if(tempFood.amountOrdered == 0){ // if i haven't already ordered the food
				//ask for food; use marketIndex, but it goes to infinity so take mod of markets.size();
				//and we ask for the difference between the threshold and the capacity.
				int outOfcounter = 0;
				while(markets.get(marketIndex%markets.size()).outOf[tempFood.choiceID-1]){
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
		Food tempFood = foods.get(o.getChoice());
		tempFood.inventory--;
		print("Cooking now: "+ tempFood.inventory + " of item " + o.getChoice() +" left");
		final RestaurantChoiOrder finalO = o;
		switch (o.getChoice()) {
		case 1:
			timer.schedule(new TimerTask() {
				public void run() {
					msgFoodsDone(finalO);
				}
			}, tempFood.cookingTime);
			break;
		case 2:
			timer.schedule(new TimerTask() {
				public void run() {
					msgFoodsDone(finalO);
				}
			}, tempFood.cookingTime);
			break;
		case 3:
			timer.schedule(new TimerTask() {
				public void run() {      
					msgFoodsDone(finalO);
				}
			}, tempFood.cookingTime);
			break;
		case 4:
			timer.schedule(new TimerTask() {
				public void run() {
					msgFoodsDone(finalO);
				}
			}, tempFood.cookingTime);
			break;
		}
		return true;
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
			active = false;
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
		foods.get(1).inventory = 0;
		foods.get(2).inventory = 0;
		foods.get(3).inventory = 0;
		foods.get(4).inventory = 0;
	}
}
