package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import city.interfaces.MarketCashier;
import city.Role;

public class MarketCashierRole extends Role implements MarketCashier {

//  Data
//	=====================================================================	
//	Market market; TODO
	
	double money;
	

	private List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());
	private class Transaction {
		MarketEmployeeRole employee;
		MarketCustomerRole customer;
		MarketCustomerDeliveryRole customerDelivery;
		Map<String, Integer> order;
		Map<String, Integer> collectedItems;
		double bill;
		double payment;
		TransactionState s;
		
		public Transaction(MarketEmployeeRole e, MarketCustomerRole c, Map<String, Integer> o, Map<String, Integer> i) {
			employee = e;
			customer = c;
			customerDelivery = null;
	        for (String s: o.keySet()) {
	        	order.put(s, o.get(s)); // initialize all values in collectedItems to 0
	        }
	        for (String s: i.keySet()) {
	        	order.put(s, i.get(s)); // initialize all values in collectedItems to 0
	        }
	        bill = 0;
	        payment = 0;
	        s = TransactionState.Pending;
	    }
		
		public Transaction(MarketEmployeeRole e, MarketCustomerDeliveryRole c, Map<String, Integer> o, Map<String, Integer> i) {
			employee = e;
			customer = null;
			customerDelivery = c;
	        for (String s: o.keySet()) {
	        	order.put(s, o.get(s)); // initialize all values in collectedItems to 0
	        }
	        for (String s: i.keySet()) {
	        	order.put(s, i.get(s)); // initialize all values in collectedItems to 0
	        }
	    }
	}
	private enum TransactionState
	{Pending, Calculating, ReceivedPayment};

	private List<MyDeliveryTruck> deliveryPeople = Collections.synchronizedList(new ArrayList<MyDeliveryTruck>());
	private class MyDeliveryTruck {
		MarketDeliveryPersonRole deliveryPerson;
		boolean available;
		
		public MyDeliveryTruck(MarketDeliveryPersonRole d) {
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
		super(); // TODO
		money = 1000.0; // TODO
    }
	
//  Messages
//	=====================================================================	
//	Customer (In Person)
//	---------------------------------------------------------------
	public void msgComputeBill(MarketEmployeeRole e, MarketCustomerRole c, Map<String, Integer> order, Map<String, Integer> collectedItems) {
		System.out.println("Market customer received msgComputeBill");
		transactions.add(new Transaction(e, c, order, collectedItems));
		stateChanged();
	}
	
	public void msgHereIsPayment(MarketCustomerRole c, double money) {
		Transaction t = findTransaction(c);
		t.payment = money;
		t.s = TransactionState.ReceivedPayment;
		stateChanged();
	}

	
//	Customer (Delivery)
//	---------------------------------------------------------------
	public void msgComputeBill(MarketEmployeeRole e, MarketCustomerDeliveryRole c, Map<String, Integer> order, Map<String, Integer> collectedItems) {
		System.out.println("Market customer received msgComputeBill for Delivery");
		transactions.add(new Transaction(e, c, order, collectedItems));		
	}
	
	public void msgHereIsPayment(MarketCustomerDeliveryRole c, double money) {
		Transaction t = findTransaction(c);
		t.payment = money;
		t.s = TransactionState.ReceivedPayment;		
	}

//	Delivery Truck
//	---------------------------------------------------------------
	public void msgDeliveringItems(MarketDeliveryPersonRole d) {
		MyDeliveryTruck deliveryTruck = findTruck(d);
		deliveryTruck.available = false;
	}
	
	public void msgFinishedDeliveringItems(MarketDeliveryPersonRole d) {
		MyDeliveryTruck deliveryTruck = findTruck(d);
		deliveryTruck.available = true;
	}
	
	
//  Scheduler
//	=====================================================================

	@Override
	public boolean runScheduler() {
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
        	t.bill += t.collectedItems.get(s)*prices.get(s);
        }
        // notify customer if there is a difference between order and collected items

		if(t.customer != null) {
			t.customer.msgHereIsOrderandBill(t.collectedItems, t.bill);			
		}
		else
			t.customerDelivery.msgHereIsBill(t.bill);
			
	}
	
	private void processPayment(Transaction t) {
		// TODO handle non norm
		if (t.customer != null)
			t.customer.msgPaymentReceived();
		else {
			t.customerDelivery.msgPaymentReceived();
			for(MyDeliveryTruck dt : deliveryPeople ){
				if(dt.available == true) {
					dt.msgDeliverOrder(t.customerDelivery, t.collectedItems);
				}
			}
		}
		transactions.remove(t);
			
	}
	
	
	// Getters
	
	// Setters
	
//  Utilities
//	=====================================================================	
	private Transaction findTransaction(MarketCustomerRole c) {
		for(Transaction t : transactions){
			if(t.customer == c) {
				return t;		
			}
		}
		return null;
	}
	
	private Transaction findTransaction(MarketCustomerDeliveryRole c) {
		for(Transaction t : transactions){
			if(t.customerDelivery == c) {
				return t;		
			}
		}
		return null;
	}
	
	private MyDeliveryTruck findTruck(MarketDeliveryPersonRole d) {
		for(MyDeliveryTruck t : deliveryPeople){
			if(t.deliveryPerson == d) {
				return t;		
			}
		}
		return null;
	}
	// Classes
}
