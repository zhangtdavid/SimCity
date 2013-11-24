package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.animations.interfaces.MarketAnimatedCashier;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketDeliveryPerson;
import city.interfaces.MarketEmployee;
import city.Application.FOOD_ITEMS;
import city.Role;

public class MarketCashierRole extends Role implements MarketCashier {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	public MarketBuilding market;
	
	public enum WorkingState
	{Working, GoingOffShift, NotWorking};
	WorkingState workingState = WorkingState.Working;
		
	public List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());
	public class Transaction {
		MarketEmployee employee;
		MarketCustomer customer;
		MarketCustomerDelivery customerDelivery;
		MarketCustomerDeliveryPayment customerDeliveryPayment;
		Map<FOOD_ITEMS, Integer> order = new HashMap<FOOD_ITEMS,Integer>();
		Map<FOOD_ITEMS, Integer> collectedItems = new HashMap<FOOD_ITEMS,Integer>();
		int orderId;
		public int bill;
		public int payment;
		public TransactionState s;
		
		public Transaction(MarketEmployee e, MarketCustomer c, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id) {
			employee = e;
			customer = c;
			customerDelivery = null;
			customerDeliveryPayment = null;
	        for (FOOD_ITEMS s: o.keySet()) {
	        	order.put(s, o.get(s)); // copies all values in customer's order
	        }
	        for (FOOD_ITEMS s: i.keySet()) {
	        	collectedItems.put(s, i.get(s)); // copies all values in collected items
	        }
	        orderId = id;
	        bill = 0;
	        payment = 0;
	        s = TransactionState.Pending;
	    }
		
		public Transaction(MarketEmployee e, MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id) {
			employee = e;
			customer = null;
			customerDelivery = c;
			customerDeliveryPayment = cPay;
	        for (FOOD_ITEMS s: o.keySet()) {
	        	order.put(s, o.get(s)); // copies all values in customer's order
	        }
	        for (FOOD_ITEMS s: i.keySet()) {
	        	collectedItems.put(s, i.get(s)); // copies all values in collected items
	        }
	        orderId = id;
	        bill = 0;
	        payment = 0;
	        s = TransactionState.Pending;
	    }
	}
	public enum TransactionState
	{Pending, Calculating, ReceivedPayment, PendingDelivery, Delivering};

	public List<MyDeliveryPerson> deliveryPeople = Collections.synchronizedList(new ArrayList<MyDeliveryPerson>());
	public class MyDeliveryPerson {
		MarketDeliveryPerson deliveryPerson;
		public boolean available;
		
		public MyDeliveryPerson(MarketDeliveryPerson d) {
			deliveryPerson = d;
			available = true;
		}
	}
	
//	Gui
//	---------------------------------------------------------------
	private MarketAnimatedCashier marketCashierGui;

//	Constructor
//	---------------------------------------------------------------
	public MarketCashierRole(MarketBuilding b, int t1, int t2) {
		super();
		market = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(MarketBuilding.getWorkerSalary());
	}
	
	public void setActive(){
		this.setActivityBegun();
	}
	
	public void setInactive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================	
//	Market
//	---------------------------------------------------------------
	public void msgNewDeliveryPerson(MarketDeliveryPerson d) {
		log.add(new LoggedEvent("Market Cashier received msgNewDeliveryPerson from Market."));
		System.out.println("Market Cashier received msgNewDeliveryPerson from Market.");
		deliveryPeople.add(new MyDeliveryPerson(d));
		stateChanged();
	}
	
	public void msgRemoveDeliveryPerson(MarketDeliveryPerson d) {
		log.add(new LoggedEvent("Market Cashier received msgRemoveDeliveryPerson from Market."));
		System.out.println("Market Cashier received msgRemoveDeliveryPerson from Market.");
		MyDeliveryPerson dp = findDeliveryPerson(d);
		deliveryPeople.remove(dp);
		stateChanged();
	}
	
//	Employee
//	---------------------------------------------------------------
	public void msgComputeBill(MarketEmployee e, MarketCustomer c, Map<FOOD_ITEMS, Integer> order, Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("Market Cashier received msgComputeBill from Employee."));
		System.out.println("Market Cashier received msgComputeBill from Employee.");
		if (workingState != WorkingState.NotWorking) {
			transactions.add(new Transaction(e, c, order, collectedItems, id));		
			stateChanged();			
		}
	}
	
	public void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> order, Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("Market Cashier received msgComputeBill from Employee."));
		System.out.println("Market Cashier received msgComputeBill from Employee");
		if (workingState != WorkingState.NotWorking) {
			transactions.add(new Transaction(e, c, cPay, order, collectedItems, id));		
			stateChanged();			
		}
	}

