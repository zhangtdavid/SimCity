/*package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.Role;
import city.animations.RestaurantTimmsCashierAnimation;
import city.interfaces.Person;
import city.interfaces.RestaurantTimmsCashier;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsWaiter;
*/
/**
 * Restaurant cashier agent.
 * 
 * @author John Timms
 */
/*public class RestaurantTimmsCashierRole extends Role implements RestaurantTimmsCashier {
	// Data
	
	public int moneyOnHand;
	public int moneyCollected;
	public int moneyOwed;
	private RestaurantTimmsCashierAnimation animation;
	
	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	
	private Integer CASH_MIN = 100;
	private Integer CASH_MAX = 500;
	
	// Constructor
	
	public RestaurantTimmsCashierRole(Person p){
		super(p);
		this.moneyOnHand = CASH_MIN + (int)(Math.random() * ((CASH_MAX - CASH_MIN) + 1));
		this.moneyCollected = 0;
		this.moneyOwed = 0;
		
		print("Money on hand - " + this.moneyOnHand);
	}
	
	// Messages
	
	/**
	 * Receives a request from a Waiter to compute the check for a Customer. Includes the price of
	 * the food that the Customer ordered.
	 * 
	 * @param w a Waiter interface
	 * @param c a Customer interface
	 * @param money the amount billed by the market
	 */
/*
	public void msgComputeCheck(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, int money) {
		Check check = findCheck(c);
		if (check == null) {
			// This is the first time the customer has been here
			print("msgComputeCheck - new check balance of $" + money);
			checks.add(new Check(w, c, money));
		} else {
			// The customer has not been here before, or the customer is paying an old check, plus today's check
			print("msgComputeCheck - previous balance of $" + check.amount + " plus today's $" + money);
			check.state = Check.State.queue;
			check.waiter = w;
			check.addAmount(money);
		}
		moneyOwed = (moneyOwed + money);
		stateChanged();
	}
	*/
	/**
	 * Notifies the Cashier that a Customer is ready to pay. The Customer includes how much money
	 * they have, in order to determine their change.
	 * 
	 * @param c a Customer interface
	 * @param money the amount of money the Customer has
	 */
/*
	public void msgMakePayment(RestaurantTimmsCustomer c, int money) {
		Check check = findCheck(c);
		check.amountOffered = money;
		print("msgMakePayment - offered $" + money + " for bill of $" + check.amount);
		check.state = Check.State.paying;
		stateChanged();
	}
	*/
// TODO
//	/**
//	 * Receives a request from a Market to pay for StockItems ordered by the Cook.
//	 * 
//	 * @param m a Market interface
//	 * @param money the amount billed by the Market
//	 */
//	public void msgPayMarket(Market m, int money) {
//		Bill bill = findBill(m);
//		if (bill == null) {
//			// This is the first time this Market has sent a bill
//			print("msgPayMarket - new bill balance of $" + money);
//			bills.add(new Bill(m, money));
//		} else {
//			// This Market has sent a bill before, or Cashier was unable to pay last time
//			print("msgPayMarket - previous balance of $" + bill.amount + " plus today's $" + money);
//			bill.state = Bill.State.queue;
//			bill.addAmount(money);
//		}
//		stateChanged();
//	}
	
	// Actions
	
	/**
	 * Computes the Check for a Waiter.
	 * 
	 * @param c the queued Check object
	 */
/*
	private void actComputeCheck(Check c) {
		c.state = Check.State.unpaid;
		print("actComputeCheck - $" + c.amount + " owed.");
		c.waiter.msgCheckReady();	
	}
	*/
	/**
	 * Processes a Customer's payment for a Check. 
	 * 
	 * If the Customer offered enough money to pay the Check, the Check is marked paid 
	 * and the Customer is given change. If the Customer did not offer enough money, the
	 * Check is marked unpaid and the Customer's money is returned.
	 * 
	 * @param c the unpaid Check object
	 */
