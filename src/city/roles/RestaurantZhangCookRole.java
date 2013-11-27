package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import sun.security.krb5.internal.HostAddress;
import utilities.MarketOrder;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangOrder;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.Building;
import city.Role;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantZhangAnimatedCook;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantBaseBuilding;
import city.buildings.RestaurantBaseBuilding.Food;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.RestaurantZhangCashier;
import city.interfaces.RestaurantZhangCook;
import city.interfaces.RestaurantZhangHost;
import city.interfaces.RestaurantZhangWaiter;

/**
 * Restaurant Cook Agent
 */

public class RestaurantZhangCookRole extends Role implements RestaurantZhangCook {
	private static final int RESTAURANTZHANGCOOKSALARY = 100;
	private final int COOKX = 190;
	private final int COOKY = 120;

	public List<RestaurantZhangOrder> ordersToCook = Collections.synchronizedList(new ArrayList<RestaurantZhangOrder>());

	// REVOLVING STAND STUFF
	public RestaurantZhangRevolvingStand myOrderStand;
	public boolean waitingToCheckStand = false;

	public String name;

	public RestaurantZhangMenu mainMenu = new RestaurantZhangMenu();
	public Map<String, Food> cookInventory = new HashMap<String, Food>();

	public List<CookInvoice> cookInvoiceList = new ArrayList<CookInvoice>();
	public List<MarketBuilding> markets = Collections.synchronizedList(new ArrayList<MarketBuilding>());
	public RestaurantZhangCashier cashier;
	public List<MarketCustomerDelivery> marketCustomerDeliveryList = new ArrayList<MarketCustomerDelivery>();

	public RestaurantZhangAnimatedCook thisGui;

	public RestaurantZhangHost host;

	Timer timer = new Timer();

	private Semaphore atBase = new Semaphore(0, false);
	private boolean restaurantClosing = false;

	public RestaurantZhangCookRole(Building restaurantToWorkAt, int shiftStart_, int shiftEnd_) {
		super();
		this.setShift(shiftStart_, shiftEnd_);
		this.setWorkplace(restaurantToWorkAt);
		this.setSalary(RESTAURANTZHANGCOOKSALARY);
	}

	public String getName() {
		return super.getPerson().getName();
	}

	public void msgHereIsAnOrder(RestaurantZhangWaiter w, String choice, RestaurantZhangTable t) {
		ordersToCook.add(new RestaurantZhangOrder(w, choice, t, ordersToCook.size()));
		stateChanged();
	}

	public void msgGotCompletedOrder(RestaurantZhangTable t) {
		synchronized(ordersToCook) {
			for(RestaurantZhangOrder o : ordersToCook) {
				if(o.t.equals(t)) {
					o.status = RestaurantZhangOrder.OrderStatus.removed;
					stateChanged();
					return;
				}
			}
		}
	}

	public void msgProcessedInvoice(String food, boolean isAvailable, int processedAmount) {
		//		CookInvoice currentInvoice = null; // Find the cookInvoice associated with the food
		//		for(CookInvoice ci : cookInvoiceList) {
		//			if(ci.food == food) {
		//				currentInvoice = ci;
		//			}
		//		}
		//		if(currentInvoice != null) {
		//			if(!isAvailable) { // If the market cannot fulfill it, switch the market for the invoice
		//				currentInvoice.status = CookInvoiceStatus.changedMarket;
		//				stateChanged();
		//			} else if(currentInvoice.amount > processedAmount) {
		//				CookInvoice newInvoice = new CookInvoice(currentInvoice.food, 
		//						cookInventory.get(currentInvoice.food).threshold - (processedAmount + cookInventory.get(currentInvoice.food).amount), 
		//						marketList.get(marketList.indexOf(currentInvoice.assignedMarket)));
		//				newInvoice.status = CookInvoiceStatus.changedMarket;
		//				cookInvoiceList.add(newInvoice);				
		//				stateChanged();
		//			}
		//		} else {
		//			print("Nonexistent invoice.");
		//		}
	}

	public void msgHereIsInvoice(String food, int amount) {
		//		CookInvoice currentInvoice = null; // Find the cookInvoice associated with the food
		//		for(CookInvoice ci : cookInvoiceList) {
		//			if(ci.food == food) {
		//				currentInvoice = ci;
		//			}
		//		}
		//		currentInvoice.amount = amount;
		//		currentInvoice.status = CookInvoiceStatus.completed;
		//		stateChanged();
	}

