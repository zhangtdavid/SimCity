package city.roles;

import agent.Agent;

import java.util.*;

import city.Role;
import city.interfaces.RestaurantChungMarket;
import restaurant.interfaces.Market;

/**
 * Restaurant Cook Agent
 */
// A Cook fulfills the customers' food orders as communicated by the waiters
// and maintains the restaurant's food inventory
public class RestaurantChungMarketRole extends Role implements RestaurantChungMarket {	
	Timer timer = new Timer();
	Timer timer2 = new Timer();
	RestaurantChungCashierRole cashier;
	Double money;
	
//	Fixed Numbers
//	=====================================================================
	private static final int RUSHPROCESSINGTIME = 5;
	private static final int PROCESSINGTIME = 500;
	
//	Orders
//	=====================================================================	
	public List<Order> ordersList = Collections.synchronizedList(new ArrayList<Order>()); // Holds orders, their states, and recipients
	public class Order {
		RestaurantChungCookRole c;
		int ID;
		private Map<String, Integer> orderItems = new HashMap<String, Integer>();
		boolean rush;
		OrderState s;
		
		public Order(RestaurantChungCookRole cook, int id, Map<String, Integer> marketInventory, boolean rush, OrderState state) {
			c = cook;
			ID = id;
			orderItems = marketInventory;
			this.rush = rush;
			s = state;
		}
	}
	
	private enum OrderState
	{Pending, TryingToProcess, Processing, DoneProcessing, Shipping, AskForPayment, WaitingForPayment, Fulfilled, Cancelled};
	
//	Market Inventory
//	=====================================================================
	private Map<String, Inventory> marketInventory = new HashMap<String, Inventory>();
	private class Inventory {
		String item;
		int amount;
		double price;
		
		public Inventory(String food, int num, double cost) {
			item = food;
			amount = num;
			price = cost;
		}
	}

//	Constructor
//	=====================================================================		
	public RestaurantChungMarketRole() {
		super();
		money = 0.0;
		// Add marketInventory and their quantities to a map
		marketInventory.put("Steak", new Inventory("Steak", 15, 8.00));
		marketInventory.put("Chicken", new Inventory("Chicken", 15, 5.00));
		marketInventory.put("Salad", new Inventory("Salad", 15, 3.00));
		marketInventory.put("Pizza", new Inventory("Pizza", 15, 4.00));
	}

//  Messages
//	=====================================================================
	public void msgHereIsAnOrder(RestaurantChungCookRole c, int id, boolean rush, Map<String, Integer> cookOrder) {
		print("Market received msgHereIsAnOrder");
		// Create a copy of the cook's order map
		Map<String, Integer> tempItems = new HashMap<String, Integer>();
		for(String i: cookOrder.keySet()){
			tempItems.put(i, cookOrder.get(i));
//			print(i + " " + cookOrder.get(i)); // TODO
		}

		ordersList.add(new Order(c, id, tempItems, false, OrderState.Pending));
		stateChanged();
	}
	
	public void msgSelfDoneProcessing(Order o) {
		o.s = OrderState.DoneProcessing;
		stateChanged();
	}
	
	public void msgSelfDoneShipping(Order o) {
		o.s = OrderState.AskForPayment;
		stateChanged();
	}
	
	public void msgHereIsPayment(int id, double payment) {
		Order o = findOrderFromID(id);
		o.s = OrderState.Fulfilled;
		money += payment;
		stateChanged();
	}

//  Scheduler
//	=====================================================================
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if (ordersList.size() == 0) return true; // Solved an issue I encountered, can't remember exactly?
		
		synchronized(ordersList) {
			for (Order o : ordersList) {
				if (o.s == OrderState.Pending) {
					tryToProcessIt(o);
					return true;
				}
			}
		}

		synchronized(ordersList) {
			for (Order o : ordersList) {
				if (o.s == OrderState.DoneProcessing) {
					shipIt(o);
					return true;
				}
			}
		}

		synchronized(ordersList) {
			for (Order o : ordersList) {
				if (o.s == OrderState.AskForPayment) {
					billCashier(o);
					return true;
				}
			}
		}

