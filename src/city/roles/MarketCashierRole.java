package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketDeliveryPerson;
import city.interfaces.MarketEmployee;
import city.Role;

public class MarketCashierRole extends Role implements MarketCashier {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	public MarketBuilding market;
		
	public List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());
	public class Transaction {
		MarketEmployee employee;
		MarketCustomer customer;
		MarketCustomerDelivery customerDelivery;
		MarketCustomerDeliveryPayment customerDeliveryPayment;
		Map<String, Integer> order = new HashMap<String,Integer>();
		Map<String, Integer> collectedItems = new HashMap<String,Integer>();
		public int bill;
		public int payment;
		public TransactionState s;
		
		public Transaction(MarketEmployee e, MarketCustomer c, Map<String, Integer> o, Map<String, Integer> i) {
			employee = e;
			customer = c;
			customerDelivery = null;
			customerDeliveryPayment = null;
	        for (String s: o.keySet()) {
	        	order.put(s, o.get(s)); // copies all values in customer's order
	        }
	        for (String s: i.keySet()) {
	        	collectedItems.put(s, i.get(s)); // copies all values in collected items
	        }
	        bill = 0;
	        payment = 0;
	        s = TransactionState.Pending;
	    }
		
		public Transaction(MarketEmployee e, MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<String, Integer> o, Map<String, Integer> i) {
			employee = e;
			customer = null;
			customerDelivery = c;
			customerDeliveryPayment = cPay;
	        for (String s: o.keySet()) {
	        	order.put(s, o.get(s)); // copies all values in customer's order
	        }
	        for (String s: i.keySet()) {
	        	collectedItems.put(s, i.get(s)); // copies all values in collected items
	        }
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
//	private MarketCashierGui marketCashierGui;

//	Constructor
//	---------------------------------------------------------------
	public MarketCashierRole() {
		super();
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
	
//	Customer (In Person)
//	---------------------------------------------------------------
	public void msgComputeBill(MarketEmployee e, MarketCustomer c, Map<String, Integer> order, Map<String, Integer> collectedItems) {
		log.add(new LoggedEvent("Market Cashier received msgComputeBill from Customer In Person."));
		System.out.println("Market Cashier received msgComputeBill from Customer In Person.");
		transactions.add(new Transaction(e, c, order, collectedItems));
		stateChanged();
	}
	
	public void msgHereIsPayment(MarketCustomer c, int money) {
		log.add(new LoggedEvent("Market Cashier received msgHereIsPayment from Customer In Person for " + money));
		System.out.println("Market Cashier received msgHereIsPayment from Customer In Person for " + money);
		Transaction t = findTransaction(c);
		t.payment = money;
		t.s = TransactionState.ReceivedPayment;
		stateChanged();
	}
	
//	Customer (Delivery)
//	---------------------------------------------------------------
	public void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<String, Integer> order, Map<String, Integer> collectedItems) {
		log.add(new LoggedEvent("Market Cashier received msgComputeBill from Customer Delivery."));
		System.out.println("Market Cashier received msgComputeBill from Customer Delivery");
		transactions.add(new Transaction(e, c, cPay, order, collectedItems));		
		stateChanged();
	}
	
	public void msgHereIsPayment(MarketCustomerDeliveryPayment c, int money) {
		log.add(new LoggedEvent("Market Cashier received msgHereIsPayment from Customer Delivery Payment for " + money));
		System.out.println("Market Cashier received msgHereIsPayment from Customer Delivery Payment for " + money);
		Transaction t = findTransaction(c);
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
	
	public void msgFinishedDeliveringItems(MarketDeliveryPerson d, MarketCustomerDelivery cd) {
		log.add(new LoggedEvent("Market Cashier received msgFinishedDeliveringItems from Delivery Person."));
		System.out.println("Market Cashier received msgFinishedDeliveringItems from Delivery Person.");
		Transaction t = findTransaction(cd);
		transactions.remove(t);
		MyDeliveryPerson dp = findDeliveryPerson(d);
		dp.available = true;
	}
	
//  Scheduler
//	=====================================================================

	@Override
	public boolean runScheduler() {
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

		for (String s: t.collectedItems.keySet()) {
        	t.bill += t.collectedItems.get(s)*market.prices.get(s);
        }
        // notify customer if there is a difference between order and collected items

		if(t.customer != null) {
			t.customer.msgHereIsOrderandBill(t.collectedItems, t.bill);			
		}
		else
			t.customerDeliveryPayment.msgHereIsBill(this, t.bill);
			
	}
	
	private void processPayment(Transaction t) {
		// TODO handle non norm when payment is not enough
		// TODO manage giving change
		t.s = TransactionState.PendingDelivery;
		
		if (t.customer != null){
			t.customer.msgPaymentReceived();
			market.money += t.payment;
			transactions.remove(t);
		}
		else {
			t.customerDeliveryPayment.msgPaymentReceived();
			market.money += t.payment;
			for(MyDeliveryPerson dt : deliveryPeople ){
				if(dt.available == true) {
					assignDelivery(t, dt);
				}
			}
		}
	}
	
	private void assignDelivery(Transaction t, MyDeliveryPerson dt) {
		t.s = TransactionState.Delivering;
		dt.deliveryPerson.msgDeliverOrder(t.customerDelivery, t.collectedItems);
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
