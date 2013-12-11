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
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChungAnimatedCook;
import city.bases.JobRole;
import city.bases.RestaurantBuilding.FoodOrderState;
import city.bases.Role;
import city.bases.interfaces.RestaurantBuildingInterface.Food;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChungBuilding;
import city.buildings.interfaces.RestaurantChung;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.RestaurantChungCook;
import city.roles.interfaces.RestaurantChungWaiter;

public class RestaurantChungCookRole extends JobRole implements RestaurantChungCook {
//  Data
//  ===================================================================== 
	public EventLog log = new EventLog();
	Timer timer0 = new Timer();
	Timer timer = new Timer();
    Timer timer2 = new Timer();
    
	private Semaphore atCookHome = new Semaphore(0, true);
	private Semaphore atGrill = new Semaphore(0, true);
	private Semaphore atPlating = new Semaphore(0, true);

	private boolean cooking = false;
    private boolean plating = false;
	boolean waitingToCheckStand = false;
	
	RestaurantChung restaurant;

    private List<RestaurantChungOrder> orders = Collections.synchronizedList(new ArrayList<RestaurantChungOrder>()); // Holds orders, their states, and recipients

    private List<Role> marketCustomerDeliveryRoles = new ArrayList<Role>(); // List of marketCustomerDelivery roles that are created for each order to the market
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
    public void msgHereIsAnOrder(RestaurantChungWaiter w, FOOD_ITEMS choice, int table) {
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
        o.setOrderState(OrderState.DoneCooking);
        print("RestaurantChungCook done cooking");
		log.add(new LoggedEvent("RestaurantChungCook received msgSelfDoneCooking."));
        stateChanged();
    }
    
