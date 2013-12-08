package city.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import city.Application;
import city.animations.interfaces.RestaurantTimmsAnimatedCashier;
import city.animations.interfaces.RestaurantTimmsAnimatedCustomer;
import city.bases.Role;
import city.buildings.RestaurantTimmsBuilding.MenuItem;
import city.buildings.interfaces.RestaurantTimms;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.roles.interfaces.RestaurantTimmsWaiter;

/**
 * Restaurant customer agent.
 */
public class RestaurantTimmsCustomerRole extends Role implements RestaurantTimmsCustomer {
	
	// Data
	
	public enum State { none, goToRestaurant, waitingInLine, longLine, goToTable, orderFromWaiter, hasOrdered, waiterDeliveredFood };
	private State state = State.none;
	
	private int tableNumber;
	private Application.FOOD_ITEMS eatingItem;
	private MenuItem orderItem;
	private RestaurantTimmsWaiter waiter = null;
	private Timer timer = new Timer();
	private RestaurantTimms rtb;
	
	private List<MenuItem> failedItems = new ArrayList<MenuItem>();
	
	private Semaphore atRestaurant = new Semaphore(0, true);
	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore atCashier = new Semaphore(0, true);
	private Semaphore atExit = new Semaphore(0, true);
	private Semaphore customerHover = new Semaphore(0, true);
	
	// Constructor

	public RestaurantTimmsCustomerRole(){
		super();
		this.eatingItem = null;
		this.orderItem = null;
		this.state = State.none;
		this.rtb = null;
	}
	
	// Messages
	
	@Override
	public void msgRestaurantFull() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		 Double outcome = Math.random();
		 // 50/50 whether customer decides to leave or stay
		 if (outcome < 0.5) {
			 if (state == State.goToRestaurant || state == State.waitingInLine) {
				 state = State.longLine;
				 stateChanged();
			 }
		 }
	}
	
	@Override
	public void msgGoToTable(RestaurantTimmsWaiter w, int position) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		waiter = w;
		tableNumber = position;
		state = State.goToTable;
		stateChanged();
	}
	
	@Override
	public void msgOrderFromWaiter() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		if (state != State.hasOrdered)
			state = State.orderFromWaiter;
		stateChanged();
	}
	
	@Override
	public void msgWaiterDeliveredFood(Application.FOOD_ITEMS stockItem) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.eatingItem = stockItem;
		state = State.waiterDeliveredFood;
		stateChanged();
	}
	
	@Override
	public void msgPaidCashier(int change) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.getPerson().setCash(change);
		customerHover.release();
	}
	
	@Override
	public void guiAtLine() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		atRestaurant.release();
	}
	
	@Override
	public void guiAtTable() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		atTable.release();
	}
	
	@Override
	public void guiAtCashier() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		atCashier.release();
	}
	
	@Override
	public void guiAtExit() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		atExit.release();
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		switch (state) {
			case goToRestaurant:
				try {
					actGoToRestaurant();
				} catch (InterruptedException e) {}
				break;
			case goToTable:
				try {
					actGoToTable();
					actReadMenu();
				} catch (InterruptedException e) {}
				break;
			case orderFromWaiter:
				try {
					actOrderFromWaiter();
				} catch (InterruptedException e) {}
				break;
			case hasOrdered:
				try {
					actOrderFromWaiter();
				} catch (InterruptedException e) {}
				break;
			case waiterDeliveredFood:
				actEatFood();
				break;
			case longLine:
				try {
					actLeaveRestaurant();
				} catch (InterruptedException e) {}
				break;
			default:
				break;
		}
		return false;
	}
	
	// Actions
	
	private void actGoToRestaurant() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.getAnimation(RestaurantTimmsAnimatedCustomer.class).goToRestaurant();
		atRestaurant.acquire();
		rtb.getHost().msgWantSeat(this);
		state = State.waitingInLine;
	}
	
	private void actGoToTable() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.getAnimation(RestaurantTimmsAnimatedCustomer.class).goToTable(tableNumber);
		atTable.acquire();
	}
	
	private void actReadMenu() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		timer.schedule(new TimerTask() {
			public void run() {
				waiter.msgWantFood(RestaurantTimmsCustomerRole.this);
			}
		},
		(PICKINESS * 1000));
	}
	
	private void actOrderFromWaiter() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// If re-ordering, add the last order to the list of failed items
		Boolean reOrder = false;
		if (state == State.hasOrdered) {
			failedItems.add(orderItem);
			orderItem = null;
			reOrder = true;
		} 
		
		for (MenuItem i : rtb.getMenuItems()) {
			int price = i.getPrice();
			if ((this.getPerson().getCash() - price)  >= -2 && !failedItems.contains(i)) {
				orderItem = i;
			}
		}

		// Otherwise, he will leave
		if (orderItem == null) {
			waiter.msgDoNotWantFood(this);
			actLeaveRestaurant();
		} else {
			waiter.msgOrderFood(this, orderItem.getItem());
			if (reOrder) {
				this.getAnimation(RestaurantTimmsAnimatedCustomer.class).setPlate("! ");
			} else {
				this.getAnimation(RestaurantTimmsAnimatedCustomer.class).setPlate("? ");
				state = State.hasOrdered;
			}
		}
	}
	
	private void actEatFood() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.getAnimation(RestaurantTimmsAnimatedCustomer.class).setPlate(eatingItem.toString());
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					actLeaveRestaurant();
				} catch (InterruptedException e) {}
			}
		},
		(HUNGER * 1000));
	}
	
	private void actLeaveRestaurant() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		if (state == State.longLine) {
			// Left because line was too long
			rtb.getHost().msgDoNotWantSeat(this);
			this.getAnimation(RestaurantTimmsAnimatedCustomer.class).goToExit();
			atExit.acquire();
		} else if (orderItem != null) {
			// Food has been eaten, must pay cashier
			rtb.getHost().msgLeaving(this, tableNumber);
			this.getAnimation(RestaurantTimmsAnimatedCustomer.class).goToCashier(rtb.getCashier().getAnimation(RestaurantTimmsAnimatedCashier.class));
			atCashier.acquire();
			rtb.getCashier().msgMakePayment(this, this.getPerson().getCash());
			customerHover.acquire();
			this.getAnimation(RestaurantTimmsAnimatedCustomer.class).goToExit();
			atExit.acquire();
			state = State.none;
		} else {
			// Nothing was ordered because food was too expensive
			rtb.getHost().msgLeaving(this, tableNumber);
			this.getAnimation(RestaurantTimmsAnimatedCustomer.class).goToExit();
			atExit.acquire();
		}
		super.setInactive();
	}
	
	// Getters
	
	@Override
	public State getState() {
		return state;
	}
	
	@Override
	public Application.FOOD_ITEMS getOrderItem() {
		return orderItem.getItem();
	}
	
	@Override
	public String getStateString() {
		return this.state.toString();
	}
	
	// Setters
	
	@Override
	public void setActive() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.eatingItem = null;
		this.orderItem = null;
		this.state = State.goToRestaurant;
		this.getAnimation(RestaurantTimmsAnimatedCustomer.class).setVisible(true);
		super.setActive();
		stateChanged();
	}
	
	@Override
	public void setInactive() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	@Override
	public void setRestaurantTimmsBuilding(RestaurantTimms b) {
		this.rtb = b;
	}
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("RestaurantTimmsCustomer", msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTTIMMS, "RestaurantTimmsCustomerRole " + this.getPerson().getName(), msg);
    }
}

