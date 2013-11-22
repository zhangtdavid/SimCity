package city.roles;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.Role;
import city.interfaces.RestaurantChungCook;
import city.interfaces.RestaurantChungMarket;

/**
 * Restaurant Cook Agent
 */
// A Cook fulfills the customers' food orders as communicated by the waiters
// and maintains the restaurant's food inventory
public class RestaurantChungCookRole extends Role implements RestaurantChungCook {        
    Timer timer = new Timer();
    Timer timer2 = new Timer();
    private RestaurantChungCookAnimation cookGui = null;
    
    private boolean cooking = false;
    private boolean plating = false;
    
	private Semaphore atCookHome = new Semaphore(0, true);
	private Semaphore atGrill = new Semaphore(0, true);
	private Semaphore atPlating = new Semaphore(0, true);

//  Orders
//  =====================================================================        
    public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>()); // Holds orders, their states, and recipients
    private class Order {
    	RestaurantChungWaiterRoleBase w;
        String choice;
        int table;
        OrderState s;
        
        public Order(RestaurantChungWaiterRoleBase w2, String selection, int tableNum, OrderState state) {
                w = w2;
                choice = selection;
                table = tableNum;
                s = state;
        }
    }
    
    private enum OrderState
    {Pending, Cooking, Cancelled, DoneCooking, Plating, DonePlating};
    
//  Food
//  =====================================================================
    private Map<String, Food> foods = new HashMap<String, Food>();
    private class Food {
        String item;
        int cookingTime;
        int amount;
        int low;
        int capacity;
        boolean open;
        FoodOrderState s;
        
        public Food(String item, int cookingTime, int amount, int low, int capacity) {
            this.item = item;
            this.cookingTime = cookingTime;
            this.amount = amount;
            this.low = low;
            open = false;
            this.capacity = capacity;
            s = FoodOrderState.None;
        }
    }
    private enum FoodOrderState
    {None, Pending, Ordered};
        
//  Markets
//  =====================================================================
    private List<RestaurantChungMarket> markets = Collections.synchronizedList(new ArrayList<RestaurantChungMarket>());
    int currentMarket = 0; // Index of market in the list to order from
        
//  Market Orders
//  =====================================================================        
    public List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>()); // Holds orders, their states, and recipients
    int orderID = 0; // Begins at 0, is incremented with each new order
    
    private class MarketOrder{
        int ID; // Used to quickly identify a fulfilled order from a market
        boolean rush;
        Map<String, Integer> orderItems = new HashMap<String, Integer>(); // Map of the type and quantity of items ordered, always includes all items, but can have a quantity of 0
        MarketOrderState s;
        
        public MarketOrder(int id, boolean rush, Map<String, Integer> items, MarketOrderState status) {
            ID = id;
            this.rush = rush;
            for (String s: items.keySet()) {
                orderItems.put(s, items.get(s)); // Create a deep copy of the map
            }
            
            s = status;
        }
    }
    private enum MarketOrderState
    {Pending, Ordered, Delivered};

//  Constructor
//  =====================================================================                
    public RestaurantChungCookRole() {
        super();
        // Add items and their cooking times to a map
        foods.put("Steak", new Food("Steak", 20, 0, 5, 10));
        foods.put("Chicken", new Food("Chicken", 10, 0, 5, 10));
        foods.put("Salad", new Food("Salad", 5, 0, 5, 10));
        foods.put("Pizza", new Food("Pizza", 15, 0, 5, 10));
    }

//  Messages
//  =====================================================================
    public void openRestaurant() {
        print("Cook received restaurant opened");
        for (Food f: foods.values()) {
                f.open = true;
        }
        stateChanged();
    }
        
//  Waiter
//  ---------------------------------------------------------------
    public void msgHereIsAnOrder(RestaurantChungWaiterRoleBase w, String choice, int table) {
        print("Cook received msgHereIsAnOrder");
        orders.add(new Order(w, choice, table, OrderState.Pending));
        stateChanged();
    }
        
//  Self
//  ---------------------------------------------------------------        
    public void msgSelfLowFoodsIdentified() {
        stateChanged();
    }
    
    public void msgSelfDoneCooking(Order o) {
        o.s = OrderState.DoneCooking;
        print("DONE COOKING");
        stateChanged();
    }
    
    public void msgSelfDonePlating(Order o) {
        o.s = OrderState.DonePlating;
        print("DONE PLATING");
       stateChanged();
    }

