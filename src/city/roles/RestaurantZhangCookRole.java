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

import trace.AlertLog;
import trace.AlertTag;
import utilities.MarketOrder;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangOrder;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantZhangAnimatedCook;
import city.bases.Building;
import city.bases.JobRole;
import city.bases.RestaurantBuilding;
import city.bases.interfaces.RestaurantBuildingInterface.Food;
import city.buildings.MarketBuilding;
import city.buildings.interfaces.Market;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.RestaurantZhangCashier;
import city.roles.interfaces.RestaurantZhangCook;
import city.roles.interfaces.RestaurantZhangHost;
import city.roles.interfaces.RestaurantZhangWaiter;

public class RestaurantZhangCookRole extends JobRole implements RestaurantZhangCook {

	// Data
	
	private List<MarketBuilding> markets = Collections.synchronizedList(new ArrayList<MarketBuilding>());
	private RestaurantZhangCashier cashier;
	private List<MarketCustomerDelivery> marketCustomerDeliveryList = new ArrayList<MarketCustomerDelivery>();
	private RestaurantZhangAnimatedCook thisGui;
	private RestaurantZhangHost host;
	private Timer timer = new Timer();
	private Semaphore atBase = new Semaphore(0, false);
	private boolean restaurantClosing = false;
	private List<RestaurantZhangOrder> ordersToCook = Collections.synchronizedList(new ArrayList<RestaurantZhangOrder>());
	private RestaurantZhangRevolvingStand myOrderStand;
	private boolean waitingToCheckStand = false;
	private RestaurantZhangMenu mainMenu = new RestaurantZhangMenu();
	private Map<String, Food> cookInventory = new HashMap<String, Food>();
	private List<CookInvoice> cookInvoiceList = new ArrayList<CookInvoice>();
	
	// Constructor

	public RestaurantZhangCookRole(Building restaurantToWorkAt, int shiftStart_, int shiftEnd_) {
		super();
		this.setShift(shiftStart_, shiftEnd_);
		this.setWorkplace(restaurantToWorkAt);
		this.setSalary(RESTAURANTZHANGCOOKSALARY);
	}
	
	// Messages

	@Override
	public void msgHereIsAnOrder(RestaurantZhangWaiter w, String choice, RestaurantZhangTable t) {
		ordersToCook.add(new RestaurantZhangOrder(w, choice, t, ordersToCook.size()));
		stateChanged();
	}

	@Override
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

	@Override
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

	@Override
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

