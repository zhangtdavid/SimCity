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
import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import utilities.RestaurantChungOrder;
import utilities.RestaurantChungOrder.OrderState;
import utilities.RestaurantChungRevolvingStand;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.abstracts.RestaurantBuildingBase.FoodOrderState;
import city.abstracts.RestaurantBuildingInterface.Food;
import city.animations.interfaces.RestaurantChungAnimatedCook;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChungBuilding;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.RestaurantChung;
import city.interfaces.RestaurantChungCook;
import city.interfaces.RestaurantChungWaiter;

public class RestaurantChungCookRole extends Role implements RestaurantChungCook {
//  Data
//  ===================================================================== 
	public EventLog log = new EventLog();
	Timer timer = new Timer();
    Timer timer2 = new Timer();
    
	private Semaphore atCookHome = new Semaphore(0, true);
	private Semaphore atGrill = new Semaphore(0, true);
	private Semaphore atPlating = new Semaphore(0, true);

	private boolean cooking = false;
    private boolean plating = false;
	boolean waitingToCheckStand = false;
	
	RestaurantChung restaurant;

	RestaurantChungRevolvingStand orderStand;

    private List<RestaurantChungOrder> orders = Collections.synchronizedList(new ArrayList<RestaurantChungOrder>()); // Holds orders, their states, and recipients

    private List<Role> marketCustomerDeliveryRoles = new ArrayList<Role>(); // List of each marketCustomerDelivery role that is created for each order to the market
    private List<MyMarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MyMarketOrder>()); // Holds MarketOrder and state

	WorkingState workingState = WorkingState.Working;
            
//  Constructor
//	=====================================================================
    public RestaurantChungCookRole(RestaurantChung b, int t1, int t2) {
        super();
		restaurant = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChungBuilding.WORKER_SALARY);
	}

//  Activity
//  ===================================================================== 
	@Override
	public void setInactive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================
//  Waiter
//  ---------------------------------------------------------------
	@Override
    public void msgHereIsAnOrder(RestaurantChungWaiter w, String choice, int table) {
        print("RestaurantChungCook received msgHereIsAnOrder from RestaurantChungWaiter");
		log.add(new LoggedEvent("RestaurantChungCook received msgHereIsAnOrder from RestaurantChungWaiter. For " + choice));
        orders.add(new RestaurantChungOrder(w, choice, table, OrderState.Pending));
        stateChanged();
    }
        
//  Self
//  ---------------------------------------------------------------        
	@Override
    public void msgSelfLowFoodsIdentified() {
        print("RestaurantChungCook received msgSelfLowFoodsIdentified");
        stateChanged();
    }
    
	@Override
    public void msgSelfDoneCooking(RestaurantChungOrder o) {
        o.s = OrderState.DoneCooking;
        print("RestaurantChungCook done cooking");
		log.add(new LoggedEvent("RestaurantChungCook received msgSelfDoneCooking."));
        stateChanged();
    }
    
	@Override
    public void msgSelfDonePlating(RestaurantChungOrder o) {
        o.s = OrderState.DonePlating;
        print("RestaurantChungCook done plating");
		log.add(new LoggedEvent("RestaurantChungCook received msgSelfDonePlating."));
        stateChanged();
    }

