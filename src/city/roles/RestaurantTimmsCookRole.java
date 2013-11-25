package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.Application;
import city.Role;
import city.buildings.RestaurantTimmsBuilding;
import city.interfaces.RestaurantTimmsCook;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsWaiter;

/**
 * Restaurant cook agent.
 */
public class RestaurantTimmsCookRole extends Role implements RestaurantTimmsCook {
	// Data
	
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	private static List<MenuItem> menuItems = Collections.synchronizedList(new ArrayList<MenuItem>());
	// private List<MarketAgent> markets = new ArrayList<MarketAgent>(); // TODO
	
	private Integer speed;
	private Timer timer = new Timer();
	
	private Integer KITCHEN_STORE_MIN = 2;
	private Integer KITCHEN_STORE_MAX = 2;
	private Integer MENU_PRICE_MIN = 5;
	private Integer MENU_PRICE_MAX = 12;
	
	Semaphore marketResponse = new Semaphore(0, true);
	
	// Constructor
	
	/**
	 * Construct a RestaurantTimmsCookRole.
	 * 
	 * @param b the RestaurantTimmsBuilding that this cook will work at
	 * @param shiftStart the hour (0-23) that the role's shift begins
	 * @param shiftEnd the hour (0-23) that the role's shift ends
	 */
	public RestaurantTimmsCookRole(RestaurantTimmsBuilding b, int shiftStart, int shiftEnd){
		super();
		this.setWorkplace(b);
		this.setSalary(RestaurantTimmsBuilding.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		this.speed = 3;
		
// TODO		
//		// Create the menu. This does not order from the Market.
//		for (MarketAgent.StockItem stockItem : MarketAgent.stockItems) {
//			Integer randomAmount = KITCHEN_STORE_MIN + (int)(Math.random() * ((KITCHEN_STORE_MAX - KITCHEN_STORE_MIN) + 1));
//			Integer randomPrice = MENU_PRICE_MIN + (int)(Math.random() * ((MENU_PRICE_MAX - MENU_PRICE_MIN) + 1));
//			menuItems.add(new MenuItem(stockItem, randomAmount, randomPrice));
//			print("Tonight's menu - " + stockItem.toString() + " - " + randomAmount + "x - $" + randomPrice);
//		}
	}
	
	// Messages

	@Override
	public void msgCookOrder(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, Application.FOOD_ITEMS s) {
		print("msgCookOrder");
		orders.add(new Order(w, c, s));
		stateChanged();
	}
	
	@Override
	public void msgPickUpOrder(RestaurantTimmsCustomer c) {
		print("msgPickUpOrder");
		Order order = null;
		for (Order o : orders) {
			if (o.getCustomer() == c) {
				order = o;
				break;
			}
		}
		orders.remove(order);
	}

	public void msgMarketOrderPlaced(Application.FOOD_ITEMS s, Boolean inStock) {
		print("msgMarketOrderPlaced");
		MenuItem menuItem = findMenuItem(s);
		if (inStock) {
			menuItem.setState(MenuItem.State.onOrder);
		}
		marketResponse.release();
	}
	
	@Override
	public void msgMarketOrderDelivered(Application.FOOD_ITEMS s, int quantity) {
		print("msgMarketOrderDelivered");
		MenuItem menuItem = findMenuItem(s);
		menuItem.incrementQuantityOnHand(quantity);
		menuItem.setState(MenuItem.State.inStock);
		stateChanged();
	}
	
	// Actions
	
	private Boolean actConfirmOrder(Order o) {
		print("actConfirmOrder");
		Order order = o;
		if (checkStore(order.getStockItem())) {
			decrementMenuItem(order.getStockItem(), 1);
			order.setState(Order.State.queue);
			order.getWaiter().msgOrderPlaced(order.getCustomer(), true);
			return true;
		} else {
			order.getWaiter().msgOrderPlaced(order.getCustomer(), false);
			return false;
		}
	}
	
	private void actCookOrder(final Order o) {
		print("actCookOrder - " + o.getStockItem().toString());
		o.setState(Order.State.cooking);
		timer.schedule(new TimerTask() {
			public void run() {
				o.setState(Order.State.ready);
				o.getWaiter().msgFoodReady(o.getCustomer());
			}
		},
		(this.speed * 1000));
	}
	
	private void actOrderFromMarket(MenuItem m) throws InterruptedException {
		print("actOrderFromMarket");
// TODO
//		m.setState(MenuItem.State.ordering);
//		Integer randomAmount = KITCHEN_STORE_MIN + (int)(Math.random() * ((KITCHEN_STORE_MAX - KITCHEN_STORE_MIN) + 1));
//		
//		// Try ordering from all markets
//		for (MarketAgent market : markets) {
//			market.msgPlaceOrder(this, cashier, m.getStockItem(), randomAmount);
//			marketResponse.acquire();
//			if (m.getState() == MenuItem.State.onOrder) {
//				return;
//			}
//		}
//		
//		// If no markets have it, remove it from the menu
//		m.setState(MenuItem.State.offMenu);
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		synchronized(orders) {
			// High priority - respond to waiters placing orders
			for (Order order : orders) {
				if (order.getState() == Order.State.pending) {
					if (!actConfirmOrder(order)) {
						orders.remove(order);
						return true;
					}
				}
			}
			
			// Normal priority - cook food
			for (Order order : orders) {
				if (order.getState() == Order.State.queue) {
					actCookOrder(order);
					return true;
				}
			}
		}
		
		synchronized(menuItems) {
			// Low priority - order from market
			for (MenuItem menuItem : menuItems) {
				if (menuItem.getQuantityOnHand() <= 1 && menuItem.getState() == MenuItem.State.inStock) {
					try {
						actOrderFromMarket(menuItem);
					} catch (InterruptedException e) {}
				}
			}
		}
		
		// Fall through
		return false;
	}
	
	// Get
	
	@Override
	public int getMenuItemPrice(Application.FOOD_ITEMS s) {
		MenuItem menuItem = findMenuItem(s);
		return menuItem.getPrice();
	}
	
	// Set

	@Override
	public void setActive() {
		// TODO
		super.setActive();
	}
	
	// Utilities 
	
	private static MenuItem findMenuItem(Application.FOOD_ITEMS s) {
		MenuItem item = null;
		for (MenuItem menuItem : menuItems) {
			if (menuItem.getStockItem() == s) {
				item = menuItem;
				break;
			}
		}
		return item;
	}
	
	private Boolean checkStore(Application.FOOD_ITEMS s) {
		MenuItem menuItem = findMenuItem(s);
		if (menuItem.getQuantityOnHand() > 0) {
			return true;
		}
		return false;
	}
	
	private void decrementMenuItem(Application.FOOD_ITEMS s, Integer i) {
		MenuItem menuItem = findMenuItem(s);
		menuItem.decrementQuantityOnHand(i);
	}
	
	// Order Class

	private static class Order {
		private RestaurantTimmsWaiter waiter;
		private RestaurantTimmsCustomer customer;
		private Application.FOOD_ITEMS stockItem;
		
		public enum State { pending, queue, cooking, ready };
		private State state;

		Order(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, Application.FOOD_ITEMS s) {
			this.waiter = w;
			this.customer = c;
			this.stockItem = s;
			this.state = State.pending;
		}
		
		private RestaurantTimmsWaiter getWaiter() {
			return this.waiter;
		}
		
		private RestaurantTimmsCustomer getCustomer() {
			return this.customer;
		}
		
		private Application.FOOD_ITEMS getStockItem() {
			return this.stockItem;
		}
		
		private State getState() {
			return this.state;
		}
		
		private void setState(State s) {
			this.state = s;
		}
	}
	
	// MenuItem Class
	
	public static class MenuItem {
		private Application.FOOD_ITEMS stockItem;
		private Integer quantityOnHand;
		private Integer price;
		
		private static enum State { inStock, ordering, onOrder, offMenu };
		private State state;
		
		public MenuItem(Application.FOOD_ITEMS s, Integer quantityOnHand, Integer price) {
			this.stockItem = s;
			this.quantityOnHand = quantityOnHand;
			this.price = price;
			this.state = State.inStock;
		}
		
		public Application.FOOD_ITEMS getStockItem() {
			return stockItem;
		}
		
		public Integer getQuantityOnHand() {
			return quantityOnHand;
		}
		
		public Integer getPrice() {
			return price;
		}
		
		public State getState() {
			return state;
		}
		
		public void setState(State s) {
			this.state = s;
		}
		
		public void incrementQuantityOnHand(Integer q) {
			this.quantityOnHand = (this.quantityOnHand + q);
		}
		
		public void decrementQuantityOnHand(Integer q) {
			this.quantityOnHand = (this.quantityOnHand - q);
		}
	}

}
