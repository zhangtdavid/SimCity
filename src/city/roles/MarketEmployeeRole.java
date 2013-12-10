package city.roles;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.MarketAnimatedEmployee;
import city.bases.JobRole;
import city.buildings.MarketBuilding;
import city.buildings.interfaces.Market;
import city.roles.interfaces.MarketCustomer;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.MarketEmployee;

public class MarketEmployeeRole extends JobRole implements MarketEmployee {
//  Data
//	=====================================================================
	public EventLog log = new EventLog();
	private Semaphore atPhone = new Semaphore(0, true);
	private Semaphore finishedCollectingItems = new Semaphore(0, true);
	private Semaphore atCashier = new Semaphore(0, true);
	private Semaphore atCounter = new Semaphore(0, true);

	private Market market;
 	
	private MarketCustomer customer;
	private MarketCustomerDelivery customerDelivery;
	private MarketCustomerDeliveryPayment customerDeliveryPayment;

	private Map<FOOD_ITEMS, Integer> order = new HashMap<FOOD_ITEMS, Integer>();
	private int orderId;
	private Map<FOOD_ITEMS, Integer> collectedItems = new HashMap<FOOD_ITEMS, Integer>();
    
	private MarketEmployeeState state;
	private MarketEmployeeEvent event;
	private WorkingState workingState = WorkingState.Working;

//	Constructor
//	=====================================================================
	public MarketEmployeeRole(Market b, int t1, int t2) {
		super();
		market = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(MarketBuilding.WORKER_SALARY);
		customer = null;
		customerDelivery = null;
		customerDeliveryPayment = null;
		state = MarketEmployeeState.None;
    }
	
//  Activity
//	=====================================================================
//	// TODO schung 99c0f4da25
//	@Override
//	public void setActive() {
//		super.setActivityBegun();
//		super.setActive();
//	}
	
	@Override
	public void setInactive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================
//	Manager
//	---------------------------------------------------------------
	@Override
	public void msgAssistCustomer(MarketCustomer c) {
		log.add(new LoggedEvent("Market Employee received msgAssistCustomer from Market Manager."));
		print("Market Employee received msgAssistCustomer from Market Manager.");
		event = MarketEmployeeEvent.AskedToAssistCustomer;
		customer = c;
		customerDelivery = null;
		customerDeliveryPayment = null;
		stateChanged();
	}
	
	@Override
	public void msgAssistCustomerDelivery(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay) {
		log.add(new LoggedEvent("Market Employee received msgAssistCustomerDelivery from Market Manager."));
		print("Market Employee received msgAssistCustomerDelivery from Market Manager.");
		event = MarketEmployeeEvent.AskedToAssistCustomer;
		customer = null;
		customerDelivery = c;
		customerDeliveryPayment = cPay;
		stateChanged();
	}
	
	@Override
	public void msgHereIsCustomerDeliveryOrder(Map<FOOD_ITEMS, Integer> o, int id) {
		log.add(new LoggedEvent("Market Employee received msgHereIsCustomerDeliveryOrder from Market Manager."));
		print("Market Employee received msgHereIsCustomerDeliveryOrder from Market Manager.");
		event = MarketEmployeeEvent.OrderReceived;
        for (FOOD_ITEMS item: o.keySet()) {
            order.put(item, o.get(item)); // Create a deep copy of the order map
        }
        orderId = id;
        stateChanged();
	}
	
//	Customer
//	---------------------------------------------------------------
	@Override
	public void msgHereIsMyOrder(MarketCustomer c, Map<FOOD_ITEMS, Integer> o, int id) {
		log.add(new LoggedEvent("Market Employee received msgHereIsMyOrder from Market Customer."));
		print("Market Employee received msgHereIsMyOrder from Market Customer.");
		if (customer == c) { // Makes sure it is the same customer
			event = MarketEmployeeEvent.OrderReceived;
            for (FOOD_ITEMS item: o.keySet()) {
                order.put(item, o.get(item)); // Create a deep copy of the order map
            }
		}
        orderId = id;
        stateChanged();
	}
	
//	Animation
//	---------------------------------------------------------------
	@Override
	public void msgAnimationAtPhone() {
		print("Market Employee received msgAnimationAtPhone");
		atPhone.release();
		stateChanged();
	}
	
	@Override
	public void msgFinishedCollectingItems() {
		print("Market Employee received msgFinishedCollectingItems");
		finishedCollectingItems.release();
		stateChanged();
	}
	
