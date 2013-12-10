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
import utilities.MarketTransaction.MarketTransactionState;
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
import city.roles.interfaces.RestaurantChungWaiter;

public class RestaurantChungCashierRole extends JobRole implements RestaurantChungCashier {
//	Data
//	=====================================================================	
	public EventLog log = new EventLog();
	Timer timer = new Timer();
	
	private RestaurantChung restaurant;
	
	private List<Role> roles = new ArrayList<Role>();
	private List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());
	private List<MarketTransaction> marketTransactions = Collections.synchronizedList(new ArrayList<MarketTransaction>());

	WorkingState workingState = WorkingState.Working;
	
//	Constructor
//	=====================================================================
	public RestaurantChungCashierRole(RestaurantChung b, int t1, int t2) {
		super();
		restaurant = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChungBuilding.WORKER_SALARY);
		roles.add(new MarketCustomerDeliveryPaymentRole(restaurant, marketTransactions, this));
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
	public void msgComputeBill(RestaurantChungWaiter w, RestaurantChungCustomer c, FOOD_ITEMS order) {
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
		stateChanged();
	}
	
//	public void msgReceivedBill() {
//		stateChanged();
//	}
	
//  Scheduler
//	=====================================================================
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	public boolean runScheduler() {
//		if (transactions.size() == 0) return true; // Solved an issue I encountered, can't remember exactly?
		
		boolean blocking = false;
		for (Role r : roles) if (r.getActive()) {
			blocking  = true;
			r.runScheduler();
		}
				
		if (workingState == WorkingState.GoingOffShift) {
			if (restaurant.getRestaurantChungCashier() != this)
				workingState = WorkingState.NotWorking;
		}
		
		if (restaurant.getCash() > 1000)
			depositMoney();
		
		// Scheduler disposition
		
		synchronized(marketTransactions) {
			for (MarketTransaction t : marketTransactions) {
				if (t.getMarketTransactionState() == MarketTransactionState.Done) {
					marketTransactions.remove(t);
				}
			}
		}		
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
				if (t.s == TransactionState.InsufficientPayment) {
					notifyHostOfFlake(t);
					return true;
				}
			}
		}
		
		if (workingState == WorkingState.NotWorking && transactions.size() == 0 && marketTransactions.size() == 0)
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
		
		transactions.remove(t);
		if (restaurant.getCash() >= (t.payment-t.price)) t.c.msgHereIsChange(t.payment-t.price);
//		t.c.msgHereIsChange(t.payment-t.price);
		// else, what happens when cashier does not have enough money?
		//TODO RYAN: don't worry about it; if he gives you extra cash you don't need money to give change; imagine debit?
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
	
	@Override
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	@Override
	public List<MarketTransaction> getMarketTransactions() {
		return marketTransactions;
	}
	
//	Setters
//	=====================================================================		
	@Override
	public void setPerson(Person p) {
		super.setPerson(p);
		for (int i = 0; i < roles.size(); i++) {
			roles.get(i).setPerson(this.getPerson());
		}
	}
	
	@Override
	public void setRestaurant(RestaurantChung restaurant) {
		this.restaurant = restaurant;
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
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungCashierRole " + this.getPerson().getName(), msg);
    }

//	Classes
//	=====================================================================
	public class Transaction {
		RestaurantChungWaiter w;
		RestaurantChungCustomer c;
		FOOD_ITEMS choice;
		private int price;
		private int payment;
		private TransactionState s;
		
		public Transaction(RestaurantChungWaiter w2, RestaurantChungCustomer customer, FOOD_ITEMS order, TransactionState state) {
			w = w2;
			c = customer;
			choice = order;
			price = restaurant.getFoods().get(choice).getPrice();
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