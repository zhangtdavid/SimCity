package city.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.Application;
import city.Role;
import city.animations.interfaces.RestaurantTimmsAnimatedCashier;
import city.animations.interfaces.RestaurantTimmsAnimatedCustomer;
import city.interfaces.RestaurantTimmsCashier;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsHost;
import city.interfaces.RestaurantTimmsWaiter;

/**
 * Restaurant customer agent.
 */
public class RestaurantTimmsCustomerRole extends Role implements RestaurantTimmsCustomer {
	// Data
	
	public enum State { none, goToRestaurant, waitingInLine, longLine, goToTable, orderFromWaiter, hasOrdered, waiterDeliveredFood };
	private State state = State.none;
	
	public int pickiness;
	public int hunger;
	private int tableNumber;
	private Application.FOOD_ITEMS eatingItem;
	public Application.FOOD_ITEMS orderItem;
	public int money;
	private RestaurantTimmsCashier cashier;
	private RestaurantTimmsHost host;
	private RestaurantTimmsWaiter waiter;
	private Timer timer = new Timer();
	private RestaurantTimmsAnimatedCustomer animation = null;
	
	private List<Application.FOOD_ITEMS> failedItems = new ArrayList<Application.FOOD_ITEMS>();
	
	private Semaphore atRestaurant = new Semaphore(0, true);
	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore atCashier = new Semaphore(0, true);
	private Semaphore atExit = new Semaphore(0, true);
	private Semaphore customerHover = new Semaphore(0, true); 
	
	private Integer MONEY_MIN = 5;
	private Integer MONEY_MAX = 12;
	
	// Constructor

	public RestaurantTimmsCustomerRole(RestaurantTimmsHost host, RestaurantTimmsCashier cashier){
		super();
		this.eatingItem = null;
		this.orderItem = null;
		this.hunger = 5;
		this.pickiness = 3;
		this.host = host;
		this.cashier = cashier;
		this.money = 0;
		this.state = State.none;
	}
	
	// Messages
	
	@Override
	public void msgGoToRestaurant() {
		print("msgGoToRestaurant");
		money += (MONEY_MIN + (int)(Math.random() * ((MONEY_MAX - MONEY_MIN) + 1)));
		eatingItem = null;
		orderItem = null;
		state = State.goToRestaurant;
		stateChanged();
	}
	
	@Override
	public void msgRestaurantFull() {
		print("msgRestaurantFull");
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
		print("msgGoToTable");
		waiter = w;
		tableNumber = position;
		state = State.goToTable;
		stateChanged();
	}
	
	@Override
	public void msgOrderFromWaiter() {
		print("msgOrderFromWaiter");
		if (state != State.hasOrdered)
			state = State.orderFromWaiter;
		stateChanged();
	}
	
	@Override
	public void msgWaiterDeliveredFood(Application.FOOD_ITEMS stockItem) {
		print("msgWaiterDeliveredFood");
		this.eatingItem = stockItem;
		state = State.waiterDeliveredFood;
		stateChanged();
	}
	
	@Override
	public void msgPaidCashier(int change) {
		print("msgPaidCashier");
		money = change;
		customerHover.release();
	}
	
	@Override
	public void guiAtLine() {
		print("guiAtLine");
		atRestaurant.release();
	}
	
	@Override
	public void guiAtTable() {
		print("guiAtTable");
		atTable.release();
	}
	
	@Override
	public void guiAtCashier() {
		print("guiAtCashier");
		atCashier.release();
	}
	
	@Override
	public void guiAtExit() {
		print("guiAtExit");
		atExit.release();
	}
	
	// Actions
	
	private void actGoToRestaurant() throws InterruptedException {
		print("actGoToRestaurant");
		animation.goToRestaurant();
		atRestaurant.acquire();
		host.msgWantSeat(this);
		state = State.waitingInLine;
	}
	
	private void actGoToTable() throws InterruptedException {
		print("actGoToTable");
		animation.goToTable(tableNumber);
		atTable.acquire();
	}
	
	private void actReadMenu() {
		print("actReadMenu");
		timer.schedule(new TimerTask() {
			public void run() {
				waiter.msgWantFood(RestaurantTimmsCustomerRole.this);
			}
		},
		(pickiness * 1000));
	}
	
	private void actOrderFromWaiter() throws InterruptedException {
		print("actOrderFromWaiter");
		
		// If re-ordering, add the last order to the list of failed items
		Boolean reOrder = false;
		if (state == State.hasOrdered) {
			failedItems.add(orderItem);
			orderItem = null;
			reOrder = true;
		} 
		
// TODO
//		// Customer will order any item that is UP TO $2 more than he has
//		for (Application.MARKET_ITEMS stockItem : MarketAgent.stockItems) {
//			int price = CookAgent.getMenuItemPrice(stockItem);
//			if ((money - price) >= -2 && !failedItems.contains(stockItem)) {
//				orderItem = stockItem;
//			}
//		}
		
		// TODO temporary replacement for market
		orderItem = Application.FOOD_ITEMS.steak;

		// Otherwise, he will leave
		if (orderItem == null) {
			waiter.msgDoNotWantFood(this);
			actLeaveRestaurant();
		} else {
			waiter.msgOrderFood(this, orderItem);
			if (reOrder) {
				animation.setPlate("! ");
			} else {
				animation.setPlate("? ");
				state = State.hasOrdered;
			}
		}
	}
	
	private void actEatFood() {
		print("actEatFood");
		animation.setPlate(eatingItem.toString());
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					actLeaveRestaurant();
				} catch (InterruptedException e) {}
			}
		},
		(hunger * 1000));
	}
	
	private void actLeaveRestaurant() throws InterruptedException {
		print("actLeaveRestaurant");
		if (state == State.longLine) {
			// Left because line was too long
			host.msgDoNotWantSeat(this);
			animation.goToExit();
			atExit.acquire();
		} else if (orderItem != null) {
			// Food has been eaten, must pay cashier
			host.msgLeaving(this, tableNumber);
			animation.goToCashier(cashier.getAnimation(RestaurantTimmsAnimatedCashier.class));
			atCashier.acquire();
			cashier.msgMakePayment(this, money);
			customerHover.acquire();
			animation.goToExit();
			atExit.acquire();
			state = State.none;
		} else {
			// Nothing was ordered because food was too expensive
			host.msgLeaving(this, tableNumber);
			animation.goToExit();
			atExit.acquire();
		}
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
	
	// Get
	
	// Set
	
	@Override
	public void setHost(RestaurantTimmsHost h) {
		this.host = h;
	}
	
	@Override
	public void setActive() {
		this.animation = this.getAnimation(RestaurantTimmsAnimatedCustomer.class);
		super.setActive();
	}
	
}

