package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import utilities.MarketOrder;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import city.Building;
import city.Role;
import city.interfaces.Market;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.RestaurantZhangCashier;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangHost;
import city.interfaces.RestaurantZhangWaiter;

public class RestaurantZhangCashierRole extends Role implements RestaurantZhangCashier {
	
	// Data
	
	// private Map<RestaurantZhangMarket, Integer> marketBills = Collections.synchronizedMap(new HashMap<Market, Integer>());
	// private List<MarketTransaction> marketTransactions = Collections.synchronizedList(new ArrayList<MarketTransaction>());
	private List<RestaurantZhangCheck> pendingChecks = Collections.synchronizedList(new ArrayList<RestaurantZhangCheck>());
	private List<Role> roles = new ArrayList<Role>();
	private RestaurantZhangHost host;
	private boolean restaurantClosing = false;
	private Map<RestaurantZhangCustomer, Integer> tabCustomers = new HashMap<RestaurantZhangCustomer, Integer>();
	private int balance = 10000;
	private Map<String, Integer> menu;
	
	// Constructor

	public RestaurantZhangCashierRole(Building restaurantToWorkAt, int shiftStart_, int shiftEnd_) {
		super();
		this.setShift(shiftStart_, shiftEnd_);
		this.setWorkplace(restaurantToWorkAt);
		this.setSalary(RESTAURANTZHANGCASHIERSALARY);
		// roles.add(new MarketCustomerDeliveryPaymentRole(restaurant, marketTransactions));
	}
	
	// Messages

	@Override
	public void msgComputeBill(RestaurantZhangWaiter waiter, RestaurantZhangCustomer customer, String choice) {
		pendingChecks.add(new RestaurantZhangCheck(waiter, customer, menu.get(choice)));
		stateChanged();
	}

	@Override
	public void msgHereIsPayment(RestaurantZhangCheck c, int cash) {
		pendingChecks.add(c);
		c.status = RestaurantZhangCheck.CheckStatus.atCustomer;
		c.payment = cash;
		stateChanged();
	}
	
	// Scheduler

	@Override
	public boolean runScheduler() {
		if(restaurantClosing) {
			if(((RestaurantZhangHostRole)host).getNumberOfCustomersInRestaurant() <= 0) {
				super.setInactive();
				restaurantClosing = false;
				return true;
			}
		}
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
	
	private void giveCheckToWaiter(RestaurantZhangCheck c) {
		pendingChecks.remove(c);
		c.status = RestaurantZhangCheck.CheckStatus.atWaiter;
		print("Giving check to waiter " + c.waiter.getPerson().getName());
		c.waiter.msgHereIsWaiterCheck(c);
	}

	private void processPayment(RestaurantZhangCheck c) {
		pendingChecks.remove(c);
		if(tabCustomers.containsKey(c.cust)) { //
			print("Tab needs to be paid, added to check: " + tabCustomers.get(c.cust));
			c.price += tabCustomers.get(c.cust);
			tabCustomers.remove(c.cust);
		}
		int change = c.payment - c.price;//Math.round((c.payment - c.price) * 100) / 100; // Calculate the change
		if(change < 0) {
			int tab = Math.abs(change);
			balance += c.payment;
			print("Adding customer " + c.cust.getPerson().getName() + " to tabs list, tab of " + tab);
			tabCustomers.put(c.cust, tab);
			c.cust.msgPayLater(tab);
			return;
		} else {
			balance += c.price;
			print("Giving change " + change + " to customer " + c.cust.getPerson().getName());
			c.cust.msgHereIsChange(change);
		}
	}

	//	private void payMarket(Market m, int bill) {
	//		Do("Paying Market " + m.getName() + " bill of " + bill);
	//		marketBills.remove(m);
	//		m.msgPayBill(bill);
	//		balance -= (int)bill;
	//		Do("Cashier balance after paying Market " + m.getName() + ": " + balance);
	//	}
	
	// Getters
	
	@Override
	public List<RestaurantZhangCheck> getPendingChecks() {
		return pendingChecks;
	}
	
	@Override
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
		return (MarketCustomerDeliveryPayment) roles.get(0); // TODO cleanup
	}
	
	@Override
	public RestaurantZhangHost getHost() {
		return host;
	}
	
	@Override
	public Map<RestaurantZhangCustomer, Integer> getTabCustomers() {
		return tabCustomers;
	}
	
	@Override
	public int getBalance() {
		return balance;
	}
	
	@Override
	public Map<String, Integer> getMenu() {
		return menu;
	}
	
	// Setters

	@Override
	public void setMenu(RestaurantZhangMenu m) {
		menu = new HashMap<String, Integer>(m.getMenu());
	}
	
	@Override
	public void setHost(RestaurantZhangHost h) {
		host = h;
	}
	
	@Override
	public void setInactive() {
		if(host != null) {
			if(((RestaurantZhangHostRole)host).getNumberOfCustomersInRestaurant() !=0) {
				restaurantClosing = true;
				return;
			}
		}
		super.setInactive();
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTZHANG, "RestaurantZhangCashierRole " + this.getPerson().getName(), msg);
    }

	// Classes

	public static class MarketTransaction {
		public enum MarketTransactionState {Pending, Processing, WaitingForConfirmation};
		public Market market;
		public MarketOrder order;
		public int bill;
		public MarketTransactionState s;

		public MarketTransaction (Market m, MarketOrder o) {
			market = m;
			order = new MarketOrder(o);
			bill = 0;
			s = MarketTransactionState.Pending;
		}
	}
}

