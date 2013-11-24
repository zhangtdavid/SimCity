package city.roles;

import java.util.*;
import java.util.concurrent.Semaphore;

import utilities.MarketOrder;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.animations.RestaurantChungCookAnimation;
import city.animations.interfaces.RestaurantChungAnimatedCook;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChungBuilding;
import city.buildings.RestaurantChungBuilding.Food;
import city.buildings.RestaurantChungBuilding.FoodOrderState;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCook;
import city.interfaces.RestaurantChungWaiterBase;

/**
 * Restaurant Cook Agent
 */
// A Cook fulfills the customers' food orders as communicated by the waiters
// and maintains the restaurant's food inventory
public class RestaurantChungCookRole extends Role implements RestaurantChungCook {        
	RestaurantChungBuilding restaurant;
	
	Timer timer = new Timer();
    Timer timer2 = new Timer();
    private RestaurantChungAnimatedCook cookGui = null;
    
    private boolean cooking = false;
    private boolean plating = false;
    
	private Semaphore atCookHome = new Semaphore(0, true);
	private Semaphore atGrill = new Semaphore(0, true);
	private Semaphore atPlating = new Semaphore(0, true);
	
	private MarketCustomerDelivery marketCustomerDelivery;
	private RestaurantChungCashier restaurantChungCashier;

//  Orders
//  =====================================================================        
    public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>()); // Holds orders, their states, and recipients
    private class Order {
    	RestaurantChungWaiterBase w;
        String choice;
        int table;
        OrderState s;
        
        public Order(RestaurantChungWaiterBase w2, String selection, int tableNum, OrderState state) {
                w = w2;
                choice = selection;
                table = tableNum;
                s = state;
        }
    }
    
    private enum OrderState
    {Pending, Cooking, Cancelled, DoneCooking, Plating, DonePlating};
        
//  Markets
//  =====================================================================
    private List<MarketBuilding> markets = Collections.synchronizedList(new ArrayList<MarketBuilding>());
    int currentMarket = 0; // Index of market in the list to order from
        
//  Market Orders
//  =====================================================================        
    public List<MyMarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MyMarketOrder>()); 
    
    private class MyMarketOrder{
    	MarketOrder order;
        MarketOrderState s;
        
        public MyMarketOrder(MarketOrder o) {
        	order = new MarketOrder(o);
            s = MarketOrderState.Pending;
        }
    }
    private enum MarketOrderState
    {Pending, Ordered};

//  Constructor
//  =====================================================================                
    public RestaurantChungCookRole(RestaurantChungBuilding restaurant) {
        super();
        this.restaurant = restaurant;
    }
    
	public void setActive(){
		this.setActivityBegun();
	}
	
//  Messages
//	=====================================================================
//  Waiter
//  ---------------------------------------------------------------
    public void msgHereIsAnOrder(RestaurantChungWaiterBase w, String choice, int table) {
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

//  Market Delivery Person
//  ---------------------------------------------------------------
    public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> marketOrder, int id) {
        print("Cook received msgOrderIsReady");
        MyMarketOrder mo = findMarketOrder(id);
        removeMarketOrder(mo);
        
        for (FOOD_ITEMS i: marketOrder.keySet()) {
            Food f = findFood(i.toString());
            f.amount += marketOrder.get(i);
            print("New Inventory: " + f.item + " " + f.amount);
            f.s = FoodOrderState.None;
        }
        stateChanged();
    }
    
