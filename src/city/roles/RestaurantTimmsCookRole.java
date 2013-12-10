package city.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import trace.AlertLog;
import trace.AlertTag;
import utilities.MarketOrder;
import city.Application;
import city.Application.BUILDING;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantTimmsAnimatedCook;
import city.bases.JobRole;
import city.bases.Role;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantTimmsBuilding.InternalMarketOrder;
import city.buildings.RestaurantTimmsBuilding.MenuItem;
import city.buildings.RestaurantTimmsBuilding.MenuItem.State;
import city.buildings.RestaurantTimmsBuilding.Order;
import city.buildings.interfaces.RestaurantTimms;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.RestaurantTimmsCook;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.roles.interfaces.RestaurantTimmsWaiter;

/**
 * Restaurant cook agent.
 */
public class RestaurantTimmsCookRole extends JobRole implements RestaurantTimmsCook {
	
	// Data
	
	private Integer speed;
	private Timer timer = new Timer();
	private RestaurantTimms rtb; 
	private List<Role> roles = new ArrayList<Role>(); // For market orders
	private MarketCustomerDelivery marketCustomerDeliveryRole;
	private static final int MARKET_ORDER_SIZE = 5;
	private boolean shiftOver;
	
	// Constructor
	
	/**
	 * Construct a RestaurantTimmsCookRole.
	 * 
	 * @param b the RestaurantTimmsBuilding that this cook will work at
	 * @param shiftStart the hour (0-23) that the role's shift begins
	 * @param shiftEnd the hour (0-23) that the role's shift ends
	 */
	public RestaurantTimmsCookRole(RestaurantTimms b, int shiftStart, int shiftEnd) {
		super();
		this.setWorkplace(b);
		this.setSalary(RestaurantTimms.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		this.speed = 3;
		this.rtb = b;
		this.marketCustomerDeliveryRole = null;
		this.shiftOver = false;
	}
	
	// Messages

	@Override
	public void msgCookOrder(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, Application.FOOD_ITEMS s) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		rtb.addOrder(new Order(w, c, s));
		stateChanged();
	}
	
	@Override
	public void msgPickUpOrder(RestaurantTimmsCustomer c) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		Order order = null;
		for (Order o : rtb.getOrders()) {
			if (o.getCustomer() == c) {
				order = o;
				break;
			}
		}
		rtb.removeOrder(order);
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		//-------------------/
		// Primary Scheduler /
		//-------------------/
		
		if (shiftOver && !rtb.getCook().equals(this)) {
			print("Leaving shift.");
			super.setInactive();
			return false;
		}
		
		// High priority - respond to waiters placing orders
		for (Order order : rtb.getOrders()) {
			if (order.getState() == Order.State.pending) {
				if (!actConfirmOrder(order)) {
					rtb.removeOrder(order);
					return true;
				}
			}
		}
		
		// Normal priority - cook food
		for (Order order : rtb.getOrders()) {
			if (order.getState() == Order.State.queue) {
				actCookOrder(order);
				return true;
			}
		}
		
		// Low priority - order from market
		HashMap<FOOD_ITEMS, Integer> order = new HashMap<FOOD_ITEMS, Integer>();
		for (MenuItem m : rtb.getMenuItems()) {
			if (m.getQuantity() <= 1 && m.isInStock()) {
				order.put(m.getItem(), MARKET_ORDER_SIZE);
			}
		}
		if (order.size() > 0) { actOrderFromMarket(order); }
		
		//------------------------------------/
		// Role Scheduler (for market orders) /
		//------------------------------------/
		
		if (marketCustomerDeliveryRole != null && !marketCustomerDeliveryRole.getActive()) {
			roles.remove(marketCustomerDeliveryRole);
			marketCustomerDeliveryRole = null;
		}
		
		boolean blocking = false;
		for (Role r : roles) if (r.getActive() && r.getActivity()) {
			blocking  = true;
			boolean activity = r.runScheduler();
			if (!activity) {
				r.setActivityFinished();
			}
			break;
		}
		
		// Scheduler disposition
		return blocking;
	}
	
	// Actions
	
	private Boolean actConfirmOrder(Order o) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		Order order = o;
		if (checkStore(order.getItem())) {
			decrementMenuItem(order.getItem(), 1);
			order.setState(Order.State.queue);
			order.getWaiter().msgOrderPlaced(order.getCustomer(), true);
			return true;
		} else {
			order.getWaiter().msgOrderPlaced(order.getCustomer(), false);
			return false;
		}
	}
	
	private void actCookOrder(final Order o) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		o.setState(Order.State.cooking);
		timer.schedule(new TimerTask() {
			public void run() {
				o.setState(Order.State.ready);
				o.getWaiter().msgFoodReady(o.getCustomer());
			}
		},
		(this.speed * 1000));
	}
	
	private void actOrderFromMarket(HashMap<FOOD_ITEMS, Integer> order) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// Add the order to the restaurant's list
		MarketOrder marketOrder = new MarketOrder(order);
		InternalMarketOrder internalMarketOrder = new InternalMarketOrder(marketOrder);
		rtb.addMarketOrder(internalMarketOrder);
		
		// Say that we're ordering the food
		for (FOOD_ITEMS i : marketOrder.getOrderItems().keySet()) {
			for (MenuItem m : rtb.getMenuItems()) {
				if (m.getItem() == i) {
					m.setState(State.onOrder);
				}
			}
		}
		
		// Set up a role which will place the order
		MarketBuilding market = (MarketBuilding) Application.CityMap.findRandomBuilding(BUILDING.market);
		marketCustomerDeliveryRole = new MarketCustomerDeliveryRole(rtb, marketOrder, rtb.getCashier().getMarketPaymentRole());
		marketCustomerDeliveryRole.setPerson(this.getPerson());
		marketCustomerDeliveryRole.setMarket(market);
		marketCustomerDeliveryRole.setActive();
		roles.add((Role) marketCustomerDeliveryRole);
	}

	// Getters
	
	@Override
	public int getMenuItemPrice(Application.FOOD_ITEMS s) {
		MenuItem menuItem = findMenuItem(s);
		return menuItem.getPrice();
	}
	
	// Setters

	@Override
	public void setActive() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		rtb.setCook(this);
		shiftOver = false;
		this.getAnimation(RestaurantTimmsAnimatedCook.class).setVisible(true);
		super.setActive();
	}
	
	@Override
	public void setInactive() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		shiftOver = true;
	}
	
	// Utilities 
	
	private MenuItem findMenuItem(Application.FOOD_ITEMS s) {
		MenuItem item = null;
		for (MenuItem m : rtb.getMenuItems()) {
			if (m.getItem() == s) {
				item = m;
				break;
			}
		}
		return item;
	}
	
	private Boolean checkStore(Application.FOOD_ITEMS s) {
		MenuItem menuItem = findMenuItem(s);
		if (menuItem.getQuantity() > 0) {
			return true;
		}
		return false;
	}
	
	private void decrementMenuItem(Application.FOOD_ITEMS s, Integer i) {
		MenuItem menuItem = findMenuItem(s);
		menuItem.decrementQuantity(i);
	}
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("RestaurantTimmsCook", msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTTIMMS, "RestaurantTimmsCookRole " + this.getPerson().getName(), msg);
    }

}
