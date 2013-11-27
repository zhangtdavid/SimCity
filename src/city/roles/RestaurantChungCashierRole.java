package city.roles;

import java.util.*;

import city.Application;
import city.Role;
import city.Application.FOOD_ITEMS;
import city.agents.PersonAgent;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChungBuilding;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungHost;
import city.interfaces.RestaurantChungWaiter;
import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import utilities.MarketTransaction;
/**
 * Restaurant Cook Agent
 */
// A Cashier calculates and processes customers' bills

public class RestaurantChungCashierRole extends Role implements RestaurantChungCashier {	
	public EventLog log = new EventLog();

	Timer timer = new Timer();
	private RestaurantChungBuilding restaurant;
	private RestaurantChungHost host;

	public enum WorkingState
	{Working, GoingOffShift, NotWorking};
	WorkingState workingState = WorkingState.Working;
	
	private List<Role> roles = new ArrayList<Role>();


//	Transactions
//	=====================================================================	
	public List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());
	public class Transaction {
		RestaurantChungWaiter w;
		RestaurantChungCustomer c;
		String choice;
		public int price;
		public int payment;
		public TransactionState s;
		
		public Transaction(RestaurantChungWaiter w2, RestaurantChungCustomer customer, String order, TransactionState state) {
			w = w2;
			c = customer;
			choice = order;
			price = restaurant.foods.get(FOOD_ITEMS.valueOf(choice)).price;
			payment = 0;
			s = state;
		}
	}
		
	public enum TransactionState
	{None, Pending, Calculating, ReceivedPayment, InsufficientPayment, NotifiedHost, Done};
	
	public List<MarketTransaction> marketTransactions = Collections.synchronizedList(new ArrayList<MarketTransaction>());
	
	// list market transactions

		
//	Constructor
//	=====================================================================		
	public RestaurantChungCashierRole(RestaurantChungBuilding b, int t1, int t2) {
		super();
		restaurant = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChungBuilding.getWorkerSalary());
		roles.add(new MarketCustomerDeliveryPaymentRole(restaurant, marketTransactions));
		roles.add((Role) restaurant.bankCustomer);
	}
	
//	public void setActive(){
//		super.setActive();
//	}
	
	public void setInActive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================
//	Waiter
//	---------------------------------------------------------------
	public void msgComputeBill(RestaurantChungWaiter w, RestaurantChungCustomer c, String order) {
		print("Cashier received msgComputeBill");
		log.add(new LoggedEvent("Cashier received msgComputeBill. For order " + order));
		if (workingState != WorkingState.NotWorking) {
			transactions.add(new Transaction(w, c, order, TransactionState.Pending));
			stateChanged();
		}
		// TODO inform sender of inactivity
	}

//	Restaurant Customer
//	---------------------------------------------------------------
	public void msgHereIsPayment(RestaurantChungCustomer c, int money) {
		print("Cashier received msgHereIsPayment");
		log.add(new LoggedEvent("Cashier received msgHereIsPayment. For amount of " + money));
		Transaction t = findTransaction(c);
		t.payment = money;
		restaurant.setCash(restaurant.getCash() + money);
		System.out.println("RESTAURANT CASH: " + restaurant.getCash());
		t.s = TransactionState.ReceivedPayment;
		stateChanged();
	}

//	Cook
//	---------------------------------------------------------------
	public void msgAddMarketOrder(MarketBuilding m, MarketOrder o) {
		marketTransactions.add(new MarketTransaction(m, o));
		((MarketCustomerDeliveryPaymentRole) roles.get(0)).setMarket(m);
	}
	