//  Market Delivery Person
//  ---------------------------------------------------------------
	@Override
    public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> marketOrder, int id) {
        print("RestaurantChungCook received msgHereIsOrderDelivery from MarketDeliveryPerson");
		log.add(new LoggedEvent("RestaurantChungCook received msgHereIsOrderDelivery from MarketDeliveryPerson."));
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
//		log.add(new LoggedEvent("Cook received msgHereIsOrderDelivery."));
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
	@Override
	public void msgAnimationAtCookHome() {
		print("RestaurantChungCook at Cook Home");
		log.add(new LoggedEvent("RestaurantChungCook received msgAnimationAtCookHome."));
		atCookHome.release();
		stateChanged();
	}
	
	@Override
	public void msgAnimationAtGrill() {
		print("RestaurantChungCook at Grill");
		log.add(new LoggedEvent("RestaurantChungCook received msgAnimationAtGrill."));
		atGrill.release();
		stateChanged();
	}
	
	@Override
	public void msgAnimationAtPlating() {
		print("RestaurantChungCook at Plating");
		log.add(new LoggedEvent("RestaurantChungCook received msgAnimationAtPlating."));
		atPlating.release();
		stateChanged();
	}
    
//  Scheduler
//  =====================================================================
    /**
     * Scheduler.  Determine what action is called for, and do it.
     */
	@Override
	public boolean runScheduler() {
		boolean blocking = false;
		if (!cooking && !plating) {
    		// Role Scheduler
			for (Role r : marketCustomerDeliveryRoles) if (r.getActive() && r.getActivity()) {
				blocking  = true;
				boolean activity = r.runScheduler();
				if (!activity) {
					r.setActivityFinished();
				}
				break;
			}
    		
    		if (workingState == WorkingState.GoingOffShift) {
    			if (restaurant.getRestaurantChungCashier() != this)
    				workingState = WorkingState.NotWorking;
    		}
    		
            synchronized(marketOrders) {
                for (MyMarketOrder o: marketOrders) {
                    if (o.s == MarketOrderState.Pending) {
                        orderFoodThatIsLow(o);
                        return true;
                    }
                }
            }
            
//            if (orders.size() == 0) return true; // Solved an issue I encountered, can't remember exactly?
            
            synchronized(orders) {
                for (RestaurantChungOrder o : orders) {
                    if (o.s == OrderState.Cancelled) {
                        informWaiterOfCancellation(o);
                        removeOrder(o);
                        return true;
                    }
                }
            }
            
			if(!waitingToCheckStand) {
				print("Waiting 5 seconds to check the stand");
				waitingToCheckStand = true;
				timer.schedule(new TimerTask() {
					public void run() {
						waitingToCheckStand = false;
						RestaurantChungOrder newOrder = orderStand.remove();
						if(newOrder != null) {
							print("Found an item on the stand");
							orders.add(newOrder);
						}
						stateChanged();
					}
				},
				5000);
			}
            
            synchronized(orders) {
                for (RestaurantChungOrder o : orders) {
                    if (o.s == OrderState.Pending) {
                        tryToCookIt(o);
                        return true;
                    }
                }
            }
            
            synchronized(orders) {
                for (RestaurantChungOrder o : orders) {
                    if (o.s == OrderState.DoneCooking) {
                        plateIt(o);
                        return true;
                    }
                }
            }


            synchronized(orders) {
                for (RestaurantChungOrder o : orders) {
                    if (o.s == OrderState.DonePlating) {
                        removeOrder(o);
                        return true;
                    }
                }
            }
            
    		if (workingState == WorkingState.NotWorking)
    			super.setInactive();
            
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
		
		print("end of scheduler");
		
        return blocking;
    }

//  Actions
//  =====================================================================
//  Market Orders
//  ---------------------------------------------------------------
    private void identifyFoodThatIsLow() {
        print("Identifying food that is low");                
        Map<FOOD_ITEMS, Integer> lowFoods = new HashMap<FOOD_ITEMS, Integer>();                
        int numLow = 0;
        
        for (FOOD_ITEMS i: restaurant.getFoods().keySet()) {
            if (restaurant.getFoods().get(i).s == FoodOrderState.None && (restaurant.getFoods().get(i).amount <= restaurant.getFoods().get(i).low)) {
            	lowFoods.put(i, restaurant.getFoods().get(i).capacity - restaurant.getFoods().get(i).amount);
                restaurant.getFoods().get(i).s = FoodOrderState.Pending;
                numLow++;
            }
            else lowFoods.put(i, 0);
        }

        if (numLow > 0) marketOrders.add(new MyMarketOrder(new MarketOrder(lowFoods)));
        
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

        for (FOOD_ITEMS i: restaurant.getFoods().keySet()) {
            print("Current Inventory: " + i + " " + restaurant.getFoods().get(i).amount);
        }

        
        for (FOOD_ITEMS i: o.order.orderItems.keySet()) {
            print("Order: " + i + " " + o.order.orderItems.get(i));
        }
         
        MarketBuilding selectedMarket = (MarketBuilding) CityMap.findRandomBuilding(BUILDING.market);
        print(restaurant.toString());
        print(o.order.toString());
        print(restaurant.getRestaurantChungCashier().toString());
        print(restaurant.getRestaurantChungCashier().getMarketCustomerDeliveryPayment().toString());
        MarketCustomerDelivery marketCustomerDelivery = new MarketCustomerDeliveryRole(restaurant, o.order, restaurant.getRestaurantChungCashier().getMarketCustomerDeliveryPayment());
    	marketCustomerDelivery.setMarket(selectedMarket);
        marketCustomerDelivery.setPerson(super.getPerson());
        marketCustomerDeliveryRoles.add((Role) marketCustomerDelivery);
    	restaurant.getRestaurantChungCashier().msgAddMarketOrder(selectedMarket, o.order);
        marketCustomerDelivery.setActive();
        return;
    }
    
    private void removeMarketOrder(MyMarketOrder o) {
        print("removing market order");
        removeMarketOrderFromList(o);
    }
        
//  Cooking
//  ---------------------------------------------------------------
    private void tryToCookIt(RestaurantChungOrder o) {
        Food f = restaurant.getFoods().get(FOOD_ITEMS.valueOf(o.choice));
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
    
    private void cookIt(final RestaurantChungOrder o) {
        o.s = OrderState.Cooking;
        final RestaurantChungAnimatedCook cookGui = this.getAnimation(RestaurantChungAnimatedCook.class);
        cookGui.DoGoToGrill(o.choice);
//		try {
//			atGrill.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        print("Cooking");
        cooking = true;
        timer.schedule(new TimerTask() {
        	public void run() {
                Food f = restaurant.getFoods().get(FOOD_ITEMS.valueOf(o.choice));
                f.amount--;
                print(o.choice + " amount after cooking " + f.amount);
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
        restaurant.getFoods().get(FOOD_ITEMS.valueOf(o.choice)).cookingTime*100);
    }
    
    private void plateIt(final RestaurantChungOrder o) {
        o.s = OrderState.Plating;
        final RestaurantChungAnimatedCook cookGui = this.getAnimation(RestaurantChungAnimatedCook.class);
        cookGui.DoGoToPlating(o.choice);
//		try {
//			atPlating.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        print("Plating");
        plating = true;
        timer2.schedule(new TimerTask() {
            public void run() {
            	RestaurantChungWaiter waiter = findWaiter(o); // Determines the waiter associated with the order
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
    
    private void removeOrder(RestaurantChungOrder o) {
        print("removing order");
        removeOrderFromList(o);
    }

    private void informWaiterOfCancellation(RestaurantChungOrder o) {
        RestaurantChungWaiter w = findWaiter(o);
        w.msgOutOfItem(o.choice, o.table);
    }

//  Getters
//  =====================================================================   
	@Override
	public RestaurantChungRevolvingStand getRevolvingStand() {
		return orderStand;
	}

	@Override
	public List<RestaurantChungOrder> getOrders() {
		return orders;
	}
	
	@Override
	public List<Role> getMarketCustomerDeliveryRoles() {
		return marketCustomerDeliveryRoles;
	}
	
	@Override
	public List<MyMarketOrder> getMarketOrders() {
		return marketOrders;
	}
	
//  Setters
//  =====================================================================       
	@Override
	public void setRevolvingStand(RestaurantChungRevolvingStand stand) {
		orderStand = stand;
	}
	
//  Utilities
//  =====================================================================               
	@Override
    public RestaurantChungWaiter findWaiter(RestaurantChungOrder order) {
        for(RestaurantChungOrder o : orders){
            if(o == order) {
                return o.w;
            }
        }
        return null;
    }
    
	@Override
    public Food findFood(String choice) {
        for(Food f: restaurant.getFoods().values()){
            if(f.item.equals(choice)) {
                return f;
            }
        }
        return null;
    }
    
	@Override
    public MyMarketOrder findMarketOrder(int id) {
        for(MyMarketOrder o: marketOrders){
            if(o.order.orderId == id) {
                return o;
            }
        }
        return null;
    }
    
	@Override
    public void removeOrderFromList(RestaurantChungOrder order) {
        for(int i = 0; i < orders.size(); i++) {
            if(orders.get(i) == order) {
                orders.remove(order);
            }
        }
    }
        
	@Override
    public void removeMarketOrderFromList(MyMarketOrder order) {
        for(int i = 0; i < marketOrders.size(); i++) {
            if(marketOrders.get(i) == order) {
                marketOrders.remove(order);
            }
        }
    }

	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungCookRole " + this.getPerson().getName(), msg);
    }
    
//  Classes
//  ===================================================================== 
    public class MyMarketOrder{
    	private MarketOrder order;
        private MarketOrderState s;
        
        public MyMarketOrder(MarketOrder o) {
        	order = new MarketOrder(o);
            s = MarketOrderState.Pending;
        }
        
        public MarketOrder getOrder() {
			return order;
		}
        
        public MarketOrderState getMarketOrderState() {
        	return s;
		}
    }
}