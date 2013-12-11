package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import utilities.MarketOrder;
import utilities.MarketTransaction;
import utilities.MarketTransaction.MarketTransactionState;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import city.Application.BANK_SERVICE;
import city.Application.TRANSACTION_TYPE;
import city.bases.Building;
import city.bases.JobRole;
import city.bases.Role;
import city.buildings.RestaurantZhangBuilding;
import city.buildings.interfaces.Market;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.RestaurantZhangCashier;
import city.roles.interfaces.RestaurantZhangCustomer;
import city.roles.interfaces.RestaurantZhangHost;
import city.roles.interfaces.RestaurantZhangWaiter;

public class RestaurantZhangCashierRole extends JobRole implements RestaurantZhangCashier {

	// Data

	private List<RestaurantZhangCheck> pendingChecks = Collections.synchronizedList(new ArrayList<RestaurantZhangCheck>());
	private RestaurantZhangHost host;
	private boolean restaurantClosing = false;
	private Map<RestaurantZhangCustomer, Integer> tabCustomers = new HashMap<RestaurantZhangCustomer, Integer>();
	private int balance = 10000;
	private Map<String, Integer> menu;

	private RestaurantZhangBuilding restaurant;
	private List<Role> roles = Collections.synchronizedList(new ArrayList<Role>());
	private List<MarketTransaction> marketTransactions = Collections.synchronizedList(new ArrayList<MarketTransaction>());

	// Constructor

	public RestaurantZhangCashierRole(Building restaurantToWorkAt, int shiftStart_, int shiftEnd_) {
		super();
		restaurant = (RestaurantZhangBuilding) restaurantToWorkAt;
		this.setShift(shiftStart_, shiftEnd_);
		this.setWorkplace(restaurantToWorkAt);
		this.setSalary(RESTAURANTZHANGCASHIERSALARY);
		// Market stuff
		roles.add(new MarketCustomerDeliveryPaymentRole(restaurant, marketTransactions, this));
		roles.get(0).setActive();
//		roles.add((Role) restaurant.getBankCustomer());
//		roles.get(1).setActive();
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

	// Sent by Cook
	@Override
	public void msgAddMarketOrder(Market m, MarketOrder o) {
		print("Cashier received msgAddMarketOrder");
		marketTransactions.add(new MarketTransaction(m, o));
		((MarketCustomerDeliveryPaymentRole) roles.get(0)).setMarket(m);
		stateChanged();
	}

	// Scheduler

	@Override
	public boolean runScheduler() {
		
		synchronized(marketTransactions) {
            for (MarketTransaction transaction : marketTransactions) {
                    if (transaction.getMarketTransactionState() == MarketTransactionState.Done) {
					marketTransactions.remove(transaction);
					return true;
				}
			}
		}

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
		
		if(restaurant.getCash() > 2000) {
			restaurant.getBankCustomer().setActive(BANK_SERVICE.atmDeposit, restaurant.getCash() - 2000, TRANSACTION_TYPE.business);
			roles.add((Role) restaurant.getBankCustomer());
			restaurant.setCash(2000);
		}
		
		boolean blocking = false;
		
		for (Role r : roles)  {
			if (r.getActive() && r.getActivity()) {
				if(r.getPerson() == null && r.getClass() != BankCustomer.class)
					r.setPerson(this.getPerson()); // Sanity check
				blocking  = true;
				boolean activity = r.runScheduler();
				if (!activity)
					r.setActivityFinished();
				break;
			}
		}
		
		return blocking;
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
		this.getPerson().printViaRole("RestaurantZhangCashier", msg);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTZHANG, "RestaurantZhangCashierRole " + this.getPerson().getName(), msg);
	}

	// Classes
}

