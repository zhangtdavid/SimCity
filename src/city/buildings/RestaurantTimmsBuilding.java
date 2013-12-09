package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import utilities.MarketOrder;
import city.Application;
import city.Application.BUILDING;
import city.Application.FOOD_ITEMS;
import city.animations.RestaurantTimmsCashierAnimation;
import city.animations.RestaurantTimmsCookAnimation;
import city.animations.RestaurantTimmsCustomerAnimation;
import city.animations.RestaurantTimmsHostAnimation;
import city.animations.RestaurantTimmsWaiterAnimation;
import city.bases.RestaurantBuilding;
import city.bases.interfaces.RoleInterface;
import city.buildings.RestaurantTimmsBuilding.MenuItem.State;
import city.buildings.interfaces.Bank;
import city.buildings.interfaces.RestaurantTimms;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.RestaurantTimmsPanel;
import city.roles.BankCustomerRole;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.RestaurantTimmsCashier;
import city.roles.interfaces.RestaurantTimmsCook;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.roles.interfaces.RestaurantTimmsHost;
import city.roles.interfaces.RestaurantTimmsWaiter;

public class RestaurantTimmsBuilding extends RestaurantBuilding implements RestaurantTimms {
	
	// Data
	
	private RestaurantTimmsCashier cashier;
	private RestaurantTimmsCook cook;
	private RestaurantTimmsHost host;
	
