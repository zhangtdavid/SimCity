package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import utilities.MarketTransaction;
import city.Application;
import city.Application.FOOD_ITEMS;
import city.agents.interfaces.Person;
import city.bases.JobRole;
import city.bases.Role;
import city.buildings.RestaurantChungBuilding;
import city.buildings.interfaces.Market;
import city.buildings.interfaces.RestaurantChung;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.RestaurantChungCashier;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungHost;
import city.roles.interfaces.RestaurantChungWaiter;

public class RestaurantChungCashierRole extends JobRole implements RestaurantChungCashier {
//	Data
//	=====================================================================	
	public EventLog log = new EventLog();
	Timer timer = new Timer();
	
	private RestaurantChung restaurant;
	private RestaurantChungHost host;
	
	private List<Role> roles = new ArrayList<Role>();
	public List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());
	public List<MarketTransaction> marketTransactions = Collections.synchronizedList(new ArrayList<MarketTransaction>());

	WorkingState workingState = WorkingState.Working;
	
//	Constructor
//	=====================================================================
	public RestaurantChungCashierRole(RestaurantChung b, int t1, int t2) {
		super();
		restaurant = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChungBuilding.WORKER_SALARY);
		roles.add(new MarketCustomerDeliveryPaymentRole(restaurant, marketTransactions));
		roles.get(0).setActive();
		roles.add((Role) restaurant.getBankCustomer());
		roles.get(1).setActive();
	}

//	Activity
//	=====================================================================	
	@Override
	public void setInactive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================
//	Waiter
//	---------------------------------------------------------------
	@Override
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
	@Override
	public void msgHereIsPayment(RestaurantChungCustomer c, int money) {
		print("Cashier received msgHereIsPayment");
		log.add(new LoggedEvent("Cashier received msgHereIsPayment. For amount of " + money));
		Transaction t = findTransaction(c);
		t.payment = money;
		restaurant.setCash(restaurant.getCash() + money);
		t.s = TransactionState.ReceivedPayment;
		stateChanged();
	}

//	Cook
//	---------------------------------------------------------------
	@Override
	public void msgAddMarketOrder(Market m, MarketOrder o) {
		print("Cashier received msgAddMarketOrder");
		log.add(new LoggedEvent("Cashier received msgAddMarketOrder."));
		marketTransactions.add(new MarketTransaction(m, o));
		((MarketCustomerDeliveryPaymentRole) roles.get(0)).setMarket(m);
	}
	
//  Scheduler
//	=====================================================================
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
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
				
		if (workingState == WorkingState.GoingOffShift) {
			if (restaurant.getRestaurantChungCashier() != this)
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
		print(restaurant.getBankCustomer().toString());
		restaurant.getBankCustomer().setActive(Application.BANK_SERVICE.atmDeposit, restaurant.getCash()-1000, Application.TRANSACTION_TYPE.business);
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
		restaurant.getRestaurantChungHost().msgFlakeAlert(t.c, t.price-t.payment);
	}
	
//	Getters
//	=====================================================================	
	@Override
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
		return (MarketCustomerDeliveryPayment) roles.get(0); // TODO clean up
	}
	
//	Setters
//	=====================================================================		
	@Override
	public void setPerson(Person p) {
		super.setPerson(p);
		roles.get(0).setPerson(this.getPerson());
		roles.get(1).setPerson(this.getPerson());
	}
	
	@Override
	public void setRestaurant(RestaurantChung restaurant) {
		this.restaurant = restaurant;
	}
	
	@Override
	public void setHost(RestaurantChungHost host) {
		this.host = host;
	}
	
	@Override
	public void setMarketCustomerDeliveryPaymentPerson() {
		roles.get(0).setPerson(super.getPerson());
	}

	@Override
	public void setBankCustomerPerson() {
		restaurant.getBankCustomer().setPerson(super.getPerson());
	}	
//	Utilities
//	=====================================================================
	@Override
	public int checkBill(MarketTransaction t) {
		int tempBill = 0;
        for (FOOD_ITEMS item: t.getOrder().getOrderItems().keySet()) {
        	tempBill += t.getOrder().getOrderItems().get(item)*t.getMarket().getPrices().get(item);
        }

        if (tempBill == t.getBill())
        	return t.getBill();
        
		return -1;
	}
	
	@Override
	public Transaction findTransaction(RestaurantChungCustomer c) {
		for(Transaction t: transactions) {
			if(t.c == c) {
				return t;
			}
		}
		return null;
	}
	
	@Override
	public MarketTransaction findMarketTransaction(int id) {
		for(MarketTransaction t: marketTransactions) {
			if(t.getOrder().getOrderId() == id) {
				return t;
			}
		}
		return null;
	}
	
	@Override
	public void removeOrderFromList(Transaction transaction) {
		for(Transaction t: transactions) {
			if(t == transaction) {
				transactions.remove(t);
				return;
			}
		}
	}
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungCashierRole " + this.getPerson().getName(), msg);
    }

//	Classes
//	=====================================================================
	public class Transaction {
		RestaurantChungWaiter w;
		RestaurantChungCustomer c;
		String choice;
		private int price;
		private int payment;
		private TransactionState s;
		
		public Transaction(RestaurantChungWaiter w2, RestaurantChungCustomer customer, String order, TransactionState state) {
			w = w2;
			c = customer;
			choice = order;
			price = restaurant.getFoods().get(FOOD_ITEMS.valueOf(choice)).getPrice();
			payment = 0;
			s = state;
		}
		
		// Getters
		public int getPrice() {
			return price;
		}
		
		public int getPayment() {
			return payment;
		}
		
		public TransactionState getTransactionState() {
			return s;
		}
		
		// Setters
		public void setPrice(int price) {
			this.price = price;
		}
		
		public void setPayment(int payment) {
			this.payment = payment;
		}		
	}
}