	@Override
	public void msgAnimationAtCashier() {
		print("Market Employee received msgAnimationAtCashier");
		atCashier.release();
		stateChanged();
	}
	
	@Override
	public void msgAnimationAtCounter() {
		print("Market Employee received msgAnimationAtCounter");
		atCounter.release();
		stateChanged();
	}
	
//  Scheduler
//	=====================================================================
	@Override
	public boolean runScheduler() {
		if (workingState == WorkingState.GoingOffShift) {
			if (market.getEmployees().size() > 1 && customer == null && customerDelivery == null) {
				market.removeEmployee(this);
				super.setInactive();
				this.getAnimation(MarketAnimatedEmployee.class).removeFromEmployeeStalls();
			}
		}
		
		if (state == MarketEmployeeState.None && event == MarketEmployeeEvent.AskedToAssistCustomer) {
			assistCustomer();
			return true;
		}
		
		if (state == MarketEmployeeState.AskedForOrder && event == MarketEmployeeEvent.OrderReceived) {
			collectItems();
			return true;
		}
		
		return false;
	}
	
//  Actions
//	=====================================================================
	private void assistCustomer() {
		state = MarketEmployeeState.AskedForOrder;
		if (customer != null) {
			customer.msgWhatWouldYouLike(this);
		}
		else {
			this.getAnimation(MarketAnimatedEmployee.class).doGoToPhone();
			try {
				atPhone.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			market.getManager().msgWhatWouldCustomerDeliveryLike(this);
		}
	}
	
	private void collectItems() {
        for (FOOD_ITEMS item: order.keySet()) {
        	if (market.getInventory().get(item) < order.get(item) && market.getInventory().get(item) > 0) {
        		collectedItems.put(item, collectedItems.get(item) + market.getInventory().get(item));
        		market.getInventory().put(item, 0);
        	}
        	else if (market.getInventory().get(item) >= order.get(item)) {
        		market.getInventory().put(item, market.getInventory().get(item) - order.get(item));
        		collectedItems.put(item, order.get(item));
        	}
        	if (market.getInventory().get(item) < 25)
        		market.getManager().msgItemLow();
        }
        
		this.getAnimation(MarketAnimatedEmployee.class).doCollectItems();
		try {
			finishedCollectingItems.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.getAnimation(MarketAnimatedEmployee.class).doDeliverItems();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	// dependent on customer type
    	if (customer != null)
    		market.getCashier().msgComputeBill(this, customer, order, collectedItems, orderId);
    	else
    		market.getCashier().msgComputeBill(this, customerDelivery, customerDeliveryPayment, order, collectedItems, orderId);
    	this.getAnimation(MarketAnimatedEmployee.class).doGoToCounter();
		try {
			atCounter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (workingState == WorkingState.Working)
    			market.getManager().msgIAmAvailableToAssist(this);
    	customer = null;
    	customerDelivery = null;
    	customerDeliveryPayment = null;
		state = MarketEmployeeState.None;
	}

//  Getters
//	=====================================================================
	// Market
	@Override
	public Market getMarket() {
		return market;
	}
	
	@Override
	public MarketCustomer getMarketCustomer() {
		return customer;
	}
	
	@Override
	public MarketCustomerDelivery getMarketCustomerDelivery() {
		return customerDelivery;
	}
	
	@Override
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
		return customerDeliveryPayment;
	}
	
	@Override
	public Map<FOOD_ITEMS, Integer> getOrder() {
		return order;
	}
	
	@Override
	public int getOrderId() {
		return orderId;
	}
	
	@Override
	public Map<FOOD_ITEMS, Integer> getCollectedItems() {
		return collectedItems;
	}
	
	@Override
	public MarketEmployeeState getMarketEmployeeState() {
		return state;
	}
	
	@Override
	public MarketEmployeeEvent getMarketEmployeeEvent() {
		return event;
	}
	
	@Override
	public WorkingState getWorkingState() {
		return workingState;
	}
	
	@Override
	public String getStateString() {
		return state.toString();
	}
	
//  Setters
//	=====================================================================
	@Override
	public void setMarket(Market market) {
		this.market = market;
	}
	
//  Utilities
//	=====================================================================
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("MarketEmployee", msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketEmployeeRole " + this.getPerson().getName(), msg);
    }
}