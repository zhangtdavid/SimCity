package city.roles;

import java.util.*;

import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import city.Role;
import city.interfaces.RestaurantZhangCashier;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangWaiter;

/**
 * Restaurant Cashier Agent
 */

public class RestaurantZhangCashierRole extends Role implements RestaurantZhangCashier {
	private static final int CASHIERX = 0;
	private static final int CASHIERY = 200;
	public double balance = 10000;
	public Map<RestaurantZhangCustomer, Double> tabCustomers = new HashMap<RestaurantZhangCustomer, Double>();
	//public Map<RestaurantZhangMarket, Integer> marketBills = Collections.synchronizedMap(new HashMap<Market, Integer>());
	
	private String name;
	
	public Map<String, Double> menu;
	
	public List<RestaurantZhangCheck> pendingChecks = Collections.synchronizedList(new ArrayList<RestaurantZhangCheck>());

	public RestaurantZhangCashierRole(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void msgComputeBill(RestaurantZhangWaiter waiter, RestaurantZhangCustomer customer, String choice) {
		pendingChecks.add(new RestaurantZhangCheck(waiter, customer, menu.get(choice)));
		stateChanged();
	}
	
	public void msgHereIsPayment(RestaurantZhangCheck c, double cash) {
		pendingChecks.add(c);
		c.status = RestaurantZhangCheck.CheckStatus.atCustomer;
		c.payment = cash;
		stateChanged();
	}
	
//	public void msgHereIsMarketBill(Market m, int bill) {
//		marketBills.put(m, bill);
//		stateChanged();
//	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean runScheduler() {
		synchronized(pendingChecks) {
			for(RestaurantZhangCheck c : pendingChecks) {
				if(c.status == RestaurantZhangCheck.CheckStatus.created) {
					giveCheckToWaiter(c);
					return true;
				}
			}
		}
		synchronized(pendingChecks) {
			for(RestaurantZhangCheck c : pendingChecks) {
				if(c.status == RestaurantZhangCheck.CheckStatus.atCustomer) {
					processPayment(c);
					return true;
				}
			}
		}
//		synchronized(marketBills) {
//			if(!marketBills.isEmpty() ) {
//				Iterator<Map.Entry<Market, Integer>> it = marketBills.entrySet().iterator();
//				Map.Entry<Market, Integer> currentMarketBill = it.next();
//				Market mmmm = currentMarketBill.getKey();
//				payMarket(currentMarketBill.getKey(), currentMarketBill.getValue());
//				return true;
//			}
//		}
		return false;
	}

	// Actions
	void giveCheckToWaiter(RestaurantZhangCheck c) {
		pendingChecks.remove(c);
		c.status = RestaurantZhangCheck.CheckStatus.atWaiter;
		print("Giving check to waiter " + c.waiter.getName());
		c.waiter.msgHereIsWaiterCheck(c);
	}
	
	void processPayment(RestaurantZhangCheck c) {
		pendingChecks.remove(c);
		if(tabCustomers.containsKey(c.cust)) { //
			print("Tab needs to be paid, added to check: " + tabCustomers.get(c.cust));
			c.price += tabCustomers.get(c.cust);
			c.price =  Math.floor(c.price * 100) / 100;
			tabCustomers.remove(c.cust);
		}
		double change = c.payment - c.price;//Math.round((c.payment - c.price) * 100) / 100; // Calculate the change
		if(change < 0) {
			double tab = Math.abs(change);
			balance += c.payment;
			print("Adding customer " + c.cust.getName() + " to tabs list, tab of " + tab);
			tabCustomers.put(c.cust, tab);
			c.cust.msgPayLater(tab);
			return;
		} else {
			balance += c.price;
			print("Giving change " + change + " to customer " + c.cust.getName());
			c.cust.msgHereIsChange(change);
		}
	}
	
//	void payMarket(Market m, int bill) {
//		Do("Paying Market " + m.getName() + " bill of " + bill);
//		marketBills.remove(m);
//		m.msgPayBill(bill);
//		balance -= (double)bill;
//		Do("Cashier balance after paying Market " + m.getName() + ": " + balance);
//	}
	
	//utilities
	
	public int getX() {
		return CASHIERX;
	}
	
	public int getY() {
		return CASHIERY;
	}

	public void setMenu(RestaurantZhangMenu m) {
		menu = new HashMap<String, Double>(m.getMenu());
	}
}
