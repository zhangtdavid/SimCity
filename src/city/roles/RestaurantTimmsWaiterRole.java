package city.roles;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import city.Application;
import city.animations.interfaces.RestaurantTimmsAnimatedCook;
import city.animations.interfaces.RestaurantTimmsAnimatedCustomer;
import city.animations.interfaces.RestaurantTimmsAnimatedWaiter;
import city.bases.JobRole;
import city.buildings.interfaces.RestaurantTimms;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.roles.interfaces.RestaurantTimmsWaiter;

/**
 * Restaurant waiter agent.
 */
public class RestaurantTimmsWaiterRole extends JobRole implements RestaurantTimmsWaiter {
	
	// Data
	
	private Boolean wantsBreak;
	private int tiredness;
	private Timer timer = new Timer();
	private RestaurantTimms rtb;
	private boolean shiftOver;
	
	private List<InternalCustomer> customers = new ArrayList<InternalCustomer>();
	
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
	public RestaurantTimmsWaiterRole(RestaurantTimms b, int shiftStart, int shiftEnd) {
		super();
		this.setWorkplace(b);
		this.setSalary(RestaurantTimms.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		this.wantsBreak = false;
		this.tiredness = 15;
		this.rtb = b;
		this.shiftOver = false;
	}
	
	// Messages
	
	@Override
	public void msgWantBreak() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.wantsBreak = true;
		stateChanged();
	}
	
	@Override
	public void msgAllowBreak(Boolean r) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.wantsBreak = r;
		waiterHover.release();
	}
	
	@Override
	public void msgSeatCustomer(RestaurantTimmsCustomer c, int n) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		customers.add(new InternalCustomer(c, n, InternalCustomer.State.seat));
		stateChanged();
	}
	
	@Override
	public void msgWantFood(RestaurantTimmsCustomer c) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		InternalCustomer customer = findCustomer(c);
		customer.setState(InternalCustomer.State.order);
		stateChanged();
	}
	
	@Override
	public void msgOrderFood(RestaurantTimmsCustomer c, Application.FOOD_ITEMS s) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		InternalCustomer customer = findCustomer(c);
		customer.setStockItem(s);
		customer.setState(InternalCustomer.State.hasOrdered);
		waiterHover.release();
	}
	
	@Override
	public void msgOrderPlaced(RestaurantTimmsCustomer c, Boolean inStock) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
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
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		InternalCustomer customer = findCustomer(c);
		customer.setState(InternalCustomer.State.foodReady);
		stateChanged();
	}
	
	@Override
	public void msgCheckReady() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		waiterHover.release();
	}
	
	@Override
	public void msgDoNotWantFood(RestaurantTimmsCustomer c) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		InternalCustomer customer = findCustomer(c);
		customer.setState(InternalCustomer.State.none);
		waiterHover.release();
	}
	
	@Override
	public void guiAtCustomer() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		atCustomer.release();
	}
	
	@Override
	public void guiAtTable() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		atTable.release();
	}
	
	@Override
	public void guiAtKitchen() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		atKitchen.release();
	}
	
	@Override
	public void guiAtHome() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		atHome.release();
	}
	
	// Scheduler

	@Override
	public boolean runScheduler() {
		InternalCustomer temp = null;
		
		if (shiftOver) {
			boolean disposition = true;
			for (InternalCustomer c : customers) {
				if (c.getState() != InternalCustomer.State.foodDelivered) {
					disposition = false;
					break;
				}
			}
			if (disposition) {
				print("Leaving shift.");
				customers.clear();
				super.setInactive();
				return false;
			}
		}
		
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
	
	// Actions
	
	private void actAskForBreak() throws InterruptedException {
		print("actAskForBreak");
		rtb.getHost().msgAskForBreak(this);
		waiterHover.acquire();
		
		if (wantsBreak) {
			this.getAnimation(RestaurantTimmsAnimatedWaiter.class).goOnBreak();
			timer.schedule(new TimerTask() {
				public void run() {
					wantsBreak = false;
					RestaurantTimmsWaiterRole.this.getAnimation(RestaurantTimmsAnimatedWaiter.class).goOffBreak();
					waiterBreak.release();
				}
			},
			(this.tiredness * 1000));
		} else {
			waiterBreak.release();
		}
	}
	
	private void actSeatCustomer(InternalCustomer c) throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.getAnimation(RestaurantTimmsAnimatedWaiter.class).goToCustomer(c.getCustomer().getAnimation(RestaurantTimmsAnimatedCustomer.class));
		atCustomer.acquire();
		this.getAnimation(RestaurantTimmsAnimatedWaiter.class).goToTable(c.getTableNumber(), null);
		c.getCustomer().msgGoToTable(this, c.getTableNumber());
		atTable.acquire();
		c.setState(InternalCustomer.State.none);
	}
	
	private void actGoToHome() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		int homePosition = rtb.getWaiterIndex(this);
		RestaurantTimmsAnimatedWaiter animation = this.getAnimation(RestaurantTimmsAnimatedWaiter.class);
		animation.goToHome(homePosition);
		atHome.acquire();
	}
	
	private void actTakeOrder(InternalCustomer c) throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		RestaurantTimmsCustomer customer = c.getCustomer();
		this.getAnimation(RestaurantTimmsAnimatedWaiter.class).goToTable(c.getTableNumber(), null);
		atTable.acquire();
		customer.msgOrderFromWaiter();
		waiterHover.acquire();
	}
	
	private void actPlaceOrder(InternalCustomer c) throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		RestaurantTimmsCustomer customer = c.getCustomer();
		this.getAnimation(RestaurantTimmsAnimatedWaiter.class).goToKitchen(rtb.getCook().getAnimation(RestaurantTimmsAnimatedCook.class));
		atKitchen.acquire();
		rtb.getCook().msgCookOrder(this, customer, c.getStockItem());
		waiterHover.acquire();
	}
	
	private void actDeliverOrder(InternalCustomer c) throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		RestaurantTimmsCustomer customer = c.getCustomer();
		this.getAnimation(RestaurantTimmsAnimatedWaiter.class).goToKitchen(rtb.getCook().getAnimation(RestaurantTimmsAnimatedCook.class));
		atKitchen.acquire();
		rtb.getCook().msgPickUpOrder(customer);
		this.getAnimation(RestaurantTimmsAnimatedWaiter.class).goToTable(c.getTableNumber(), c.getStockItem().toString());
		atTable.acquire();
		customer.msgWaiterDeliveredFood(c.getStockItem());
		c.setState(InternalCustomer.State.foodDelivered);
		rtb.getCashier().msgComputeCheck(this, c.getCustomer(), rtb.getCook().getMenuItemPrice(c.getStockItem()));
		waiterHover.acquire();
	}
	
	// Getters
	
	@Override
	public Boolean getWantsBreak() {
		return this.wantsBreak;
	}
	
	@Override
	public List<InternalCustomer> getCustomers() {
		return customers;
	}
	
	// Setters
	
	@Override
	public void setActive() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		rtb.addWaiter(this);
		shiftOver = false;
		this.getAnimation(RestaurantTimmsAnimatedWaiter.class).setVisible(true);
		super.setActive();
	}
	
	@Override
	public void setInactive() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		shiftOver = true;
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("RestaurantTimmsWaiter", msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTTIMMS, "RestaurantTimmsWaiterRole " + this.getPerson().getName(), msg);
    }
	
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
		public enum State { none, seat, order, hasOrdered, makingFood, foodReady, foodDelivered };
		
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