	private List<InternalCustomer> restaurantCustomers = Collections.synchronizedList(new ArrayList<InternalCustomer>());
	private List<RestaurantTimmsWaiter> restaurantWaiters = Collections.synchronizedList(new ArrayList<RestaurantTimmsWaiter>());
	private List<RestaurantTimmsBuilding.Table> restaurantTables = Collections.synchronizedList(new ArrayList<RestaurantTimmsBuilding.Table>());
	private List<MenuItem> restaurantMenu = Collections.synchronizedList(new ArrayList<MenuItem>());
	private List<InternalMarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<InternalMarketOrder>());
	private List<Check> restaurantChecks = Collections.synchronizedList(new ArrayList<Check>());
	private List<Order> restaurantOrders = Collections.synchronizedList(new ArrayList<Order>());
	
	private static final int START_CASH_MIN = 1000;
	private static final int START_CASH_MAX = 5000;
	private static final int KITCHEN_STORE_MIN = 2;
	private static final int KITCHEN_STORE_MAX = 2;
	private static final int MENU_PRICE_MIN = 5;
	private static final int MENU_PRICE_MAX = 12;
	
	private int waiterRoundRobinIndex = 0;
	private BankCustomer bankCustomer = null;
	
	// Constructor
	
	public RestaurantTimmsBuilding(String name, RestaurantTimmsPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		this.setCash((START_CASH_MIN + (int)(Math.random() * ((START_CASH_MAX - START_CASH_MIN) + 1))));
		this.setCustomerRoleName("city.roles.RestaurantTimmsCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantTimmsCustomerAnimation");
		
		// Create tables
		int i = 0;
		while (i < 9) {
			restaurantTables.add(new Table(i));
			i++;
		}
		
		// Create menu - food materializes at creation
		for (Application.FOOD_ITEMS f : Application.FOOD_ITEMS.values()) {
			int randomAmount = KITCHEN_STORE_MIN + (int)(Math.random() * ((KITCHEN_STORE_MAX - KITCHEN_STORE_MIN) + 1));
			int randomPrice = MENU_PRICE_MIN + (int)(Math.random() * ((MENU_PRICE_MAX - MENU_PRICE_MIN) + 1));
			MenuItem item = new MenuItem(f, randomAmount, randomPrice);
			item.setState(State.inStock);
			restaurantMenu.add(item);
		}
		
		// Create bank customer
		this.bankCustomer = new BankCustomerRole((Bank)(Application.CityMap.findRandomBuilding(BUILDING.bank)));
		
		this.addWorkerRoleName("city.roles.RestaurantTimmsCashierRole");
		this.addWorkerRoleName("city.roles.RestaurantTimmsCookRole");
		this.addWorkerRoleName("city.roles.RestaurantTimmsHostRole");
		this.addWorkerRoleName("city.roles.RestaurantTimmsWaiterRole");
		this.setBuildingClassName("city.buildings.interfaces.RestaurantTimms");
	}
	
	//=========//
	// Getters //
	//=========//
	
	@Override
	public boolean getBusinessIsOpen() {
		boolean disposition = true;
		if (cashier == null || cook == null || host == null) { disposition = false; }
		if (restaurantWaiters.size() == 0) { disposition = false; }
		return disposition;
	}
	
	@Override
	public RestaurantTimmsCashier getCashier() {
		return cashier;
	}
	
	@Override
	public RestaurantTimmsCook getCook() {
		return cook;
	}
	
	@Override
	public BankCustomer getBankCustomer() {
		return bankCustomer;
	}
	
	/**
	 * Returns the list of waiting customers from the list of customers.
	 */
	@Override
	public List<RestaurantTimmsCustomer> getWaitingCustomers() {
		List<RestaurantTimmsCustomer> list = new ArrayList<RestaurantTimmsCustomer>();
		for (InternalCustomer c : restaurantCustomers) {
			if (!c.isSeated()) {
				list.add(c.getCustomer());
			}
		}
		return list;
	}
	
	/**
	 * Returns a waiting customer from the list of customers. Sets the customer's host
	 * and waiter to the specified values and marks the customer as no longer waiting.
	 */
	@Override
	public RestaurantTimmsCustomer getCustomer(RestaurantTimmsHost h, RestaurantTimmsWaiter w) {
		InternalCustomer i = null;
		for (InternalCustomer temp : restaurantCustomers) {
			if (!temp.isSeated()) {
				i = temp;
				break;
			}
		}
		i.setHost(h);
		i.setWaiter(w);
		i.setSeated();
		return i.getCustomer();
	}
	
	@Override
	public RestaurantTimmsHost getHost() {
		return host;
	}
	
	/**
	 * Returns the list of waiters.
	 */
	@Override
	public List<RestaurantTimmsWaiter> getWaiters() {
		return restaurantWaiters;
	}
	
	/**
	 * Returns a waiter from the list of waiters. Waiter to return is chosen round-robin style.
	 */
	@Override
	public RestaurantTimmsWaiter getWaiter() {
		RestaurantTimmsWaiter w = restaurantWaiters.get(waiterRoundRobinIndex);
		waiterRoundRobinIndex = (waiterRoundRobinIndex + 1) % restaurantWaiters.size();
		return w;
	}
	
	/**
	 * Returns the index of a waiter from the list of waiters. This tells the waiter where his home position is.
	 */
	@Override
	public int getWaiterIndex(RestaurantTimmsWaiter w) {
		return restaurantWaiters.indexOf(w);
	}
	
	@Override
	public List<Table> getTables() {
		return restaurantTables;
	}
	
	@Override
	public List<MenuItem> getMenuItems() {
		return restaurantMenu;
	}
	
	@Override
	public List<Check> getChecks() {
		return restaurantChecks;
	}
	
	@Override
	public List<Order> getOrders() {
		return restaurantOrders;
	}
	
	//=========//
	// Setters //
	//=========//
	
	@Override
	public void setCashier(RestaurantTimmsCashier c) {
		this.cashier = c;
	}
	
	@Override
	public void setCook(RestaurantTimmsCook c) {
		this.cook = c;
	}
	
	@Override
	public void setHost(RestaurantTimmsHost h) {
		this.host = h;
	}
	
	@Override 
	public void setBankCustomer(BankCustomer b) {
		this.bankCustomer = b;
	}
	
	//===========//
	// Utilities //
	//===========//
	
	@Override
	public void addOccupyingRole(RoleInterface r) {
		if (r instanceof RestaurantTimmsCashier) {
			RestaurantTimmsCashier cashier = (RestaurantTimmsCashier) r; 
			if (!super.occupyingRoleExists(r)) {
				RestaurantTimmsCashierAnimation a = new RestaurantTimmsCashierAnimation();
				cashier.setAnimation(a);
				this.getPanel().addVisualizationElement(a);
				super.addOccupyingRole(r, a);
			}
		} else if (r instanceof RestaurantTimmsCook) {
			RestaurantTimmsCook cook = (RestaurantTimmsCook) r;
			if (!super.occupyingRoleExists(r)) {
				RestaurantTimmsCookAnimation a = new RestaurantTimmsCookAnimation();
				cook.setAnimation(a);
				this.getPanel().addVisualizationElement(a);
				super.addOccupyingRole(r, a);
			}
		} else if (r instanceof RestaurantTimmsCustomer) {
			RestaurantTimmsCustomer customer = (RestaurantTimmsCustomer) r;
			if (!super.occupyingRoleExists(r)) {
				customer.setRestaurantTimmsBuilding(this);
				RestaurantTimmsCustomerAnimation a = new RestaurantTimmsCustomerAnimation(customer);
				customer.setAnimation(a);
				this.getPanel().addVisualizationElement(a);
				super.addOccupyingRole(r, a);
			}
		} else if (r instanceof RestaurantTimmsHost) {
			RestaurantTimmsHost host = (RestaurantTimmsHost) r;
			if (!super.occupyingRoleExists(r)) {
				RestaurantTimmsHostAnimation a = new RestaurantTimmsHostAnimation();
				host.setAnimation(a);
				this.getPanel().addVisualizationElement(a);
				super.addOccupyingRole(r, a);
			}
		} else if (r instanceof RestaurantTimmsWaiter) {
			RestaurantTimmsWaiter waiter = (RestaurantTimmsWaiter) r;
			if (!super.occupyingRoleExists(r)) {
				RestaurantTimmsWaiterAnimation a = new RestaurantTimmsWaiterAnimation(waiter);
				waiter.setAnimation(a);
				this.getPanel().addVisualizationElement(a);
				super.addOccupyingRole(r, a);
			}
		}
	}
	
	@Override
	public void addCustomer(RestaurantTimmsCustomer c, RestaurantTimmsHost h) {
		InternalCustomer i = new InternalCustomer();
		i.setCustomer(c);
		i.setHost(h);
		restaurantCustomers.add(i);
	}
	
	@Override
	public void updateCustomer(RestaurantTimmsCustomer c, RestaurantTimmsHost h, RestaurantTimmsWaiter w) {
		InternalCustomer i = null;
		for (InternalCustomer temp : restaurantCustomers) {
			if (temp.getCustomer() == c) {
				i = temp;
				break;
			}
		}
		i.setHost(h);
		i.setWaiter(w);
	}
	
	@Override
	public void removeCustomer(RestaurantTimmsCustomer c) {
		InternalCustomer i = null;
		for (InternalCustomer temp : restaurantCustomers) {
			if (temp.getCustomer() == c) {
				i = temp;
				break;
			}
		}
		restaurantCustomers.remove(i);
	}
	
	@Override
	public void addWaiter(RestaurantTimmsWaiter w) {
		restaurantWaiters.add(w);
	}
	
	@Override
	public void addMarketOrder(InternalMarketOrder o) {
		marketOrders.add(o);
	}
	
	@Override
	public void addCheck(Check c) {
		restaurantChecks.add(c);
	}
	
	@Override
	public void addOrder(Order o) {
		restaurantOrders.add(o);
	}
	
	@Override
	public void removeOrder(Order o) {
		restaurantOrders.remove(o);
	}
	
	/**
	 * Overrides RestaurantBaseBuilding.
	 * 
	 * RestaurantTimms does not use the "foods" like the other restaurants do. This method
	 * intercepts calls from Shirley's market to accept deliveries into our own restaurantMenu
	 * 
	 * Because we set the food as being "in stock" here, there are no market-related messages in
	 * the cook role.
	 */
	@Override
	public void incrementFoodQuantity(Map<FOOD_ITEMS, Integer> receivedItems) {
        for (FOOD_ITEMS f: receivedItems.keySet()) {
        	for (MenuItem i : restaurantMenu) {
        		if (i.getItem() == f) {
        			i.incrementQuantity(receivedItems.get(f));
        			i.setState(MenuItem.State.inStock);
        		}
        	}
        }
	}
	
	//=========//
	// Classes //
	//=========//
	
	public static class InternalCustomer {
		private RestaurantTimmsCustomer customer;
		private RestaurantTimmsHost host;
		private RestaurantTimmsWaiter waiter;
		
		private enum State {waiting, seated};
		private State state;
		
		public InternalCustomer() {
			setCustomer(null);
			setHost(null);
			setWaiter(null);
			state = State.waiting;
		}
		
		public boolean isSeated() {
			return (state == State.seated);
		}
		
		public void setSeated() {
			state = State.seated;
		}

		public RestaurantTimmsCustomer getCustomer() {
			return customer;
		}

		public void setCustomer(RestaurantTimmsCustomer customer) {
			this.customer = customer;
		}

		public RestaurantTimmsHost getHost() {
			return host;
		}

		public void setHost(RestaurantTimmsHost host) {
			this.host = host;
		}

		public RestaurantTimmsWaiter getWaiter() {
			return waiter;
		}

		public void setWaiter(RestaurantTimmsWaiter waiter) {
			this.waiter = waiter;
		}
		
	} 
    
    public static class InternalMarketOrder {
    	// None of the state code is used right now
    	
        private enum State {pending, ordered};
        private MarketOrder order;
        private State state;
        
		public InternalMarketOrder(MarketOrder o) {
			order = new MarketOrder(o);
			state = State.pending;
		}
		
		public MarketOrder getOrder() {
			return order;
		}
		
		public boolean isPending() {
			return (state == State.pending);
		}
    }
    
    public static class MenuItem {
    	private Application.FOOD_ITEMS item;
    	private int quantity;
    	private int price;
    	public static enum State { none, inStock, onOrder, offMenu };
    	private State state;
    	
		public MenuItem(Application.FOOD_ITEMS item, int quantity, int price) {
			this.item = item;
			this.quantity = quantity;
			this.price = price;
			this.state = State.none;
		}
		
		public Application.FOOD_ITEMS getItem() {
			return item;
		}
		
		public Integer getQuantity() {
			return quantity;
		}
		
		public int getPrice() {
			return price;
		}
		
		public State getState() {
			return state;
		}
		
		public void setState(State s) {
			this.state = s;
		}

		public boolean isInStock() {
			return (state == State.inStock);
		}
		
		public void incrementQuantity(int q) {
			this.quantity = (this.quantity + q);
		}
		
		public void decrementQuantity(int q) {
			this.quantity = (this.quantity - q);
		}
    }

	public static class Table {
		private boolean occupied;
		private int number;

		public Table(int i) {
			this.occupied = false;
			this.number = i;
		}
		
		public int getNumber() {
			return number;
		}
		
		public boolean getOccupied() {
			return occupied;
		}

		public void setOccupied() {
			this.occupied = true;
		}

		public void setUnoccupied() {
			this.occupied = false;
		}
	}
	
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
		
		public int getAmountOffered() {
			return this.amountOffered;
		}
		
		public RestaurantTimmsCustomer getCustomer() {
			return this.customer;
		}
		
		public State getState() {
			return this.state;
		}
		
		public RestaurantTimmsWaiter getWaiter() {
			return this.waiter;
		}
		
		public void setState(State s) {
			this.state = s;
		}
		
		public void setAmount(int i) {
			this.amount = i;
		}
		
		public void setAmountOffered(int i) {
			this.amountOffered = i;
		}
		
		public void setWaiter(RestaurantTimmsWaiter w) {
			this.waiter = w;
		}
	}
	
	public static class Order {
		private RestaurantTimmsWaiter waiter;
		private RestaurantTimmsCustomer customer;
		private Application.FOOD_ITEMS item;
		
		public enum State { pending, queue, cooking, ready };
		private State state;

		public Order(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, Application.FOOD_ITEMS s) {
			this.waiter = w;
			this.customer = c;
			this.item = s;
			this.state = State.pending;
		}
		
		public RestaurantTimmsWaiter getWaiter() {
			return this.waiter;
		}
		
		public RestaurantTimmsCustomer getCustomer() {
			return this.customer;
		}
		
		public Application.FOOD_ITEMS getItem() {
			return this.item;
		}
		
		public State getState() {
			return this.state;
		}
		
		public void setState(State s) {
			this.state = s;
		}
	}

}
