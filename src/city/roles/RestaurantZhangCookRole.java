package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
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
import city.Application;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantZhangAnimatedCook;
import city.bases.JobRole;
import city.bases.RestaurantBuilding;
import city.bases.interfaces.RestaurantBuildingInterface.Food;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantZhangBuilding;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.RestaurantZhangCashier;
import city.roles.interfaces.RestaurantZhangCook;
import city.roles.interfaces.RestaurantZhangHost;
import city.roles.interfaces.RestaurantZhangWaiter;

public class RestaurantZhangCookRole extends JobRole implements RestaurantZhangCook {

	// Data

	private List<MarketBuilding> markets = Collections.synchronizedList(new ArrayList<MarketBuilding>());
	private RestaurantZhangAnimatedCook thisGui;
	private RestaurantZhangHost host;
	private Timer timer = new Timer();
	private Semaphore atBase = new Semaphore(0, false);
	private boolean restaurantClosing = false;
	private List<RestaurantZhangOrder> ordersToCook = Collections.synchronizedList(new ArrayList<RestaurantZhangOrder>());
	private RestaurantZhangRevolvingStand myOrderStand;
	private boolean waitingToCheckStand = false;
	private RestaurantZhangMenu mainMenu = new RestaurantZhangMenu();

	private RestaurantZhangBuilding buildingOfEmployment;
	private List<MarketCustomerDelivery> marketCustomerDeliveryList = Collections.synchronizedList(new ArrayList<MarketCustomerDelivery>());
	private List<MyMarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MyMarketOrder>());

	// Constructor

	public RestaurantZhangCookRole(RestaurantZhangBuilding restaurantToWorkAt, int shiftStart_, int shiftEnd_) {
		super();
		buildingOfEmployment = restaurantToWorkAt;
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

	// From MarketDeliveryPerson
	@Override
	public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> marketOrder, int id) {
		print("RestaurantZhangCook received msgHereIsOrderDelivery from MarketDeliveryPerson");
		MyMarketOrder mo = findMarketOrder(id);
		marketOrders.remove(mo);

		for (FOOD_ITEMS i: marketOrder.keySet()) {
			Food f = findFood(i.toString());
			print("New Inventory: " + f.getItem() + " " + f.getAmount());
			mainMenu.reAddItem(f.getItem());
			f.setFoodOrderState(RestaurantBuilding.FoodOrderState.None);
		}
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
			for(MarketCustomerDelivery r : marketCustomerDeliveryList) {
				if (r.getActive() && r.getActivity()) {
					blocking = true;
					boolean activity = r.runScheduler();
					if (!activity) {
						r.setActivityFinished();
					}
					break;
				}
				// The role becomes inactive when the order is fulfilled, cook should remove the role from its list
				else if (!r.getActive()) {
					r.setActivityFinished();
					MyMarketOrder mo = findMarketOrder(((MarketCustomerDelivery) r).getOrder().getOrderId());
					msgHereIsOrderDelivery(mo.order.getOrderItems(), mo.getOrder().getOrderId());
					marketCustomerDeliveryList.remove(r);
					break;
				}
			}


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
		if(findFood(o.choice).getAmount() <= 0) {
			print("Out of " + findFood(o.choice).getItem());
			mainMenu.remove(o.choice);
			thisGui.removeFromPlatingArea(o.pos);
			ordersToCook.remove(o);
			o.w.msgOutOfFood(o.t);

			Application.FOOD_ITEMS currentFoodItem = Application.FOOD_ITEMS.chicken;
			for(Application.FOOD_ITEMS foodItem : Application.FOOD_ITEMS.values()) {
				if(o.choice.equals(foodItem.toString())) {
					currentFoodItem = foodItem;
					break;
				}
			}

			//Checks if an order of this type has been created
			for(MyMarketOrder order : marketOrders) {
				for(Application.FOOD_ITEMS foodInOrder : order.getOrder().getOrderItems().keySet()) {
					if(foodInOrder == currentFoodItem) {
						return;
					}
				}
			}

			buildingOfEmployment.getFoods().get(currentFoodItem).setFoodOrderState(RestaurantBuilding.FoodOrderState.Pending);
			Map<FOOD_ITEMS, Integer> mapForNewMarketOrder = new HashMap<FOOD_ITEMS, Integer>();
			mapForNewMarketOrder.put(currentFoodItem, buildingOfEmployment.getFoods().get(currentFoodItem).getCapacity());
			MyMarketOrder newMarketOrder = new MyMarketOrder(new MarketOrder(mapForNewMarketOrder));
			newMarketOrder.state = MarketOrderState.Ordered;
			marketOrders.add(newMarketOrder);

			MarketBuilding selectedMarket = (MarketBuilding) Application.CityMap.findRandomBuilding(Application.BUILDING.market);
			MarketCustomerDelivery marketCustomerDelivery = new MarketCustomerDeliveryRole(buildingOfEmployment, newMarketOrder.order, buildingOfEmployment.getCashier().getMarketCustomerDeliveryPayment());
			marketCustomerDelivery.setMarket(selectedMarket);
			marketCustomerDelivery.setPerson(super.getPerson());
			marketCustomerDelivery.setActive();
			marketCustomerDeliveryList.add(marketCustomerDelivery);
			buildingOfEmployment.getCashier().msgAddMarketOrder(selectedMarket, newMarketOrder.order);
			
			stateChanged();
			return;
		} else {
			findFood(o.choice).setAmount(findFood(o.choice).getAmount() - 1);
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
		(long) findFood(tempOrder.choice).getCookingTime());
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
	public int getPosOfNewOrder() {
		return ordersToCook.size();
	}

	private MyMarketOrder findMarketOrder(int orderId) {
		for(MyMarketOrder order : marketOrders) {
			if(order.getOrder().getOrderId() == orderId)
				return order;
		}
		return null;
	}

	private Food findFood(String foodString) {
		for(Food f : buildingOfEmployment.getFoods().values()) {
			if(f.getItem().equals(foodString))
				return f;
		}
		return null;
	}

	// Setters

	@Override
	public void setMenuTimes(RestaurantZhangMenu m) {
		mainMenu = m;
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
		thisGui.GoToDestination(190, 50);
		waitForAnimation();
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
		thisGui.GoToDestination(-20, -20);
		waitForAnimation();
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

	// Classes
	public class MyMarketOrder {
		private MarketOrder order;
		private MarketOrderState state;

		public MyMarketOrder(MarketOrder o) {
			order = new MarketOrder(o);
			state = MarketOrderState.Pending;
		}

		public MarketOrder getOrder() {
			return order;
		}

		public MarketOrderState getMarketOrderState() {
			return state;
		}
	}

	@Override
	public RestaurantZhangCashier getCashier() {
		// TODO Auto-generated method stub
		return null;
	}

	//	public static class CookInvoice {
	//		// MarketBuilding assignedMarket;
	//		MarketOrder marketorder;
	//		// CookInvoiceStatus status;
	//		String food;
	//		// int amount;
	//		// static enum CookInvoiceStatus {created, processing, changedMarket, completed};
	//
	//		CookInvoice(String food_, int amount_, Market market_) {
	//			food = food_;
	//			// amount = amount_;
	//			Map<FOOD_ITEMS, Integer> invoice = new HashMap<FOOD_ITEMS, Integer>();
	//			switch(food_) {
	//			case "steak":
	//				invoice.put(FOOD_ITEMS.steak, amount_);
	//				break;
	//			case "chicken":
	//				invoice.put(FOOD_ITEMS.chicken, amount_);
	//				break;
	//			case "pizza":
	//				invoice.put(FOOD_ITEMS.pizza, amount_);
	//				break;
	//			}
	//			marketorder = new MarketOrder(invoice);
	//			// assignedMarket = market_;
	//			// status = CookInvoiceStatus.created;
	//		}
	//	}

}

