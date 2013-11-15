package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketDeliveryPerson;
import city.interfaces.MarketEmployee;
import city.Role;

public class MarketCashierRole extends Role implements MarketCashier {

//  Data
//	=====================================================================	
	private MarketBuilding market;
	
	double money;
	
	private List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());
	private class Transaction {
		MarketEmployee employee;
		MarketCustomer customer;
		MarketCustomerDelivery customerDelivery;
		Map<String, Integer> order;
		Map<String, Integer> collectedItems;
		double bill;
		double payment;
		TransactionState s;
		
		public Transaction(MarketEmployee e, MarketCustomer c, Map<String, Integer> o, Map<String, Integer> i) {
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
		
		public Transaction(MarketEmployee e, MarketCustomerDelivery c, Map<String, Integer> o, Map<String, Integer> i) {
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

	private List<MyDeliveryPerson> deliveryPeople = Collections.synchronizedList(new ArrayList<MyDeliveryPerson>());
	private class MyDeliveryPerson {
		MarketDeliveryPerson deliveryPerson;
		boolean available;
		
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
	public MarketCashierRole(MarketBuilding market, double money) {
		super();
		this.market = market;
		this.money = money;
    }
	
//  Messages
//	=====================================================================	
//	Customer (In Person)
//	---------------------------------------------------------------
	public void msgComputeBill(MarketEmployee e, MarketCustomer c, Map<String, Integer> order, Map<String, Integer> collectedItems) {
		System.out.println("Market customer received msgComputeBill");
		transactions.add(new Transaction(e, c, order, collectedItems));
		stateChanged();
	}
	
	public void msgHereIsPayment(MarketCustomer c, double money) {
		Transaction t = findTransaction(c);
		t.payment = money;
		t.s = TransactionState.ReceivedPayment;
		stateChanged();
	}

	
//	Customer (Delivery)
//	---------------------------------------------------------------
	public void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c, Map<String, Integer> order, Map<String, Integer> collectedItems) {
		System.out.println("Market customer received msgComputeBill for Delivery");
		transactions.add(new Transaction(e, c, order, collectedItems));		
		stateChanged();
	}
	
	public void msgHereIsPayment(MarketCustomerDelivery c, double money) {
		Transaction t = findTransaction(c);
		t.payment = money;
		t.s = TransactionState.ReceivedPayment;		
		stateChanged();
	}

//	Delivery Truck
//	---------------------------------------------------------------
	public void msgDeliveringItems(MarketDeliveryPerson d) {
		MyDeliveryPerson deliveryTruck = findTruck(d);
		deliveryTruck.available = false;
	}
	
	public void msgFinishedDeliveringItems(MarketDeliveryPerson d) {
		MyDeliveryPerson deliveryTruck = findTruck(d);
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
<<<<<<< HEAD
        	t.bill += t.collectedItems.get(s)*market.prices.get(s);
=======
        	// t.bill += t.collectedItems.get(s)*prices.get(s);
>>>>>>> a893fa42ee2cc0e35972aebd933a142b6347b5aa
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
			for(MyDeliveryPerson dt : deliveryPeople ){
				if(dt.available == true) {
					dt.deliveryPerson.msgDeliverOrder(t.customerDelivery, t.collectedItems);
				}
			}
		}
		transactions.remove(t);
			
	}
	
	
	// Getters
	
	// Setters
	
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
	
	private MyDeliveryPerson findTruck(MarketDeliveryPerson d) {
		for(MyDeliveryPerson t : deliveryPeople){
			if(t.deliveryPerson == d) {
				return t;		
			}
		}
		return null;
	}
	// Classes
}