		synchronized(ordersList) {
			for (Order o : ordersList) {
				if (o.s == OrderState.Fulfilled || o.s == OrderState.Cancelled) {
					removeOrder(o);
					return true;
				}
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

//  Actions
//	=====================================================================	
	private void tryToProcessIt(Order o) {
		int numItems = 0;
		int numUnfulfilled = 0;
		
		Map<String, Integer> unfulfilled = new HashMap<String, Integer>();
		
		for (String i: o.orderItems.keySet()) {
			unfulfilled.put(i, 0);
			// If the market does not have an item in stock
			if (o.orderItems.get(i) > 0 && marketInventory.get(i).amount == 0) {
				numUnfulfilled++;
				unfulfilled.put(i, o.orderItems.get(i));
				o.orderItems.put(i, 0); // Removes unavailable items from the order
			}
			// If the market does not have enough of an item in stock
			else if (marketInventory.get(i).amount < o.orderItems.get(i)) {
				numUnfulfilled++;
				unfulfilled.put(i, o.orderItems.get(i) - marketInventory.get(i).amount);
				o.orderItems.put(i, marketInventory.get(i).amount); // Makes the order quantity equal to what the market has left
			}
		}
		
//		for (String i: unfulfilled.keySet()) {
//			print("Unfulfilled " + i + " " + unfulfilled.get(i));
//		}
		
		if (numUnfulfilled > 0) o.c.msgCannotFulfill(o.ID, unfulfilled); // Messages the cook if something can’t be fulfilled
		
		for (String i: o.orderItems.keySet()) {
			if (o.orderItems.get(i) > 0) {
				numItems++;
			}
		}
		
		// Cancels an empty order
		if (numItems == 0) {
			o.s = OrderState.Cancelled;
		}
		
		processIt(o);
	}
	
	private void processIt(final Order o) {
		print("Processing order");
		o.s = OrderState.Processing;
		Map<String, Integer> original = new HashMap<String, Integer>();
		for (String i: o.orderItems.keySet()) {
			original.put(i, o.orderItems.get(i));
		}
		
		if (o.rush) {
			timer.schedule(new TimerTask() {
				public void run() {
					for (String i: o.orderItems.keySet()) {
						//If marketInventory for an item is less than the order request, that item in the marketInventory is depleted
						if (marketInventory.get(i).amount < o.orderItems.get(i)) {
							o.orderItems.put(i, marketInventory.get(i).amount);
							marketInventory.get(i).amount = 0;
						}
						else {
							marketInventory.get(i).amount -= o.orderItems.get(i);
						}
					}
					msgSelfDoneProcessing(o);
				}
			},
			RUSHPROCESSINGTIME);
		}		
		
		else {
			Random rand = new Random();
			int randInt = rand.nextInt(PROCESSINGTIME);
			timer.schedule(new TimerTask() {
				public void run() {
					for (String i: o.orderItems.keySet()) {
						//If marketInventory for an item is less than the order request, that item in the marketInventory is depleted
						if (marketInventory.get(i).amount < o.orderItems.get(i)) {
							o.orderItems.put(i, marketInventory.get(i).amount);
							marketInventory.get(i).amount = 0;
						}
						else {
							marketInventory.get(i).amount -= o.orderItems.get(i);
						}
					}
					msgSelfDoneProcessing(o);
				}
			},
			randInt);	
		}
	}
	
	private void shipIt(final Order o) {
		o.s = OrderState.Shipping;

		for (String i: marketInventory.keySet()) {
			print("New Market Inventory : " + i + " " + marketInventory.get(i).amount);
		}
		
		if (o.rush) {
			timer2.schedule(new TimerTask() {
				public void run() {
					o.c.msgOrderIsReady(o.ID, o.orderItems);
					msgSelfDoneShipping(o);
				}
			},
			RUSHPROCESSINGTIME);
		}
		
		else {
			timer2.schedule(new TimerTask() {
				public void run() {
					o.c.msgOrderIsReady(o.ID, o.orderItems);
					msgSelfDoneShipping(o);
				}
			},
			PROCESSINGTIME);
		}
	}
	
	private void removeOrder(Order o) {
		removeOrderFromList(o);
	}
	
	private void billCashier(Order o) {
		o.s = OrderState.WaitingForPayment;
		double bill = 0;
		for (String i: o.orderItems.keySet()) {
			bill += o.orderItems.get(i)*marketInventory.get(i).price;
		}
		print("Billing cashier for " + bill);
		cashier.msgMarketOrderBill(this, o.ID, bill);
	}

//	Utilities
//	=====================================================================	
	public void setCashier(RestaurantChungCashierRole c) {
		this.cashier = c;
	}
	
	public Order findOrderFromID(int id) {
		for(int i = 0; i < ordersList.size(); i++) {
			if(ordersList.get(i).ID == id) {
				return ordersList.get(i);
			}
		}
		return null;
	}
	
	public void removeOrderFromList(Order order) {
		for(int i = 0; i < ordersList.size(); i++) {
			if(ordersList.get(i) == order) {
				ordersList.remove(order);
			}
		}
	}
}