	@Override
    public void msgSelfDonePlating(RestaurantChungOrder o) {
        print("RestaurantChungCook done plating");
		log.add(new LoggedEvent("RestaurantChungCook received msgSelfDonePlating."));
		orders.remove(o);
        stateChanged();
    }

//  Market Delivery Person
//  ---------------------------------------------------------------
	@Override
    public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> marketOrder, int id) {
        print("RestaurantChungCook received msgHereIsOrderDelivery from MarketDeliveryPerson");
		log.add(new LoggedEvent("RestaurantChungCook received msgHereIsOrderDelivery from MarketDeliveryPerson."));
        MyMarketOrder mo = findMarketOrder(id);
        marketOrders.remove(mo);
        
        for (FOOD_ITEMS i: marketOrder.keySet()) {
            Food f = findFood(i.toString());
            f.setAmount(f.getAmount() + marketOrder.get(i));
            print("New Inventory: " + f.getItem() + " " + f.getAmount());
            f.setFoodOrderState(FoodOrderState.None);
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
			for (Role r : marketCustomerDeliveryRoles) {
				if (r.getActive() && r.getActivity()) {
					blocking  = true;
					boolean activity = r.runScheduler();
					if (!activity) {
						r.setActivityFinished();
					}
					break;
				}
				// The role becomes inactive when the order is fulfilled, cook should remove the role from its list
				else if (!r.getActive()) {
					marketCustomerDeliveryRoles.remove(r);
					break;
				}
			}
						    		
    		if (workingState == WorkingState.GoingOffShift) {
    			if (restaurant.getRestaurantChungCook() != this)
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
                    if (o.getOrderState() == OrderState.Cancelled) {
                        informWaiterOfCancellation(o);
                        orders.remove(o);
                        return true;
                    }
                }
            }
            
			if(!waitingToCheckStand) {
				waitingToCheckStand = true;
				timer.schedule(new TimerTask() {
					public void run() {
						waitingToCheckStand = false;
						RestaurantChungOrder newOrder = restaurant.getOrderStand().remove();
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
                    if (o.getOrderState() == OrderState.Pending) {
                        tryToCookIt(o);
                        return true;
                    }
                }
            }
            
            synchronized(orders) {
                for (RestaurantChungOrder o : orders) {
                    if (o.getOrderState() == OrderState.DoneCooking) {
                        plateIt(o);
                        return true;
                    }
                }
            }
            
    		if (workingState == WorkingState.NotWorking && orders.size() == 0)
    			super.setInactive();

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
        Map<FOOD_ITEMS, Integer> lowFoods = new HashMap<FOOD_ITEMS, Integer>();                
        int numLow = 0;
        
        for (FOOD_ITEMS i: restaurant.getFoods().keySet()) {
            if (restaurant.getFoods().get(i).getFoodOrderState() == FoodOrderState.None && (restaurant.getFoods().get(i).getAmount() <= restaurant.getFoods().get(i).getLow())) {
            	lowFoods.put(i, restaurant.getFoods().get(i).getCapacity() - restaurant.getFoods().get(i).getAmount());
                restaurant.getFoods().get(i).setFoodOrderState(FoodOrderState.Pending);
                numLow++;
            }
            else lowFoods.put(i, 0);
        }

        if (numLow > 0) marketOrders.add(new MyMarketOrder(new MarketOrder(lowFoods)));
        
        msgSelfLowFoodsIdentified();
    }
    
    private void orderFoodThatIsLow(MyMarketOrder o) {
        print("Ordering food that is low");
        o.s = MarketOrderState.Ordered;
        
        for (FOOD_ITEMS i: o.order.getOrderItems().keySet()) {
            if (o.order.getOrderItems().get(i) > 0) {
                Food f = findFood(i.toString());
                f.setFoodOrderState(FoodOrderState.Ordered);
            }
        }

        for (FOOD_ITEMS i: restaurant.getFoods().keySet()) {
            print("Current Inventory: " + i + " " + restaurant.getFoods().get(i).getAmount());
        }

        
        for (FOOD_ITEMS i: o.order.getOrderItems().keySet()) {
            print("Order: " + i + " " + o.order.getOrderItems().get(i));
        }
         
        MarketBuilding selectedMarket = (MarketBuilding) CityMap.findRandomBuilding(BUILDING.market);
        MarketCustomerDelivery marketCustomerDelivery = new MarketCustomerDeliveryRole(restaurant, o.order, restaurant.getRestaurantChungCashier().getMarketCustomerDeliveryPayment());
    	marketCustomerDelivery.setMarket(selectedMarket);
        marketCustomerDelivery.setPerson(super.getPerson());
        marketCustomerDelivery.setActive();
        marketCustomerDeliveryRoles.add((Role) marketCustomerDelivery);
    	restaurant.getRestaurantChungCashier().msgAddMarketOrder(selectedMarket, o.order);
    	
    	stateChanged();
    }
        
//  Cooking
//  ---------------------------------------------------------------
    private void tryToCookIt(RestaurantChungOrder o) {
        Food f = restaurant.getFoods().get(o.getChoice());
        // If out of food
        identifyFoodThatIsLow();
        
        if (f.getAmount() == 0) {
                // sets all orders of the same item to cancelled
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getChoice() == o.getChoice()) {
                    orders.get(i).setOrderState(OrderState.Cancelled);
                }
            }
            return;
        }
        
        cookIt(o);
    }
    
    private void cookIt(final RestaurantChungOrder o) {
        o.setOrderState(OrderState.Cooking);
        final RestaurantChungAnimatedCook cookGui = this.getAnimation(RestaurantChungAnimatedCook.class);
        cookGui.DoGoToGrill(o.getChoice().toString());
//		try {
//			atGrill.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        print("Cooking");
        cooking = true;
        timer0.schedule(new TimerTask() {
        	public void run() {
                Food f = restaurant.getFoods().get(o.getChoice());
                f.setAmount(f.getAmount() - 1);
                print(o.getChoice() + " amount after cooking " + f.getAmount());
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
        restaurant.getFoods().get(o.getChoice()).getCookingTime()*100);
    }
    
    private void plateIt(final RestaurantChungOrder o) {
        o.setOrderState(OrderState.Plating);
        final RestaurantChungAnimatedCook cookGui = this.getAnimation(RestaurantChungAnimatedCook.class);
        cookGui.DoGoToPlating(o.getChoice().toString());
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
                waiter.msgOrderIsReady(o.getChoice(), o.getTable());
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

    private void informWaiterOfCancellation(RestaurantChungOrder o) {
        RestaurantChungWaiter w = findWaiter(o);
        w.msgOutOfItem(o.getChoice(), o.getTable());
    }

//  Getters
//  =====================================================================   
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
	
//  Utilities
//  =====================================================================               
	@Override
    public RestaurantChungWaiter findWaiter(RestaurantChungOrder order) {
        for(RestaurantChungOrder o : orders){
            if(o == order) {
                return o.getRestaurantChungWaiter();
            }
        }
        return null;
    }
    
	@Override
    public Food findFood(String choice) {
        for(Food f: restaurant.getFoods().values()){
            if(f.getItem().equals(choice)) {
                return f;
            }
        }
        return null;
    }
    
	@Override
    public MyMarketOrder findMarketOrder(int id) {
        for(MyMarketOrder o: marketOrders){
            if(o.order.getOrderId() == id) {
                return o;
            }
        }
        return null;
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