//  Market
//  ---------------------------------------------------------------
    public void msgOrderIsReady(int id, Map<String, Integer> marketOrder) {
        print("Cook received msgOrderIsReady");
        MarketOrder mo = findMarketOrder(id);
        mo.s = MarketOrderState.Delivered;
        
        for (String i: marketOrder.keySet()) {
            Food f = findFood(i);
            f.amount += marketOrder.get(i);
            print("New Inventory: " + f.item + " " + f.amount);
            f.s = FoodOrderState.None;
        }
        stateChanged();
    }
    
    public void msgCannotFulfill(int id, Map<String, Integer> unfulfilledItems) {
        print("Cook received msgCannotFulfill");
//                MarketOrder mo = findMarketOrder(id);
        Map<String, Integer> tempItems = new HashMap<String, Integer>();
        
        for (String i: foods.keySet()) {
            tempItems.put(i, 0);
            if (unfulfilledItems.get(i) > 0) {
                foods.get(i).s = FoodOrderState.None;
                tempItems.put(i, unfulfilledItems.get(i));
            }
//                        mo.orderItems.put(i,mo.orderItems.get(i)-unavailableItems.get(i)); // Update original order to reflect changes
        }
        
        marketOrders.add(new MarketOrder(orderID++, false, tempItems, MarketOrderState.Pending)); // Create a new order for the unavailable items
        stateChanged();
    }

//	Animation
//	---------------------------------------------------------------
	public void msgAnimationAtCookHome() {
		print("Cook at Cook Home");
		atCookHome.release();
		stateChanged();
	}
	
	public void msgAnimationAtGrill() {
		print("Cook at Grill");
		atGrill.release();
		stateChanged();
	}
	
	public void msgAnimationAtPlating() {
		print("Cook at Plating");
		atPlating.release();
		stateChanged();
	}
    
//  Scheduler
//  =====================================================================
    /**
     * Scheduler.  Determine what action is called for, and do it.
     */
    protected boolean pickAndExecuteAnAction() {
//                print("in scheduler");
    	if (!cooking && !plating) {
            for (Food f: foods.values()) {
                if (f.open) {
//                                    print("in open");
                    identifyFoodThatIsLow();
                    return true;
                }
            }
            
//                    print("in scheduler");
            synchronized(marketOrders) {
                for (MarketOrder o: marketOrders) {
                    if (o.s == MarketOrderState.Pending) {
                        orderFoodThatIsLow(o);
                        return true;
                    }
                }
            }
            
            if (orders.size() == 0) return true; // Solved an issue I encountered, can't remember exactly?
            
            synchronized(orders) {
                for (Order o : orders) {
                    if (o.s == OrderState.Cancelled) {
                        informWaiterOfCancellation(o);
                        removeOrder(o);
                        return true;
                    }
                }
            }
            
            synchronized(orders) {
                for (Order o : orders) {
                    if (o.s == OrderState.Pending) {
                        tryToCookIt(o);
                        return true;
                    }
                }
            }
            
            synchronized(orders) {
                for (Order o : orders) {
                    if (o.s == OrderState.DoneCooking) {
                        plateIt(o);
                        return true;
                    }
                }
            }


            synchronized(orders) {
                for (Order o : orders) {
                    if (o.s == OrderState.DonePlating) {
                        removeOrder(o);
                        return true;
                    }
                }
            }
            
            synchronized(marketOrders) {
                for (MarketOrder o: marketOrders) {
                    if (o.s == MarketOrderState.Delivered) {
                        removeMarketOrder(o);
                        return true;
                    }
                }
            }

            //we have tried all our rules and found
            //nothing to do. So return false to main loop of abstract agent
            //and wait.
    	}
        return false;
    }

//  Actions
//  =====================================================================
//  Market Orders
//  ---------------------------------------------------------------
    private void identifyFoodThatIsLow() {
        print("Identifying food that is low");                
        Map<String, Integer> normal = new HashMap<String, Integer>();                
        Map<String, Integer> rush = new HashMap<String, Integer>();
        int numLow = 0;
        int numRush = 0;
        
        for (String i: foods.keySet()) {
            if (foods.get(i).open == false) {
//                                print("here 2");

                if (foods.get(i).s == FoodOrderState.None && (foods.get(i).amount <= foods.get(i).low)) {
                    normal.put(i, foods.get(i).capacity - foods.get(i).amount);
                    foods.get(i).s = FoodOrderState.Pending;
                    numLow++;
                }
                else normal.put(i, 0);
            }
            
            else {
                rush.put(i, foods.get(i).capacity - foods.get(i).amount);
                foods.get(i).s = FoodOrderState.Pending;
                foods.get(i).open = false;
                numRush++;
            }
        }
            
//                for (String i: normal.keySet()) {
//                        print("Normal: " + i + " " + normal.get(i));
//                }
//                for (String i: rush.keySet()) {
//                        print("Rush Order: " + i + " " + rush.get(i));
//                }

            
        if (numLow > 0) marketOrders.add(new MarketOrder(orderID++, false, normal, MarketOrderState.Pending));
        if (numRush > 0) marketOrders.add(new MarketOrder(orderID++, true, rush, MarketOrderState.Pending));
        
        msgSelfLowFoodsIdentified();
            
//                for (int i = 0; i < marketOrders.size(); i++) {
//                        print("MARKET ORDER STATE: " + marketOrders.get(i).s.toString());
//                }
    }
    
    private void orderFoodThatIsLow(MarketOrder o) {
        print("Ordering food that is low");
        o.s = MarketOrderState.Ordered;
        
        for (String i: o.orderItems.keySet()) {
            if (o.orderItems.get(i) > 0) {
                Food f = findFood(i);
                f.s = FoodOrderState.Ordered;
            }
        }

        for (String i: foods.keySet()) {
            print("Current Inventory: " + i + " " + foods.get(i).amount);
        }

        
        for (String i: o.orderItems.keySet()) {
            print("Order: " + i + " " + o.orderItems.get(i));
        }
                
        RestaurantChungMarket selectedMarket = markets.get((currentMarket++)%(markets.size())); // Cycles through the available markets
        selectedMarket.msgHereIsAnOrder(this, o.ID, o.rush, o.orderItems);
        return;
    }
    
    private void removeMarketOrder(MarketOrder o) {
        print("removing market order");
        removeMarketOrderFromList(o);
    }
        