//  Scheduler
//	=====================================================================
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean runScheduler() {
//		if (transactions.size() == 0) return true; // Solved an issue I encountered, can't remember exactly?
		
		boolean blocking = false;
		for (Role r : roles) if (r.getActive() && r.getActivity()) {
			blocking  = true;
			boolean activity = r.runScheduler();
			if (!activity) {
				r.setActivityFinished();
			}
			break;
		}
		
		// TODO handle nested actions
		
		if (workingState == WorkingState.GoingOffShift) {
			if (restaurant.cashier != this)
				workingState = WorkingState.NotWorking;
		}
		
		if (restaurant.getCash() > 1000)
			depositMoney();
		
		// Scheduler disposition		
		synchronized(transactions) {
			for (Transaction t : transactions) {
				if (t.s == TransactionState.Pending) {
					computeBill(t);
					return true;
				}
			}
		}
		synchronized(transactions) {
			for (Transaction t : transactions) {
				if (t.s == TransactionState.ReceivedPayment) {
					processPayment(t);
					return true;
				}
			}	
		}
		
		synchronized(transactions) {
			for (Transaction t : transactions) {
				if (t.s == TransactionState.Done) {
					removeTransaction(t);
					return true;
				}
			}
		}
		
		synchronized(transactions) {
			for (Transaction t : transactions) {
				if (t.s == TransactionState.InsufficientPayment) {
					notifyHostOfFlake(t);
					return true;
				}
			}
		}
		
		if (marketTransactions.size() == 0 && workingState == WorkingState.NotWorking)
			super.setInactive();

		return blocking;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	//  Actions
//	=====================================================================	
	private void depositMoney() {
		restaurant.bankCustomer.setActive(Application.BANK_SERVICE.atmDeposit, restaurant.getCash()-1000, Application.TRANSACTION_TYPE.business);
		// TODO how does this work with different bank customer instances when the account number is tied to the role?
	}
	
	private void computeBill(Transaction t) {
		print("Calculating bill");
		t.s = TransactionState.Calculating;
		t.w.msgHereIsBill(t.c, t.price);
	}
	
	private void processPayment(Transaction t) {
		print("Processing payment");
		if (t.payment < t.price) {
			t.s = TransactionState.InsufficientPayment;
			t.c.msgHereIsChange(0);
			return;
		}
		
		t.s = TransactionState.Done;
		if (restaurant.getCash() >= (t.payment-t.price)) t.c.msgHereIsChange(t.payment-t.price);
//		t.c.msgHereIsChange(t.payment-t.price);
		// else, what happens when cashier does not have enough money?
		//TODO RYAN: don't worry about it; if he gives you extra cash you don't need money to give change; imagine debit?
	}
	
	private void removeTransaction(Transaction t) {
		print("Removing order");
		removeOrderFromList(t);
	}
	
	private void notifyHostOfFlake(Transaction t) {
		print("Notifying host of flake");
		t.s = TransactionState.NotifiedHost;
		host.msgFlakeAlert(t.c, t.price-t.payment);
		
	}

//	Utilities
//	=====================================================================	
	public void setRestaurant(RestaurantChungBuilding restaurant) {
		this.restaurant = restaurant;
	}
	
	public void setHost(RestaurantChungHost host) {
		this.host = host;
	}
	
	public Transaction findTransaction(RestaurantChungCustomer c) {
		for(Transaction t: transactions) {
			if(t.c == c) {
				return t;
			}
		}
		return null;
	}
	
	public MarketTransaction findMarketTransaction(int id) {
		for(MarketTransaction t: marketTransactions) {
			if(t.order.orderId == id) {
				return t;
			}
		}
		return null;
	}
	
	public void removeOrderFromList(Transaction transaction) {
		for(Transaction t: transactions) {
			if(t == transaction) {
				transactions.remove(t);
				return;
			}
		}
	}
	
	public void setMarketCustomerDeliveryPaymentPerson() {
		roles.get(0).setPerson(super.getPerson());
	}

	public void setBankCustomerPerson() {
		restaurant.bankCustomer.setPerson(super.getPerson());
	}
	
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
		return (MarketCustomerDeliveryPayment) roles.get(0); // TODO clean up
	}
	
	public int checkBill(MarketTransaction t) {
		int tempBill = 0;
        for (FOOD_ITEMS item: t.order.orderItems.keySet()) {
        	tempBill += t.order.orderItems.get(item)*t.market.prices.get(item);
        }

        if (tempBill == t.bill)
        	return t.bill;
        
		return -1;
	}
}