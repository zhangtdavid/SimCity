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
import city.agents.interfaces.Person;
import city.bases.JobRole;
import city.buildings.MarketBuilding;
import city.buildings.interfaces.Market;
import city.buildings.interfaces.Market.MyDeliveryPerson;
import city.roles.interfaces.MarketCashier;
import city.roles.interfaces.MarketCustomer;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.MarketDeliveryPerson;
import city.roles.interfaces.MarketEmployee;

public class MarketCashierRole extends JobRole implements MarketCashier {
//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private Market market;
	private WorkingState workingState = WorkingState.Working;
	
	private List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());
	
//	Constructor
//	=====================================================================
	public MarketCashierRole(Market market, int t1, int t2) {
		super();
		this.market = market;
		this.setShift(t1, t2);
		this.setWorkplace(market);
		this.setSalary(MarketBuilding.WORKER_SALARY);
	}

//	Activity
//	=====================================================================	
	@Override
	public void setInactive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================	
//	Employee
//	---------------------------------------------------------------
	@Override
	public void msgComputeBill(MarketEmployee e, MarketCustomer c, Map<FOOD_ITEMS, Integer> order, Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("MarketCashier received msgComputeBill from MarketEmployee."));
		print("MarketCashier received msgComputeBill from MarketEmployee.");
		if (workingState != WorkingState.NotWorking) {
			transactions.add(new Transaction(e, c, order, collectedItems, id));		
			stateChanged();
		}
		// TODO inform sender of inactivity
	}
	
	@Override
	public void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> order, Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("MarketCashier received msgComputeBill from MarketEmployee."));
		print("MarketCashier received msgComputeBill from MarketEmployee");
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
		log.add(new LoggedEvent("MarketCashier received msgHereIsPayment from MarketCustomerDeliveryPayment for " + money));
		print("MarketCashier received msgHereIsPayment from MarketCustomerDeliveryPayment for " + money);
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
		print("Market Cashier received msgDeliveringItems from Delivery Person.");
	}
	
	@Override
	public void msgFinishedDeliveringItems(MarketDeliveryPerson d, int id) {
		log.add(new LoggedEvent("Market Cashier received msgFinishedDeliveringItems from Delivery Person."));
		print("Market Cashier received msgFinishedDeliveringItems from Delivery Person.");
		Transaction t = findTransaction(id);
		transactions.remove(t);
	}
	
//  Scheduler
//	=====================================================================

	@Override
	public boolean runScheduler() {
		// Role Scheduler
		boolean blocking = false;
		if (market.getBankCustomer().getActive() && market.getBankCustomer().getActivity()) {
			blocking  = true;
			boolean activity = market.getBankCustomer().runScheduler();
			if (!activity) {
				market.getBankCustomer().setActivityFinished();
			}
		}
		
		if (workingState == WorkingState.GoingOffShift) {
			if (market.getCashier() != this)
				workingState = WorkingState.NotWorking;
		}
		
		if (market.getCash() > 1000)
			depositMoney();
		
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
		
		if (workingState == WorkingState.NotWorking && transactions.size() == 0)
			super.setInactive();
		
		return blocking;
	}
	
//  Actions
//	=====================================================================	
	private void depositMoney() {
		market.getBankCustomer().setActive(Application.BANK_SERVICE.atmDeposit, market.getCash()-500, Application.TRANSACTION_TYPE.business);
	}
	
	private void computeBill(Transaction t) {
		t.s = TransactionState.Calculating;

		for (FOOD_ITEMS s: t.collectedItems.keySet()) {
        	t.bill += t.collectedItems.get(s)*market.getPrices().get(s);
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
		
		boolean assigned = false;
		
		if (t.customer != null){
			t.customer.msgPaymentReceived();
			market.setCash(market.getCash() + t.payment);
			transactions.remove(t);
		}
		else {
			if(t.bill == t.payment) {
				t.customerDeliveryPayment.msgPaymentReceived(t.orderId);
				market.setCash(market.getCash() + t.payment);
				
				while (!assigned) {
					// Cycles through marketDeliveryPeople, assigns to next person in list if they are working
					if (market.getDeliveryPeople().get(market.getCurrentDeliveryPerson()%market.getDeliveryPeople().size()).getDeliveryPerson().getWorkingState() == MarketDeliveryPerson.WorkingState.Working) {
						assignDelivery(t, market.getDeliveryPeople().get(market.getCurrentDeliveryPerson()));
						assigned = true;
					}
					market.setCurrentDeliveryPerson(market.getCurrentDeliveryPerson()+1);
				}
			}

		}
	}
	
	private void assignDelivery(Transaction t, MyDeliveryPerson dt) {
		dt.getDeliveryPerson().msgDeliverOrder(t.customerDelivery, t.collectedItems, t.orderId);
		t.s = TransactionState.Delivering;
	}
	
//  Getters
//	=====================================================================
	@Override
	public Market getMarket() {
		return market;
	}

	@Override
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	@Override
	public WorkingState getWorkingState() {
		return workingState;
	}
	
//  Setters
//	=====================================================================	
	@Override
	public void setPerson(Person p) {
		super.setPerson(p);
		market.getBankCustomer().setPerson(this.getPerson());
	}
	
	@Override
	public void setMarket(Market market) {
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
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("MarketCashier", msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketCashierRole " + this.getPerson().getName(), msg);
    }
	
//  Classes 
//	=====================================================================		
	public class Transaction {
		// private MarketEmployee employee;
		private MarketCustomer customer;
		private MarketCustomerDelivery customerDelivery;
		private MarketCustomerDeliveryPayment customerDeliveryPayment;
		
		private Map<FOOD_ITEMS, Integer> order = new HashMap<FOOD_ITEMS,Integer>();
		private HashMap<FOOD_ITEMS, Integer> collectedItems = new HashMap<FOOD_ITEMS,Integer>();
		
		private int orderId;
		private int bill;
		private int payment;
		
		private TransactionState s;
		
		// constructor for in person order
		public Transaction(MarketEmployee e, MarketCustomer c, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id) {
			// employee = e;
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
		
		// constructor for delivery order
		public Transaction(MarketEmployee e, MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id) {
			// employee = e;
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
		
		// Getters
		public int getOrderId() {
			return orderId;
		}
		
		public int getBill() {
			return bill;
		}
		
		public int getPayment() {
			return payment;
		}
		
		public TransactionState getTransactionState() {
			return s;
		}
	}

}
