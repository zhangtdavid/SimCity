package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import trace.AlertLog;
import trace.AlertTag;
import utilities.MarketTransaction;
import city.Role;
import city.animations.interfaces.RestaurantTimmsAnimatedCashier;
import city.buildings.RestaurantTimmsBuilding;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.RestaurantTimmsCashier;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsWaiter;

/**
 * Restaurant cashier agent.
 */
public class RestaurantTimmsCashierRole extends Role implements RestaurantTimmsCashier {
	
	// Data
	
	private int moneyCollected;
	private int moneyOwed;
	private RestaurantTimmsBuilding rtb; 
	
	private List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	private List<Role> roles = new ArrayList<Role>(); // For market orders
	
	private MarketCustomerDeliveryPayment marketPaymentRole;
	private List<MarketTransaction> marketTransactions;
	
	// Constructor
	
	/**
	 * Construct a RestaurantTimmsCashierRole.
	 * 
	 * @param b the RestaurantTimmsBuilding that this cashier will work at
	 * @param shiftStart the hour (0-23) that the role's shift begins
	 * @param shiftEnd the hour (0-23) that the role's shift ends
	 */
	public RestaurantTimmsCashierRole(RestaurantTimmsBuilding b, int shiftStart, int shiftEnd){
		super();
		this.setWorkplace(b);
		this.setSalary(RestaurantTimmsBuilding.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		this.moneyCollected = 0;
		this.moneyOwed = 0;
		this.rtb = b;
		
		// The sub-role for paying the market is always part of the cashier. It's always active.
		// Not giving the sub-role knowledge of the role's person. Hopefully this won't cause problems.
		marketPaymentRole = new MarketCustomerDeliveryPaymentRole(rtb, marketTransactions);
		marketPaymentRole.setActive();
		roles.add((Role) marketPaymentRole);
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
	@Override
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
	
	/**
	 * Notifies the Cashier that a Customer is ready to pay. The Customer includes how much money
	 * they have, in order to determine their change.
	 * 
	 * @param c a Customer interface
	 * @param money the amount of money the Customer has
	 */
	@Override
	public void msgMakePayment(RestaurantTimmsCustomer c, int money) {
		Check check = findCheck(c);
		check.amountOffered = money;
		print("msgMakePayment - offered $" + money + " for bill of $" + check.amount);
		check.state = Check.State.paying;
		stateChanged();
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		//-------------------/
		// Primary Scheduler /
		//-------------------/
		
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

		//------------------------------------/
		// Role Scheduler (for market orders) /
		//------------------------------------/
		
		boolean blocking = false;
		for (Role r : roles) if (r.getActive() && r.getActivity()) {
			blocking  = true;
			boolean activity = r.runScheduler();
			if (!activity) {
				r.setActivityFinished();
			}
			break;
		}
		
		// Scheduler disposition
		return blocking;
	}
	
	// Actions
	
	/**
	 * Computes the Check for a Waiter.
	 * 
	 * @param c the queued Check object
	 */
	private void actComputeCheck(Check c) {
		c.state = Check.State.unpaid;
		print("actComputeCheck - $" + c.amount + " owed.");
		c.waiter.msgCheckReady();	
	}
	
	/**
	 * Processes a Customer's payment for a Check. 
	 * 
	 * If the Customer offered enough money to pay the Check, the Check is marked paid 
	 * and the Customer is given change. If the Customer did not offer enough money, the
	 * Check is marked unpaid and the Customer's money is returned.
	 * 
	 * @param c the unpaid Check object
	 */
	private void actAcceptPayment(Check c) {
		int change = (c.amountOffered - c.amount);
		if (change >= 0) {
			print("actAcceptPayment - paid - " + c.amount);
			rtb.setCash(rtb.getCash() + c.amount);
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
	
	// Getters
	
	@Override
	public MarketCustomerDeliveryPayment getMarketPaymentRole() {
		return marketPaymentRole;
	}
	
	@Override
	public int getMoneyCollected() {
		return moneyCollected;
	}

	@Override
	public int getMoneyOwed() {
		return moneyOwed;
	}
	
	@Override
	public List<Check> getChecks() {
		return checks;
	}
	
	// Setters
	
	@Override
	public void setActive() {
		rtb.setCashier(this);
		this.getAnimation(RestaurantTimmsAnimatedCashier.class).setVisible(true);
		super.setActive();
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
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTTIMMS, "RestaurantTimmsCashierRole " + this.getPerson().getName(), msg);
    }
	
}
