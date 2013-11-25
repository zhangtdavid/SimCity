package city.roles;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.Application;
import city.Role;
import city.animations.interfaces.RestaurantTimmsAnimatedCook;
import city.animations.interfaces.RestaurantTimmsAnimatedCustomer;
import city.animations.interfaces.RestaurantTimmsAnimatedWaiter;
import city.buildings.RestaurantTimmsBuilding;
import city.interfaces.RestaurantTimmsCustomer;
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
	private int homePosition;
	private Timer timer = new Timer();
	private RestaurantTimmsBuilding rtb;
	private RestaurantTimmsAnimatedWaiter animation = null;
	
	public List<InternalCustomer> customers = new ArrayList<InternalCustomer>();
	
	public Semaphore atCustomer = new Semaphore(0, true);
	public Semaphore atTable = new Semaphore(0, true);
	public Semaphore atKitchen = new Semaphore(0, true);
	public Semaphore atHome = new Semaphore(0, true);
	public Semaphore waiterBreak = new Semaphore(0, true);
	public Semaphore waiterHover = new Semaphore(0, true);
	
	// Constructor
	
	/**
	 * Construct a RestaurantTimmsWaiterRole.
	 * 
	 * @param b the RestaurantTimmsBuilding that this waiter will work at
	 * @param shiftStart the hour (0-23) that the role's shift begins
	 * @param shiftEnd the hour (0-23) that the role's shift ends
	 * @param homePosition the index of the waiter, giving him a position to stand in when not working
	 */
	public RestaurantTimmsWaiterRole(RestaurantTimmsBuilding b, int shiftStart, int shiftEnd, int homePosition) {
		super();
		this.setWorkplace(b);
		this.setSalary(RestaurantTimmsBuilding.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		this.wantsBreak = false;
		this.tiredness = 15;
		this.menuItem = null;
		this.homePosition = homePosition;
		this.lastMessage = "Constructed";
		this.lastAction = "Constructed";
		this.rtb = this.getWorkplace(RestaurantTimmsBuilding.class);
	}
	
	// Messages
	
	@Override
	public void msgWantBreak() {
		print("msgWantBreak");
		this.lastMessage = "msgWantBreak";
		this.wantsBreak = true;
		stateChanged();
	}
	
	@Override
	public void msgAllowBreak(Boolean r) {
		print("msgAllowBreak - " + r.toString());
		this.wantsBreak = r;
		waiterHover.release();
	}
	
	@Override
	public void msgSeatCustomer(RestaurantTimmsCustomer c, int n) {
		print("msgSeatCustomer");
		this.lastMessage = "msgSeatCustomer";
		customers.add(new InternalCustomer(c, n, InternalCustomer.State.seat));
		stateChanged();
	}
	
	@Override
	public void msgWantFood(RestaurantTimmsCustomer c) {
		print("msgWantFood");
		this.lastMessage = "msgWantFood";
		InternalCustomer customer = findCustomer(c);
		customer.setState(InternalCustomer.State.order);
		stateChanged();
	}
	
	@Override
	public void msgOrderFood(RestaurantTimmsCustomer c, Application.FOOD_ITEMS s) {
		print("msgOrderFood");
		this.lastMessage = "msgOrderFood";
		InternalCustomer customer = findCustomer(c);
		customer.setStockItem(s);
		customer.setState(InternalCustomer.State.hasOrdered);
		waiterHover.release();
	}
	
	@Override
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
	
	@Override
	public void msgFoodReady(RestaurantTimmsCustomer c) {
		print("msgFoodReady");
		this.lastMessage = "msgFoodReady";
		InternalCustomer customer = findCustomer(c);
		customer.setState(InternalCustomer.State.foodReady);
		stateChanged();
	}
	
	@Override
	public void msgCheckReady() {
		print("msgCheckReady");
		this.lastMessage = "msgCheckReady";
		waiterHover.release();
	}
	
	@Override
	public void msgDoNotWantFood(RestaurantTimmsCustomer c) {
		print("msgDoNotWantFood");
		this.lastMessage = "msgDoNotWantFood";
		InternalCustomer customer = findCustomer(c);
		customer.setState(InternalCustomer.State.none);
		waiterHover.release();
	}
	
	@Override
	public void guiAtCustomer() {
		print("guiAtCustomer");
		this.lastMessage = "guiAtCustomer";
		atCustomer.release();
	}
	
	@Override
	public void guiAtTable() {
		print("guiAtTable");
		this.lastMessage = "guiAtTable";
		atTable.release();
	}
	
	@Override
	public void guiAtKitchen() {
		print("guiAtKitchen");
		this.lastMessage = "guiAtKitchen";
		atKitchen.release();
	}
	
	@Override
	public void guiAtHome() {
		print("guiAtHome");
		this.lastMessage = "guiAtHome";
		atHome.release();
	}
	
	// Actions
	
	private void actAskForBreak() throws InterruptedException {
		print("actAskForBreak");
		this.lastAction = "actAskForBreak";
		rtb.host.msgAskForBreak(this);
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
		animation.goToCustomer(c.getCustomer().getAnimation(RestaurantTimmsAnimatedCustomer.class));
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
		animation.goToKitchen(rtb.cook.getAnimation(RestaurantTimmsAnimatedCook.class));
		atKitchen.acquire();
		rtb.cook.msgCookOrder(this, customer, c.getStockItem());
		waiterHover.acquire();
	}
	
	private void actDeliverOrder(InternalCustomer c) throws InterruptedException {
		print("actDeliverOrder");
		this.lastAction = "actDeliverOrder";
		RestaurantTimmsCustomer customer = c.getCustomer();
		animation.goToKitchen(rtb.cook.getAnimation(RestaurantTimmsAnimatedCook.class));
		atKitchen.acquire();
		rtb.cook.msgPickUpOrder(customer);
		menuItem = c.getStockItem().toString();
		animation.goToTable(c.getTableNumber(), c.getStockItem().toString());
		atTable.acquire();
		menuItem = null;
		customer.msgWaiterDeliveredFood(c.getStockItem());
		c.setState(InternalCustomer.State.none);
		rtb.cashier.msgComputeCheck(this, c.getCustomer(), rtb.cook.getMenuItemPrice(c.getStockItem()));
		waiterHover.acquire();
	}
	
	// Scheduler

	@Override
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
	
	@Override
	public Boolean getWantsBreak() {
		return this.wantsBreak;
	}
	
	// Set
	
	@Override
	public void setActive() {
		this.animation = this.getAnimation(RestaurantTimmsAnimatedWaiter.class);
		super.setActive();
		// TODO
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
		private Application.FOOD_ITEMS stockItem;
		
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
		
		public Application.FOOD_ITEMS getStockItem() {
			return stockItem;
		}
		
		// Set
		
		public void setState(State s) {
			this.state = s;
		}
		
		public void setStockItem(Application.FOOD_ITEMS s) {
			this.stockItem = s;
		}
	}
}
