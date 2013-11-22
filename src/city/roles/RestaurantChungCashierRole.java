package city.roles;

import java.util.*;

import city.Role;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungHost;
import city.interfaces.RestaurantChungMarket;
import utilities.EventLog;
import utilities.LoggedEvent;
/**
 * Restaurant Cook Agent
 */
// A Cashier calculates and processes customers' bills

public class RestaurantChungCashierRole extends Role implements RestaurantChungCashier {	
	public EventLog log = new EventLog();

	Timer timer = new Timer();
	private RestaurantChungHost host;
	public Double money;
	
//	Transactions
//	=====================================================================	
	public List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());
	public class Transaction {
		RestaurantChungWaiterRoleBase w;
		RestaurantChungCustomer c;
		String choice;
		public double price;
		public double payment;
		public TransactionState s;
		
		public Transaction(RestaurantChungWaiterRoleBase w2, RestaurantChungCustomer customer, String order, TransactionState state) {
			w = w2;
			c = customer;
			choice = order;
			price = prices.get(choice);
			payment = 0;
			s = state;
		}
	}
		
	public enum TransactionState
	{Pending, Calculating, ReceivedPayment, InsufficientPayment, NotifiedHost, Done};
	
	public List<MarketTransaction> marketTransactions = Collections.synchronizedList(new ArrayList<MarketTransaction>());
	public class MarketTransaction {
		RestaurantChungMarket m;
		int ID;
		public Double bill;
		public TransactionState s;
		
		public MarketTransaction (RestaurantChungMarket market, int id, Double bill, TransactionState state) {
			m = market;
			ID = id;
			this.bill = bill;
			s = state;
		}
	}
	
	private Map<String, Double> prices = new HashMap<String, Double>();
	
//	Constructor
//	=====================================================================		
	public RestaurantChungCashierRole() {
		super();
		money = 500.0;
		// Add items and their prices to a map
		prices.put("Steak", 15.99);
		prices.put("Chicken", 10.99);
		prices.put("Salad", 5.99);
		prices.put("Pizza", 8.99);
	}

//  Messages
//	=====================================================================
	public void msgComputeBill(RestaurantChungWaiterRoleBase w, RestaurantChungCustomer c, String order) {
		print("Cashier received msgComputeBill");
		log.add(new LoggedEvent("Cashier received msgComputeBill. For order " + order));
		transactions.add(new Transaction(w, c, order, TransactionState.Pending));
		stateChanged();
	}
	
	public void msgHereIsPayment(RestaurantChungCustomer c, double money) {
		print("Cashier received msgHereIsPayment");
		log.add(new LoggedEvent("Cashier received msgHereIsPayment. For amount of " + money));
		Transaction t = findTransaction(c);
		t.payment = money;
		this.money += money;
		t.s = TransactionState.ReceivedPayment;
		stateChanged();
	}
	
	public void msgMarketOrderBill (RestaurantChungMarket m, int id, double bill) {
		print("Cashier received msgMarketOrderBill");
		log.add(new LoggedEvent("Cashier received msgMarketOrderBill. For amount of " + bill));
		marketTransactions.add(new MarketTransaction(m, id, bill, TransactionState.Pending));
		stateChanged();
	}
	
//  Scheduler
//	=====================================================================
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
//		if (transactions.size() == 0) return true; // Solved an issue I encountered, can't remember exactly?

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
		
		synchronized(marketTransactions) {
			for (MarketTransaction mt : marketTransactions) {
				if (mt.s == TransactionState.Pending) {
					payMarket(mt);
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

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	//  Actions
//	=====================================================================	
	private void computeBill(Transaction t) {
		print("Calculating bill");
		t.s = TransactionState.Calculating;
		t.w.msgHereIsBill(t.c, t.price);
	}
	
	private void processPayment(Transaction t) {
		print("Processing payment");
		if (t.payment < t.price) {
			t.s = TransactionState.InsufficientPayment;
			t.c.msgHereIsChange(0.0);
			return;
		}
		
		t.s = TransactionState.Done;
		if (money >= (t.payment-t.price)) t.c.msgHereIsChange(t.payment-t.price);
//		t.c.msgHereIsChange(t.payment-t.price);
		// else, what happens when cashier does not have enough money?
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
	
	private void payMarket(MarketTransaction mt) {
		if (money >= mt.bill) {
			print("Paying market " + mt.bill);
			mt.m.msgHereIsPayment(mt.ID, mt.bill);
			money -= mt.bill;
			removeMarketTransactionFromList(mt);
		}
		// else, what happens when cashier does not have enough money?
//		else {
//			print("Not enough money to pay market " + mt.bill);
//		}
	}

//	Utilities
//	=====================================================================	
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
	
	public void removeOrderFromList(Transaction transaction) {
		for(Transaction t: transactions) {
			if(t == transaction) {
				transactions.remove(t);
				return;
			}
		}
	}
	
	public void removeMarketTransactionFromList(MarketTransaction transaction) {
		for(MarketTransaction mt: marketTransactions) {
			if(mt == transaction) {
				marketTransactions.remove(mt);
				return;
			}
		}
	}
}