//    public void msgCannotFulfill(int id, Map<FOOD_ITEMS, Integer> unfulfilledItems) {
//        print("Cook received msgCannotFulfill");
////                MarketOrder mo = findMarketOrder(id);
//        Map<FOOD_ITEMS, Integer> tempItems = new HashMap<FOOD_ITEMS, Integer>();
//        
//        for (FOOD_ITEMS i: foods.keySet()) {
//            tempItems.put(i, 0);
//            if (unfulfilledItems.get(i) > 0) {
//                foods.get(i).s = FoodOrderState.None;
//                tempItems.put(i, unfulfilledItems.get(i));
//            }
////                        mo.orderItems.put(i,mo.orderItems.get(i)-unavailableItems.get(i)); // Update original order to reflect changes
//        }
//        
//        marketOrders.add(new MarketOrder(orderID++, tempItems, MarketOrderState.Pending)); // Create a new order for the unavailable items
//        stateChanged();
//    }

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
	public boolean runScheduler() {
		boolean blocking = false;
		if (!cooking && !plating) {
    		// Role Scheduler
    		if (marketCustomerDelivery != null && marketCustomerDelivery.getActive() && marketCustomerDelivery.getActivity()) {
    			blocking  = true;
    			boolean activity = marketCustomerDelivery.runScheduler();
    			if (!activity) {
    				marketCustomerDelivery.setActivityFinished();
    			}
    		}
    		
            synchronized(marketOrders) {
                for (MyMarketOrder o: marketOrders) {
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
            
//            synchronized(marketOrders) {
//                for (MyMarketOrder o: marketOrders) {
//                    if (o.s == MarketOrderState.Delivered) {
//                        removeMarketOrder(o);
//                        return true;
//                    }
//                }
//            }

            //we have tried all our rules and found
            //nothing to do. So return false to main loop of abstract agent
            //and wait.
    	}
        return blocking;
    }

//  Actions
//  =====================================================================
//  Market Orders
//  ---------------------------------------------------------------
    private void identifyFoodThatIsLow() {
        print("Identifying food that is low");                
        Map<FOOD_ITEMS, Integer> normal = new HashMap<FOOD_ITEMS, Integer>();                
        int numLow = 0;
        
        for (FOOD_ITEMS i: restaurant.foods.keySet()) {
            if (restaurant.foods.get(i).s == FoodOrderState.None && (restaurant.foods.get(i).amount <= restaurant.foods.get(i).low)) {
                normal.put(i, restaurant.foods.get(i).capacity - restaurant.foods.get(i).amount);
                restaurant.foods.get(i).s = FoodOrderState.Pending;
                numLow++;
            }
            else normal.put(i, 0);
        }

            
        if (numLow > 0) marketOrders.add(new MyMarketOrder(new MarketOrder(normal)));
        
        msgSelfLowFoodsIdentified();
            
//                for (int i = 0; i < marketOrders.size(); i++) {
//                        print("MARKET ORDER STATE: " + marketOrders.get(i).s.toString());
//                }
    }
    
    private void orderFoodThatIsLow(MyMarketOrder o) {
        print("Ordering food that is low");
        o.s = MarketOrderState.Ordered;
        
        for (FOOD_ITEMS i: o.order.orderItems.keySet()) {
            if (o.order.orderItems.get(i) > 0) {
                Food f = findFood(i.toString());
                f.s = FoodOrderState.Ordered;
            }
        }

        for (FOOD_ITEMS i: restaurant.foods.keySet()) {
            print("Current Inventory: " + i + " " + restaurant.foods.get(i).amount);
        }

        
        for (FOOD_ITEMS i: o.order.orderItems.keySet()) {
            print("Order: " + i + " " + o.order.orderItems.get(i));
        }
                
        MarketBuilding selectedMarket = markets.get((currentMarket++)%(markets.size()));  // TODO change this to a lookup of markets in city directory
        marketCustomerDelivery = new MarketCustomerDeliveryRole(o.order, restaurantChungCashier.getMarketCustomerDeliveryPayment());
        selectedMarket.manager.msgIWouldLikeToPlaceADeliveryOrder(marketCustomerDelivery, restaurantChungCashier.getMarketCustomerDeliveryPayment(), o.order.orderItems, o.order.orderId); // need to change this to a call to the manager, asking market manager for service
        return;
    }
    
    private void removeMarketOrder(MyMarketOrder o) {
        print("removing market order");
        removeMarketOrderFromList(o);
    }
        
//  Cooking
//  ---------------------------------------------------------------
    private void tryToCookIt(Order o) {
        Food f = restaurant.foods.get(o.choice);
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
        cookGui.DoGoToGrill(o.choice);
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
                Food f = restaurant.foods.get(o.choice);
                f.amount--;
//              print(o.choice + " amount after cooking " + f.amount);
                cooking = false;
                msgSelfDoneCooking(o);
                cookGui.DoReturnToCookHome();
//        		try {
//        			atCookHome.acquire();
//        		} catch (InterruptedException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
            }
        },
        restaurant.foods.get(o.choice).cookingTime*100);
    }
    
    private void plateIt(final Order o) {
        o.s = OrderState.Plating;
        cookGui.DoGoToPlating(o.choice);
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
            	RestaurantChungWaiterBase waiter = findWaiter(o); // Determines the waiter associated with the order
                waiter.msgOrderIsReady(o.choice, o.table);
                plating = false;
                msgSelfDonePlating(o);
                cookGui.DoReturnToCookHome();
//        		try {
//        			atCookHome.acquire();
//        		} catch (InterruptedException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
            }
        },
        500);
    }
    
    private void removeOrder(Order o) {
        print("removing order");
        removeOrderFromList(o);
    }

    private void informWaiterOfCancellation(Order o) {
        RestaurantChungWaiterBase w = findWaiter(o);
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
    
    public void addMarket(MarketBuilding m) {
        markets.add(m);
    }
    
    public RestaurantChungWaiterBase findWaiter(Order order) {
        for(Order o : orders){
            if(o == order) {
                return o.w;
            }
        }
        return null;
    }
    
    public Food findFood(String choice) {
        for(Food f: restaurant.foods.values()){
            if(f.item.equals(choice)) {
                return f;
            }
        }
        return null;
    }
    
    public MyMarketOrder findMarketOrder(int id) {
        for(MyMarketOrder o: marketOrders){
            if(o.order.orderId == id) {
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
        
    public void removeMarketOrderFromList(MyMarketOrder order) {
        for(int i = 0; i < marketOrders.size(); i++) {
            if(marketOrders.get(i) == order) {
                marketOrders.remove(order);
            }
        }
    }
    
    public void setRestaurantCashier(RestaurantChungCashier c) {
    	restaurantChungCashier = c;
    }
    
    // HACK------------------------------------------------------------------
    public void depleteSalad() {
        Food f = findFood("Salad");
        f.amount = 0;
    }
    // END HACK------------------------------------------------------------------
}