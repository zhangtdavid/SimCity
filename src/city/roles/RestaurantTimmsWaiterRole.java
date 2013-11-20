package city.roles;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.Application;
import city.Role;
import city.animations.interfaces.RestaurantTimmsAnimatedWaiter;
import city.interfaces.Person;
import city.interfaces.RestaurantTimmsCashier;
import city.interfaces.RestaurantTimmsCook;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsHost;
import city.interfaces.RestaurantTimmsWaiter;

/**
 * Restaurant waiter agent.
 */
public class RestaurantTimmsWaiterRole extends Role implements RestaurantTimmsWaiter {
	// Data
	
	private Boolean wantsBreak;
	private int tiredness;
	private String menuItem;
	public String lastMessage;
	private String lastAction;
	private RestaurantTimmsCook cook;
	private RestaurantTimmsHost host;
	private RestaurantTimmsCashier cashier;
	private int homePosition;
	private RestaurantTimmsAnimatedWaiter animation;
	private Timer timer = new Timer();
	
	public List<InternalCustomer> customers = new ArrayList<InternalCustomer>();
	
	public Semaphore atCustomer = new Semaphore(0, true);
	public Semaphore atTable = new Semaphore(0, true);
	public Semaphore atKitchen = new Semaphore(0, true);
	public Semaphore atHome = new Semaphore(0, true);
	public Semaphore waiterBreak = new Semaphore(0, true);
	public Semaphore waiterHover = new Semaphore(0, true);
	
	// Constructor
	
	public RestaurantTimmsWaiterRole(Person p, RestaurantTimmsCook cook, RestaurantTimmsHost host, RestaurantTimmsCashier cashier, int homePosition) {
		super(p);
		this.wantsBreak = false;
		this.tiredness = 15;
		this.cook = cook;
		this.host = host;
		this.cashier = cashier;
		this.menuItem = null;
		this.homePosition = homePosition;
		this.lastMessage = "Constructed";
		this.lastAction = "Constructed";
	}
	
	// Messages
	
	public void msgWantBreak() {
		print("msgWantBreak");
		this.lastMessage = "msgWantBreak";
		this.wantsBreak = true;
		stateChanged();
	}
	
	public void msgAllowBreak(Boolean r) {
		print("msgAllowBreak - " + r.toString());
		this.wantsBreak = r;
		waiterHover.release();
	}
	
	public void msgSeatCustomer(RestaurantTimmsCustomer c, int n) {
		print("msgSeatCustomer");
		this.lastMessage = "msgSeatCustomer";
		customers.add(new InternalCustomer(c, n, InternalCustomer.State.seat));
		stateChanged();
	}
	
	public void msgWantFood(RestaurantTimmsCustomer c) {
		print("msgWantFood");
		this.lastMessage = "msgWantFood";
		InternalCustomer customer = findCustomer(c);
		customer.setState(InternalCustomer.State.order);
		stateChanged();
	}
	
	public void msgOrderFood(RestaurantTimmsCustomer c, Application.MARKET_ITEMS s) {
		print("msgOrderFood");
		this.lastMessage = "msgOrderFood";
		InternalCustomer customer = findCustomer(c);
		customer.setStockItem(s);
		customer.setState(InternalCustomer.State.hasOrdered);
		waiterHover.release();
	}
	
	public void msgOrderPlaced(RestaurantTimmsCustomer c, Boolean inStock) {
		print("msgOrderPlaced");
		this.lastMessage = "msgOrderPlaced";
		InternalCustomer customer = findCustomer(c);
		if (inStock) {
			customer.setState(InternalCustomer.State.makingFood);
		} else {
			customer.setState(InternalCustomer.State.order);
			customer.setStockItem(null);
		}
		waiterHover.release();
	}
	
	public void msgFoodReady(RestaurantTimmsCustomer c) {
		print("msgFoodReady");
		this.lastMessage = "msgFoodReady";
		InternalCustomer customer = findCustomer(c);
		customer.setState(InternalCustomer.State.foodReady);
		stateChanged();
	}
	
	public void msgCheckReady() {
		print("msgCheckReady");
		this.lastMessage = "msgCheckReady";
		waiterHover.release();
	}
	
	public void msgDoNotWantFood(RestaurantTimmsCustomer c) {
		print("msgDoNotWantFood");
		this.lastMessage = "msgDoNotWantFood";
		InternalCustomer customer = findCustomer(c);
		customer.setState(InternalCustomer.State.none);
		waiterHover.release();
	}
	
	public void guiAtCustomer() {
		print("guiAtCustomer");
		this.lastMessage = "guiAtCustomer";
		atCustomer.release();
	}
	
	public void guiAtTable() {
		print("guiAtTable");
		this.lastMessage = "guiAtTable";
		atTable.release();
	}
	
	public void guiAtKitchen() {
		print("guiAtKitchen");
		this.lastMessage = "guiAtKitchen";
		atKitchen.release();
	}
	
	public void guiAtHome() {
		print("guiAtHome");
		this.lastMessage = "guiAtHome";
		atHome.release();
	}
	
	// Actions
	
	private void actAskForBreak() throws InterruptedException {
		print("actAskForBreak");
		this.lastAction = "actAskForBreak";
		host.msgAskForBreak(this);
		waiterHover.acquire();
		
		if (wantsBreak) {
			animation.goOnBreak();
			timer.schedule(new TimerTask() {
				public void run() {
					wantsBreak = false;
					animation.goOffBreak();
					waiterBreak.release();
				}
			},
			(this.tiredness * 1000));
		} else {
			waiterBreak.release();
		}
	}
	