/*
	private void actAcceptPayment(Check c) {
		int change = (c.amountOffered - c.amount);
		if (change >= 0) {
			print("actAcceptPayment - paid - " + c.amount);
			moneyOnHand = (moneyOnHand + c.amount);
			moneyCollected = (moneyCollected + c.amount);
			moneyOwed = (moneyOwed - c.amount);
			c.state = Check.State.paid;
			c.amount = 0;
			c.amountOffered = 0;
			c.customer.msgPaidCashier(change);
		} else {
			print("actAcceptPayment - unpaid");
			c.state = Check.State.unpaid;
			c.customer.msgPaidCashier(c.amountOffered);
			c.amountOffered = 0;
		}
	}
*/
// TODO	
//	/**
//	 * Pays a Bill from a Market.
//	 * 
//	 * @param b the unpaid Bill object
//	 */
//	private void actPayMarket(Bill b) {
//		if (b.amount <= moneyOnHand) {
//			print("actPayMarket - paid");
//			moneyOnHand = (moneyOnHand - b.amount);
//			b.market.msgAcceptPayment(b.amount);
//			b.amount = 0;
//			b.state = Bill.State.paid;
//		} else {
//			print("actPayMarket - unpaid - " + moneyOnHand);
//			b.state = Bill.State.unpaid;
//		}
//	}
	
	// Scheduler
	/*
	public boolean runScheduler() {
		synchronized(checks) {
			for (Check check : checks) {
				if (check.state == Check.State.queue) {
					actComputeCheck(check);
					return true;
				}
				if (check.state == Check.State.paying) {
					actAcceptPayment(check);
					return true;
				}
			}
		}
		
		synchronized(bills) {
			for (Bill bill : bills) {
				if (bill.state == Bill.State.queue) {
					// actPayMarket(bill); // TODO
					return true;
				}
			}
		}
		return false;
	}
	
	// Get

	public RestaurantTimmsCashierAnimation getAnimation() {
		return this.animation;
	}
	
	// Set
	
	public void setAnimation(RestaurantTimmsCashierAnimation animation) {
		this.animation = animation;
	}
	
	// Utilities 
	
	private Check findCheck(RestaurantTimmsCustomer c) {
		Check item = null;
		for (Check check : checks) {
			if (check.customer == c) {
				item = check;
				break;
			}
		}
		return item;
	}

// TODO
//	private Bill findBill(Market m) {
//		Bill item = null;
//		for (Bill bill : bills) {
//			if (bill.market == m) {
//				item = bill;
//				break;
//			}
//		}
//		return item;
//	}
	
	// Check Class
	
	public static class Check {
		private RestaurantTimmsWaiter waiter;
		private RestaurantTimmsCustomer customer;
		private int amount;
		private int amountOffered;
		
		public enum State { queue, unpaid, paying, paid };
		private State state;
		
		public Check(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, int amount) {
			this.waiter = w;
			this.customer = c;
			this.amount = amount;
			this.amountOffered = 0;
			this.state = State.queue;
		}
		
		public int getAmount() {
			return this.amount;
		}
		
		public Check.State getState() {
			return this.state;
		}
		
		public void addAmount(Integer i) {
			this.amount = (this.amount + i);
		}
	}
	
	// Bill Class
	
	public static class Bill {
		// private Market market;
		private int amount;
		
		public enum State { queue, unpaid, paid };
		private State state;
		
// TODO
//		public Bill(Market m, Integer amount) {
//			this.market = m;
//			this.amount = amount;
//			this.state = State.queue;
//		}
		
		public Bill(int amount) {
			this.amount = amount;
			this.state = State.queue;
		}
		
		public int getAmount() {
			return this.amount;
		}
		
		public Bill.State getState() {
			return this.state;
		}
		
		public void addAmount(Integer i) {
			this.amount = (this.amount + i);
		}
	}
}
*/