//	Customer
//	---------------------------------------------------------------	
	public void msgHereIsPayment(int id, int money) {
		log.add(new LoggedEvent("Market Cashier received msgHereIsPayment from Customer Payment for " + money));
		System.out.println("Market Cashier received msgHereIsPayment from Customer Payment for " + money);
		Transaction t = findTransaction(id);
		t.payment = money;
		t.s = TransactionState.ReceivedPayment;		
		stateChanged();
	}

//	Delivery Truck
//	---------------------------------------------------------------
	public void msgDeliveringItems(MarketDeliveryPerson d) {
		log.add(new LoggedEvent("Market Cashier received msgDeliveringItems from Delivery Person."));
		System.out.println("Market Cashier received msgDeliveringItems from Delivery Person.");
		MyDeliveryPerson dp = findDeliveryPerson(d);
		dp.available = false;
	}
	
	public void msgFinishedDeliveringItems(MarketDeliveryPerson d, int id) {
		log.add(new LoggedEvent("Market Cashier received msgFinishedDeliveringItems from Delivery Person."));
		System.out.println("Market Cashier received msgFinishedDeliveringItems from Delivery Person.");
		Transaction t = findTransaction(id);
		transactions.remove(t);
		MyDeliveryPerson dp = findDeliveryPerson(d);
		dp.available = true;
	}
	
//  Scheduler
//	=====================================================================

	@Override
	public boolean runScheduler() {
		if (workingState == WorkingState.GoingOffShift) {
			if (market.cashier != this)
				workingState = WorkingState.NotWorking;
		}
		
		if (transactions.size() == 0 && workingState == WorkingState.NotWorking)
			super.setInactive();
		
		if (market.getCash() > 1000)
			// msg bank
		
		synchronized(transactions) {
			for (Transaction t : transactions) {
				if (t.s == TransactionState.PendingDelivery) {
					for(MyDeliveryPerson dt : deliveryPeople ){
						if(dt.available == true) {
							assignDelivery(t, dt);
							return true;
						}
					}
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
		
		return false;
	}
	
//  Actions
//	=====================================================================	
	private void computeBill(Transaction t) {
		t.s = TransactionState.Calculating;

		for (FOOD_ITEMS s: t.collectedItems.keySet()) {
        	t.bill += t.collectedItems.get(s)*market.prices.get(s);
        }
        // TODO notify customer if there is a difference between order and collected items

		if(t.customer != null) {
			t.customer.msgHereIsOrderandBill(t.collectedItems, t.bill, t.orderId);			
		}
		else
			t.customerDeliveryPayment.msgHereIsBill(this, t.bill, t.orderId);
			
	}
	
	private void processPayment(Transaction t) {
		// TODO handle non norm when payment is not enough
		// TODO manage giving change
		t.s = TransactionState.PendingDelivery;
		
		if (t.customer != null){
			t.customer.msgPaymentReceived();
			market.setCash(market.getCash() + t.payment);
			transactions.remove(t);
		}
		else {
			if(t.bill == t.payment) {
				t.customerDeliveryPayment.msgPaymentReceived(t.orderId);
				market.setCash(market.getCash() + t.payment);
				for(MyDeliveryPerson dt : deliveryPeople ) {
					if(dt.available == true) {
						assignDelivery(t, dt);
					}
				}
			}

		}
	}
	
	private void assignDelivery(Transaction t, MyDeliveryPerson dt) {
		t.s = TransactionState.Delivering;
		dt.deliveryPerson.msgDeliverOrder(t.customerDelivery, t.collectedItems, t.orderId);
	}
	
//  Getters and Setters
//	=====================================================================
	// Market
	public MarketBuilding getMarket() {
		return market;
	}
	
	public void setMarket(MarketBuilding market) {
		this.market = market;
	}
		
//  Utilities
//	=====================================================================
	private Transaction findTransaction(int id) {
		for(Transaction t : transactions){
			if(t.orderId == id) {
				return t;
			}
		}
		return null;
	}
	
	private Transaction findTransaction(MarketCustomer c) {
		for(Transaction t : transactions){
			if(t.customer == c) {
				return t;
			}
		}
		return null;
	}
	
	private Transaction findTransaction(MarketCustomerDelivery c) {
		for(Transaction t : transactions){
			if(t.customerDelivery == c) {
				return t;		
			}
		}
		return null;
	}
	
	private Transaction findTransaction(MarketCustomerDeliveryPayment c) {
		for(Transaction t : transactions){
			if(t.customerDeliveryPayment == c) {
				return t;		
			}
		}
		return null;
	}
	
	private MyDeliveryPerson findDeliveryPerson(MarketDeliveryPerson d) {
		for(MyDeliveryPerson t : deliveryPeople){
			if(t.deliveryPerson == d) {
				return t;		
			}
		}
		return null;
	}
	// Classes
}