	private void actSeatCustomer(InternalCustomer c) throws InterruptedException {
		print("actSeatCustomer");
		this.lastAction = "actSeatCustomer";
		animation.goToCustomer(c.getCustomer().getAnimation());
		atCustomer.acquire();
		animation.goToTable(c.getTableNumber(), null);
		c.getCustomer().msgGoToTable(this, c.getTableNumber());
		atTable.acquire();
		c.setState(InternalCustomer.State.none);
	}
	
	private void actGoToHome() throws InterruptedException {
		print("actGoToHome");
		this.lastAction = "actGoToHome";
		animation.goToHome(homePosition);
		atHome.acquire();
	}
	
	private void actTakeOrder(InternalCustomer c) throws InterruptedException {
		print("actTakeOrder");
		this.lastAction = "actTakeOrder";
		RestaurantTimmsCustomer customer = c.getCustomer();
		animation.goToTable(c.getTableNumber(), null);
		atTable.acquire();
		customer.msgOrderFromWaiter();
		waiterHover.acquire();
	}
	
	private void actPlaceOrder(InternalCustomer c) throws InterruptedException {
		print("actPlaceOrder");
		this.lastAction = "actPlaceOrder";
		RestaurantTimmsCustomer customer = c.getCustomer();
		animation.goToKitchen(cook.getAnimation());
		atKitchen.acquire();
		cook.msgCookOrder(this, customer, c.getStockItem());
		waiterHover.acquire();
	}
	
	private void actDeliverOrder(InternalCustomer c) throws InterruptedException {
		print("actDeliverOrder");
		this.lastAction = "actDeliverOrder";
		RestaurantTimmsCustomer customer = c.getCustomer();
		animation.goToKitchen(cook.getAnimation());
		atKitchen.acquire();
		cook.msgPickUpOrder(customer);
		menuItem = c.getStockItem().toString();
		animation.goToTable(c.getTableNumber(), c.getStockItem().toString());
		atTable.acquire();
		menuItem = null;
		customer.msgWaiterDeliveredFood(c.getStockItem());
		c.setState(InternalCustomer.State.none);
		cashier.msgComputeCheck(this, c.getCustomer(), cook.getMenuItemPrice(c.getStockItem()));
		waiterHover.acquire();
	}
	
	// Scheduler

	public boolean runScheduler() {
		InternalCustomer temp = null;
		
		// Handle break requests
		if (wantsBreak) {
			try {
				actAskForBreak();
				waiterBreak.acquire();
			} catch (InterruptedException e) { }
			return true;
		}
		
		// Serve customers
		try {
			for(InternalCustomer customer : customers) {
				switch (customer.getState()) {
					case seat:
						try {
							actSeatCustomer(customer);
						} catch (InterruptedException e) {}
						return true;
					case order:
						try {
							actTakeOrder(customer);
						} catch (InterruptedException e) {}
						return true;
					case hasOrdered:
						try {
							actPlaceOrder(customer);
						} catch (InterruptedException e) {}
						return true;
					case foodReady:
						try {
							actDeliverOrder(customer);
							temp = customer;
						} catch (InterruptedException e) {}
						break;
					default:
						break;
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}
		
		// Delete customer after delivering food
		if (temp != null) {
			customers.remove(temp);
			return true;
		}
			
		// Go home if there's no work to do
		try {
			actGoToHome();
		} catch (InterruptedException e) {}
		return false;
	}
	
	// Get
	
	public RestaurantTimmsHost getHost() {
		return this.host;
	}
	
	public Boolean getWantsBreak() {
		return this.wantsBreak;
	}
	
	public String getMenuItem() {
		if (this.menuItem == null) {
			return "Nothing";
		} else {
			return this.menuItem;
		}
	}
	
	public String getLastMessage() {
		return lastMessage;
	}
	
	public String getLastAction() {
		return lastAction;
	}
	
	public RestaurantTimmsAnimatedWaiter getAnimation() {
		return this.animation;
	}
	
	// Set
	
	public void setAnimation(RestaurantTimmsAnimatedWaiter animation) {
		this.animation = animation;
	}
	
	// Utilities
	
	private InternalCustomer findCustomer(RestaurantTimmsCustomer c) {
		InternalCustomer customer = null;
		for (InternalCustomer internalCustomer : customers) {
			if (internalCustomer.customer == c) {
				customer = internalCustomer;
				break;
			}
		}
		return customer;
	}
	
	// InternalCustomer Class
	// This class stores pointers to all of this waiter's customers and
	// stores their current state. Their state is a private concept just 
	// for the waiter, and the state is changed based on messages received.
	
	public static class InternalCustomer {
		public enum State { none, seat, order, hasOrdered, makingFood, foodReady };
		
		private RestaurantTimmsCustomer customer;
		private State state;
		private int tableNumber;
		private Application.MARKET_ITEMS stockItem;
		
		InternalCustomer(RestaurantTimmsCustomer c, int tableNumber, State state) {
			this.customer = c;
			this.state = state;
			this.tableNumber = tableNumber;
			this.stockItem = null;
		}
		
		// Get
		
		public RestaurantTimmsCustomer getCustomer() {
			return customer;
		}
	
		public State getState() {
			return state;
		}

		public int getTableNumber() {
			return tableNumber;
		}
		
		public Application.MARKET_ITEMS getStockItem() {
			return stockItem;
		}
		
		// Set
		
		public void setState(State s) {
			this.state = s;
		}
		
		public void setStockItem(Application.MARKET_ITEMS s) {
			this.stockItem = s;
		}
	}
}