	@Override
	public void msgAtDestination() { //from animation
		atBase.release();
		stateChanged();
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		try {
			if(restaurantClosing) {
				if(((RestaurantZhangHostRole)host).getNumberOfCustomersInRestaurant() <= 0) {
					setInactive();
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

	private void cookOrder(RestaurantZhangOrder o) {
		o.status = RestaurantZhangOrder.OrderStatus.cooking;
		thisGui.addToPlatingArea(o.choice + "?", o.pos);
		// Check if food is in stock
		if(cookInventory.get(o.choice).getAmount()  <= 0) {
			print("Out of " + cookInventory.get(o.choice).getItem());
			mainMenu.remove(o.choice);
			thisGui.removeFromPlatingArea(o.pos);
			ordersToCook.remove(o);
			o.w.msgOutOfFood(o.t);
			for(CookInvoice ci : cookInvoiceList) {
				if(ci.food == o.choice) { // Invoice for this food already exists
					return;
				}
			}
			CookInvoice tempInvoice = new CookInvoice(o.choice, cookInventory.get(o.choice).getCapacity() - cookInventory.get(o.choice).getAmount(), markets.get(0));
			cookInvoiceList.add(tempInvoice);
			MarketCustomerDeliveryRole tempDeliveryRole = new MarketCustomerDeliveryRole(this.getWorkplace(RestaurantBuilding.class), tempInvoice.marketorder, cashier.getMarketCustomerDeliveryPayment());
			marketCustomerDeliveryList.add(tempDeliveryRole);
			tempDeliveryRole.setActive();
			stateChanged();
			return;
		} else {
			cookInventory.get(o.choice).setAmount(cookInventory.get(o.choice).getAmount() - 1);
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
		(long) cookInventory.get(tempOrder.choice).getCookingTime());
	}

	private void orderIsReady(RestaurantZhangOrder o) {
		print("Finished order: " + o.choice);
		for(RestaurantZhangOrder order : ordersToCook) {
			if(order.equals(o)) {
				order.status = RestaurantZhangOrder.OrderStatus.doneCooking;
				stateChanged();
				return;
			}
		}
	}

	private void plateFood(RestaurantZhangOrder o) {
		thisGui.goToGrill(o.pos);
		waitForAnimation();
		thisGui.removeFromGrill(o.pos);
		thisGui.goToPlating();
		waitForAnimation();
		thisGui.addToPlatingArea(o.choice, o.pos);
		thisGui.goToBase();
		waitForAnimation();
		print("Plated order " + o.choice + " for waiter " + o.w.getPerson().getName());
		o.status = RestaurantZhangOrder.OrderStatus.plated;
		o.w.msgOrderIsReady(o.choice, o.t);
	}

	private void removeFromPlating(RestaurantZhangOrder o) {
		thisGui.removeFromPlatingArea(o.pos);
		ordersToCook.remove(o);
	}

	//	private void notifyMarket(CookInvoice ci) {
	//		print("Asking market " + ci.assignedMarket.getName() + " for " + ci.amount + " " + ci.food);
	//		ci.status = CookInvoiceStatus.processing;
	//		ci.assignedMarket.msgNeedFood(ci.amount, ci.food);
	//	}

	//	private void changeMarket(CookInvoice ci) {
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

	//	private void restockInventory(CookInvoice ci) {
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
	
	// Getters
	
	@Override
	public List<MarketBuilding> getMarkets() {
		return markets;
	}
	
	@Override
	public RestaurantZhangCashier getCashier() {
		return cashier;
	}
	
	@Override
	public List<MarketCustomerDelivery> getmarketCustomerDeliveryList() {
		return marketCustomerDeliveryList;
	}
	
	@Override
	public RestaurantZhangAnimatedCook getGui() {
		return thisGui;
	}
	
	@Override
	public RestaurantZhangHost getHost() {
		return host;
	}
	
	@Override
	public List<RestaurantZhangOrder> getOrdersToCook() {
		return ordersToCook;
	}
	
	@Override
	public RestaurantZhangRevolvingStand getOrderStand() {
		return myOrderStand;
	}
	
	@Override
	public boolean getWaitingToCheckStand() {
		return waitingToCheckStand;
	}
	
	@Override
	public RestaurantZhangMenu getMainMenu() {
		return mainMenu;
	}
	
	@Override
	public Map<String, Food> getCookInventory() {
		return cookInventory;
	}
	
	@Override
	public List<CookInvoice> getCookInvoiceList() {
		return cookInvoiceList;
	}
	
	@Override
	public int getPosOfNewOrder() {
		return ordersToCook.size();
	}

	// Setters

	@Override
	public void setMenuTimes(RestaurantZhangMenu m, Map<FOOD_ITEMS, Food> food) {
		mainMenu = m;
		Iterator<Map.Entry<FOOD_ITEMS, Food>> foodIt = food.entrySet().iterator();
		while(foodIt.hasNext()) {
			Map.Entry<FOOD_ITEMS, Food> entry = foodIt.next();
			cookInventory.put(entry.getValue().getItem(), entry.getValue());
		}
	}

	@Override
	public void setAnimation(RestaurantZhangAnimatedCook gui) {
		thisGui = gui;
	}

	@Override
	public void setRevolvingStand(RestaurantZhangRevolvingStand rs) {
		myOrderStand = rs;
	}

	@Override
	public void setActive() {
		super.setActive();
		thisGui.setVisible(true);
		runScheduler();
	}
	
	@Override
	public void setHost(RestaurantZhangHost h) {
		host = h;
	}
	
	@Override
	public void setInactive() {
		if(host != null) {
			if(((RestaurantZhangHostRole)host).getNumberOfCustomersInRestaurant() !=0) {
				restaurantClosing = true;
				return;
			}
		}
		thisGui.setVisible(false);
		super.setInactive();
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("RestaurantZhangCook", msg);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTZHANG, "RestaurantZhangCookRole " + this.getPerson().getName(), msg);
    }
	
	@Override
	public void hackOrderIsReady(RestaurantZhangOrder o) {
		orderIsReady(o);
	}
	
	@Override
	public void addMarket(MarketBuilding m) {
		markets.add(m);
	}
	
	// Classes

	public static class CookInvoice {
		// MarketBuilding assignedMarket;
		MarketOrder marketorder;
		// CookInvoiceStatus status;
		String food;
		// int amount;
		// static enum CookInvoiceStatus {created, processing, changedMarket, completed};

		CookInvoice(String food_, int amount_, Market market_) {
			food = food_;
			// amount = amount_;
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
			// assignedMarket = market_;
			// status = CookInvoiceStatus.created;
		}
	}
	
}