//  Cooking
//  ---------------------------------------------------------------
    private void tryToCookIt(Order o) {
        Food f = foods.get(o.choice);
        // If out of food
        identifyFoodThatIsLow();

        if (f.amount == 0) {
                // sets all orders of the same item to cancelled
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).choice == o.choice) {
                    orders.get(i).s = OrderState.Cancelled;
                }
            }
            return;
        }

        cookIt(o);
    }
    
    private void cookIt(final Order o) {
        o.s = OrderState.Cooking;
//        cookGui.DoGoToGrill(o.choice);
//        print(Integer.toString(atGrill.availablePermits()));
//		try {
//			atGrill.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        DoCookIt();
        cooking = true;
        timer.schedule(new TimerTask() {
            public void run() {
                Food f = foods.get(o.choice);
                f.amount--;
//              print(o.choice + " amount after cooking " + f.amount);
                cooking = false;
                msgSelfDoneCooking(o);
//                cookGui.DoReturnToCookHome();
//        		try {
//        			atCookHome.acquire();
//        		} catch (InterruptedException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
            }
        },
        foods.get(o.choice).cookingTime*100);
    }
    
    private void plateIt(final Order o) {
        o.s = OrderState.Plating;
//        cookGui.DoGoToPlating(o.choice);
//		try {
//			atPlating.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		DoPlateIt();
        plating = true;
        timer2.schedule(new TimerTask() {
            public void run() {
//            	print("in plating timer");
                RestaurantChungWaiterRoleBase waiter = findWaiter(o); // Determines the waiter associated with the order
                waiter.msgOrderIsReady(o.choice, o.table);
                plating = false;
                msgSelfDonePlating(o);
//                cookGui.DoReturnToCookHome();
//        		try {
//        			atCookHome.acquire();
//        		} catch (InterruptedException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
            }
        },
        500);
//        print("after plating timer");
    }
    
    private void removeOrder(Order o) {
        print("removing order");
        removeOrderFromList(o);
    }

    private void informWaiterOfCancellation(Order o) {
        RestaurantChungWaiterRoleBase w = findWaiter(o);
        w.msgOutOfItem(o.choice, o.table);
    }

//  Animations
//        =====================================================================
    // The animation DoXYZ() routines
    private void DoCookIt() {
        print("Cooking");
    }
    
    private void DoPlateIt() {
        print("Plating");
    }

//  Utilities
//  =====================================================================        
    public void setGui(RestaurantChungCookAnimation gui) {
        cookGui = gui;
    }
    
    public void addMarket(RestaurantChungMarket m) {
        markets.add(m);
    }
    
    public RestaurantChungWaiterRoleBase findWaiter(Order order) {
        for(Order o : orders){
            if(o == order) {
                return o.w;                
            }
        }
        return null;
    }
    
    public Food findFood(String choice) {
        for(Food f: foods.values()){
            if(f.item.equals(choice)) {
                return f;
            }
        }
        return null;
    }
    
    public MarketOrder findMarketOrder(int id) {
        for(MarketOrder o: marketOrders){
            if(o.ID == id) {
                return o;
            }
        }
        return null;
    }
    
    public void removeOrderFromList(Order order) {
        for(int i = 0; i < orders.size(); i++) {
            if(orders.get(i) == order) {
                orders.remove(order);
            }
        }
    }
        
    public void removeMarketOrderFromList(MarketOrder order) {
        for(int i = 0; i < marketOrders.size(); i++) {
            if(marketOrders.get(i) == order) {
                marketOrders.remove(order);
            }
        }
    }
    
    // HACK------------------------------------------------------------------
    public void depleteSalad() {
        Food f = findFood("Salad");
        f.amount = 0;
    }
    // END HACK------------------------------------------------------------------
}