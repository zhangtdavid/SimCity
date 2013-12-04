package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.buildings.MarketBuilding;
import city.interfaces.BankCustomer;
import city.interfaces.Market;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketDeliveryPerson;
import city.interfaces.MarketEmployee;

public class MarketCashierRole extends Role implements MarketCashier {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private MarketBuilding market;
	private WorkingState workingState = WorkingState.Working;
	
	private BankCustomer bankCustomer;
	private List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());
	private List<MyDeliveryPerson> deliveryPeople = Collections.synchronizedList(new ArrayList<MyDeliveryPerson>());
	

//	Constructor
//	=====================================================================
	public MarketCashierRole(MarketBuilding b, int t1, int t2) {
		super();
		market = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(MarketBuilding.getWorkerSalary());
		bankCustomer = b.bankCustomer;
	}

//	Activity
//	=====================================================================
//	// TODO schung 99c0f4da25
//	public void setActive() {
//		super.setActivityBegun();
//		super.setActive();
//	}
	
	@Override
	public void setInactive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================	
	
//	Market
//	---------------------------------------------------------------
	@Override
	public void msgNewDeliveryPerson(MarketDeliveryPerson d) {
		log.add(new LoggedEvent("Market Cashier received msgNewDeliveryPerson from Market."));
		System.out.println("Market Cashier received msgNewDeliveryPerson from Market.");
		deliveryPeople.add(new MyDeliveryPerson(d));
		stateChanged();
	}
	
	@Override
	public void msgRemoveDeliveryPerson(MarketDeliveryPerson d) {
		log.add(new LoggedEvent("Market Cashier received msgRemoveDeliveryPerson from Market."));
		System.out.println("Market Cashier received msgRemoveDeliveryPerson from Market.");
		MyDeliveryPerson dp = findDeliveryPerson(d);
		deliveryPeople.remove(dp);
		stateChanged();
	}
	
//	Employee
//	---------------------------------------------------------------
	@Override
	public void msgComputeBill(MarketEmployee e, MarketCustomer c, Map<FOOD_ITEMS, Integer> order, Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("Market Cashier received msgComputeBill from Employee."));
		System.out.println("Market Cashier received msgComputeBill from Employee.");
		if (workingState != WorkingState.NotWorking) {
			transactions.add(new Transaction(e, c, order, collectedItems, id));		
			stateChanged();			
		}
		// TODO inform sender of inactivity
	}
	
	@Override
	public void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> order, Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("Market Cashier received msgComputeBill from Employee."));
		System.out.println("Market Cashier received msgComputeBill from Employee");
		if (workingState != WorkingState.NotWorking) {
			transactions.add(new Transaction(e, c, cPay, order, collectedItems, id));		
			stateChanged();			
		}
		// TODO inform sender of inactivity
	}

//	Customer
//	---------------------------------------------------------------	
	@Override
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
	@Override
	public void msgDeliveringItems(MarketDeliveryPerson d) {
		log.add(new LoggedEvent("Market Cashier received msgDeliveringItems from Delivery Person."));
		System.out.println("Market Cashier received msgDeliveringItems from Delivery Person.");
		MyDeliveryPerson dp = findDeliveryPerson(d);
		dp.available = false;
	}
	
	@Override
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
		// Role Scheduler
		boolean blocking = false;
		if (market.bankCustomer.getActive() && market.bankCustomer.getActivity()) {
			blocking  = true;
			boolean activity = market.bankCustomer.runScheduler();
			if (!activity) {
				market.bankCustomer.setActivityFinished();
			}
		}
		
		if (workingState == WorkingState.GoingOffShift) {
			if (market.cashier != this)
				workingState = WorkingState.NotWorking;
		}
		
		if (market.getCash() > 1000)
			depositMoney();
		
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
		
		if (workingState == WorkingState.NotWorking)
			super.setInactive();
		
		return blocking;
	}
	
//  Actions
//	=====================================================================	
	private void depositMoney() {
		market.bankCustomer.setActive(Application.BANK_SERVICE.atmDeposit, market.getCash()-1000, Application.TRANSACTION_TYPE.business);
	}
	
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
			t.customerDeliveryPayment.msgHereIsBill(t.bill, t.orderId);
			
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
	
//  Getters
//	=====================================================================
	@Override
	public Market getMarket() {
		return market;
	}
	
	@Override
	public BankCustomer getBankCustomer() {
		return bankCustomer;
	}

	@Override
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	@Override	
	public List<MyDeliveryPerson> getDeliveryPeople() {
		return deliveryPeople;
	}
	
//  Setters
//	=====================================================================	
	@Override
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
	
	private MyDeliveryPerson findDeliveryPerson(MarketDeliveryPerson d) {
		for(MyDeliveryPerson t : deliveryPeople){
			if(t.deliveryPerson == d) {
				return t;		
			}
		}
		return null;
	}
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketCashierRole " + this.getPerson().getName(), msg);
    }
	
//  Classes 
//	=====================================================================	
	public class MyDeliveryPerson {
		MarketDeliveryPerson deliveryPerson;
		public boolean available;
		
		public MyDeliveryPerson(MarketDeliveryPerson d) {
			deliveryPerson = d;
			available = true;
		}
	}
	
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

}