	public void msgAtDestination() { //from animation
		atBase.release();
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean runScheduler() {
		try {
			if(restaurantClosing) {
				if(((RestaurantZhangHostRole)host).numberOfCustomersInRestaurant <= 0) {
					super.setInactive();
					restaurantClosing = false;
					return true;
				}
			}
			// Role Scheduler
			boolean blocking = false;
			if (marketCustomerDeliveryList.isEmpty() != true) {
				for(MarketCustomerDelivery r : marketCustomerDeliveryList) {
					if(r.getActive() && r.getActivity())
						blocking  = true;
					boolean activity = r.runScheduler();
					if (!activity) {
						r.setActivityFinished();
					}
				}
			}
			for(RestaurantZhangOrder o : ordersToCook) {
				if(o.status == RestaurantZhangOrder.OrderStatus.removed) {
					removeFromPlating(o);
					return true;
				}
			} 
			for(RestaurantZhangOrder o : ordersToCook) {
				if(o.status == RestaurantZhangOrder.OrderStatus.created) {
					cookOrder(o);
					return true;
				}
			}
			for(RestaurantZhangOrder o : ordersToCook) {
				if(o.status == RestaurantZhangOrder.OrderStatus.doneCooking) {
					plateFood(o);
					return true;
				}
			}
			//			for(CookInvoice ci : cookInvoiceList) {
			//				if(ci.status == CookInvoiceStatus.created) {
			//					notifyMarket(ci);
			//					return true;
			//				}
			//			}
			//			for(CookInvoice ci : cookInvoiceList) {
			//				if(ci.status == CookInvoiceStatus.changedMarket) {
			//					changeMarket(ci);
			//					return true;
			//				}
			//			}
			//			for(CookInvoice ci : cookInvoiceList) {
			//				if(ci.status == CookInvoiceStatus.completed) {
			//					restockInventory(ci);
			//					return true;
			//				}
			//			}
			//			// Timer to check the stand
			if(!waitingToCheckStand) {
				print("Waiting 5 seconds to check the stand");
				waitingToCheckStand = true;
				timer.schedule(new TimerTask() {
					public void run() {
						waitingToCheckStand = false;
						RestaurantZhangOrder newOrder = myOrderStand.remove();
						if(newOrder != null) {
							print("Found an item on the stand");
							ordersToCook.add(newOrder);
						}
						stateChanged();
					}
				},
				5000);
			}
			return blocking;
		} catch (ConcurrentModificationException cmeCook) {
			return false;
		}
	}

	// Actions

	void cookOrder(RestaurantZhangOrder o) {
		o.status = RestaurantZhangOrder.OrderStatus.cooking;
		thisGui.addToPlatingArea(o.choice + "?", o.pos);
		// Check if food is in stock
		if(cookInventory.get(o.choice).amount  <= 0) {
			print("Out of " + cookInventory.get(o.choice).item);
			mainMenu.remove(o.choice);
			thisGui.removeFromPlatingArea(o.pos);
			ordersToCook.remove(o);
			o.w.msgOutOfFood(o.t);
			for(CookInvoice ci : cookInvoiceList) {
				if(ci.food == o.choice) { // Invoice for this food already exists
					return;
				}
			}
			CookInvoice tempInvoice = new CookInvoice(o.choice, cookInventory.get(o.choice).capacity - cookInventory.get(o.choice).amount, markets.get(0));
			cookInvoiceList.add(tempInvoice);
			MarketCustomerDeliveryRole tempDeliveryRole = new MarketCustomerDeliveryRole(this.getWorkplace(RestaurantBaseBuilding.class), tempInvoice.marketorder, cashier.getMarketCustomerDeliveryPayment());
			marketCustomerDeliveryList.add(tempDeliveryRole);
			tempDeliveryRole.setActive();
			stateChanged();
			return;
		} else {
			cookInventory.get(o.choice).amount--;
		}
		// Cooking
		thisGui.goToPlating();
		waitForAnimation();
		thisGui.removeFromPlatingArea(o.pos);
		thisGui.goToGrill(o.pos);
		waitForAnimation();
		thisGui.addToGrill(o.choice, o.pos);
		thisGui.goToBase();
		waitForAnimation();
		final RestaurantZhangOrder tempOrder = new RestaurantZhangOrder(o);
		timer.schedule(new TimerTask() {
			public void run() {
				orderIsReady(tempOrder);
			}
		},
		(long) cookInventory.get(tempOrder.choice).cookingTime);
	}

	public void orderIsReady(RestaurantZhangOrder o) {
		print("Finished order: " + o.choice);
		for(RestaurantZhangOrder order : ordersToCook) {
			if(order.equals(o)) {
				order.status = RestaurantZhangOrder.OrderStatus.doneCooking;
				stateChanged();
				return;
			}
		}
	}

	void plateFood(RestaurantZhangOrder o) {
		thisGui.goToGrill(o.pos);
		waitForAnimation();
		thisGui.removeFromGrill(o.pos);
		thisGui.goToPlating();
		waitForAnimation();
		thisGui.addToPlatingArea(o.choice, o.pos);
		thisGui.goToBase();
		waitForAnimation();
		print("Plated order " + o.choice + " for waiter " + o.w.getName());
		o.status = RestaurantZhangOrder.OrderStatus.plated;
		o.w.msgOrderIsReady(o.choice, o.t);
	}

	void removeFromPlating(RestaurantZhangOrder o) {
		thisGui.removeFromPlatingArea(o.pos);
		ordersToCook.remove(o);
	}

	//	void notifyMarket(CookInvoice ci) {
	//		print("Asking market " + ci.assignedMarket.getName() + " for " + ci.amount + " " + ci.food);
	//		ci.status = CookInvoiceStatus.processing;
	//		ci.assignedMarket.msgNeedFood(ci.amount, ci.food);
	//	}

	//	void changeMarket(CookInvoice ci) {
	//		Do("Asking another market for " + ci.amount + " " + ci.food);
	//		if(marketList.indexOf(ci.assignedMarket) >= marketList.size() - 1) { // Last market, no more food
	//			Do("All markets out of " + ci.food);
	//			cookInvoiceList.remove(ci);
	//		} else {
	//			ci.assignedMarket = marketList.get(marketList.indexOf(ci.assignedMarket) + 1);
	//			ci.status = CookInvoiceStatus.created;
	//			stateChanged();
	//		}
	//	}

	//	void restockInventory(CookInvoice ci) {
	//		Food foodToRestock = cookInventory.get(ci.food);
	//		foodToRestock.amount += ci.amount;
	//		Do("Added " + ci.amount + " " + ci.food + " to inventory.");
	//		cookInvoiceList.remove(ci);
	//		mainMenu.reAddItem(ci.food);
	//	}

	private void waitForAnimation() {
		try {
			atBase.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//utilities

	public int getX() {
		return COOKX;
	}

	public int getY() {
		return COOKY;
	}

	public void setMenuTimes(RestaurantZhangMenu m, Map<FOOD_ITEMS, Food> food) {
		mainMenu = m;
		Iterator<Map.Entry<FOOD_ITEMS, Food>> foodIt = food.entrySet().iterator();
		while(foodIt.hasNext()) {
			Map.Entry<FOOD_ITEMS, Food> entry = foodIt.next();
			cookInventory.put(entry.getValue().item, entry.getValue());
		}
	}

	public void setAnimation(RestaurantZhangAnimatedCook gui) {
		thisGui = gui;
	}

	public RestaurantZhangAnimatedCook getGui() {
		return thisGui;
	}

	public void addMarket(MarketBuilding m) {
		markets.add(m);
	}

	public int getPosOfNewOrder() {
		return ordersToCook.size();
	}

	public void setRevolvingStand(RestaurantZhangRevolvingStand rs) {
		myOrderStand = rs;
	}

	public void setActive() {
		super.setActive();
		runScheduler();
	}
	
	public void setHost(RestaurantZhangHost h) {
		host = h;
	}
	
	public void setInactive() {
		if(host != null) {
			if(((RestaurantZhangHostRole)host).numberOfCustomersInRestaurant !=0) {
				restaurantClosing = true;
				return;
			}
		}
		super.setInactive();
	}

	enum CookInvoiceStatus {created, processing, changedMarket, completed};
	private class CookInvoice {
		MarketBuilding assignedMarket;
		MarketOrder marketorder;
		CookInvoiceStatus status;
		String food;
		int amount;

		CookInvoice(String food_, int amount_, MarketBuilding market_) {
			food = food_;
			amount = amount_;
			Map<FOOD_ITEMS, Integer> invoice = new HashMap<FOOD_ITEMS, Integer>();
			switch(food_) {
			case "Steak":
				invoice.put(FOOD_ITEMS.steak, amount_);
				break;
			case "Chicken":
				invoice.put(FOOD_ITEMS.chicken, amount_);
				break;
			case "Pizza":
				invoice.put(FOOD_ITEMS.pizza, amount_);
				break;
			}
			marketorder = new MarketOrder(invoice);
			assignedMarket = market_;
			status = CookInvoiceStatus.created;
		}
	}
}

