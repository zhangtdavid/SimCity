package city.roles;

import java.util.HashMap;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketEmployee;

public class MarketEmployeeRole extends Role implements MarketEmployee {

//  Data
//	=====================================================================
	public EventLog log = new EventLog();

	private MarketBuilding market;
	
	public enum WorkingState {Working, GoingOffShift, NotWorking};
	WorkingState workingState = WorkingState.Working;
	
	private int loc; // location at front counter
	
	public MarketCustomer customer;
	public MarketCustomerDelivery customerDelivery;
	public MarketCustomerDeliveryPayment customerDeliveryPayment;
	
	public enum MarketEmployeeState {None, AskedForOrder};
	public MarketEmployeeState state;

	public enum MarketEmployeeEvent {AskedToAssistCustomer, OrderReceived};
	public MarketEmployeeEvent event;
	
    public Map<FOOD_ITEMS, Integer> order = new HashMap<FOOD_ITEMS, Integer>();
    public int orderId;
    
    public Map<FOOD_ITEMS, Integer> collectedItems = new HashMap<FOOD_ITEMS, Integer>();
	
//	Gui
//	---------------------------------------------------------------
//		TODO schung 99c0f4da25
//	private Semaphore atPhone = new Semaphore(0, true);
//	private Semaphore finishedCollectingItems = new Semaphore(0, true);
//	private Semaphore atCashier = new Semaphore(0, true);
//	private Semaphore atCounter = new Semaphore(0, true);
	
//	Constructor
//	---------------------------------------------------------------
	public MarketEmployeeRole(MarketBuilding b, int t1, int t2) {
		super();
		market = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(MarketBuilding.getWorkerSalary());
		customer = null;
		customerDelivery = null;
		customerDeliveryPayment = null;
		state = MarketEmployeeState.None;
		loc = market.employees.size(); // TODO double check this. Need to decide how to set loc for each employee
    }

//	// TODO schung 99c0f4da25
//	public void setActive() {
//		super.setActivityBegun();
//		super.setActive();
//	}
	
	public void setInactive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================
//	Manager
//	---------------------------------------------------------------
	public void msgAssistCustomer(MarketCustomer c) {
		log.add(new LoggedEvent("Market Employee received msgAssistCustomer from Market Manager."));
		System.out.println("Market Employee received msgAssistCustomer from Market Manager.");
		if (workingState != WorkingState.NotWorking) {
			event = MarketEmployeeEvent.AskedToAssistCustomer;
			customer = c;
			customerDelivery = null;
			customerDeliveryPayment = null;
			stateChanged();
		}
	}
	
	public void msgAssistCustomerDelivery(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay) {
		log.add(new LoggedEvent("Market Employee received msgAssistCustomerDelivery from Market Manager."));
		System.out.println("Market Employee received msgAssistCustomerDelivery from Market Manager.");
		if (workingState != WorkingState.NotWorking) {
			event = MarketEmployeeEvent.AskedToAssistCustomer;
			customer = null;
			customerDelivery = c;
			customerDeliveryPayment = cPay;
			stateChanged();
		}
	}
	
	public void msgHereIsCustomerDeliveryOrder(Map<FOOD_ITEMS, Integer> o, int id) {
		log.add(new LoggedEvent("Market Employee received msgHereIsCustomerDeliveryOrder from Market Manager."));
		System.out.println("Market Employee received msgHereIsCustomerDeliveryOrder from Market Manager.");
		event = MarketEmployeeEvent.OrderReceived;
        for (FOOD_ITEMS item: o.keySet()) {
            order.put(item, o.get(item)); // Create a deep copy of the order map
        }
        orderId = id;
        stateChanged();
	}
	
//	Customer
//	---------------------------------------------------------------
	public void msgHereIsMyOrder(MarketCustomer c, Map<FOOD_ITEMS, Integer> o, int id) {
		log.add(new LoggedEvent("Market Employee received msgHereIsMyOrder from Market Customer."));
		System.out.println("Market Employee received msgHereIsMyOrder from Market Customer.");
		if (customer == c) { // Makes sure it is the same customer
			event = MarketEmployeeEvent.OrderReceived;
            for (FOOD_ITEMS item: o.keySet()) {
                order.put(item, o.get(item)); // Create a deep copy of the order map
            }
		}
        orderId = id;
        stateChanged();
	}
	
//  Scheduler
//	=====================================================================
	@Override
	public boolean runScheduler() {
		if (workingState == WorkingState.GoingOffShift) {
			if (market.employees.size() > 1)
				workingState = WorkingState.NotWorking;
		}
		
		if (customer == null && customerDelivery == null && workingState == WorkingState.NotWorking)
			super.setInactive();
		
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
			customer.msgWhatWouldYouLike(this, loc);
		}
		else {
// 			TODO schung 99c0f4da25
//			this.getAnimation(MarketAnimatedEmployee.class).doGoToPhone();
//			try {
//				atPhone.acquire();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			market.manager.msgWhatWouldCustomerDeliveryLike(this);
		}
	}
	
	private void collectItems() {
        for (FOOD_ITEMS item: order.keySet()) {
        	if (market.inventory.get(item) < order.get(item) && market.inventory.get(item) > 0) {
        		collectedItems.put(item, collectedItems.get(item) + market.inventory.get(item));
        		market.inventory.put(item, 0);
        	}
        	else if (market.inventory.get(item) >= order.get(item)) {
        		market.inventory.put(item, market.inventory.get(item) - order.get(item));
        		collectedItems.put(item, order.get(item));
//     			TODO schung 99c0f4da25
//        		this.getAnimation(MarketAnimatedEmployee.class).doCollectItems();
//        		try {
//    			finishedCollectingItems.acquire();
//    			} catch (InterruptedException e) {
//    				// TODO Auto-generated catch block
//    				e.printStackTrace();
//    			}
        	}
        	if (market.inventory.get(item) < 10)
        		market.manager.msgItemLow();
// 			TODO schung 99c0f4da25
//        	this.getAnimation(MarketAnimatedEmployee.class).doDeliverItems();
//    		try {
//				atCashier.acquire();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

        }
    	// dependent on customer type
    	if (customer != null)
    		market.cashier.msgComputeBill(this, customer, order, collectedItems, orderId);
    	else
    		market.cashier.msgComputeBill(this, customerDelivery, customerDeliveryPayment, order, collectedItems, orderId);
//			TODO schung 99c0f4da25
//    	this.getAnimation(MarketAnimatedEmployee.class).doGoToCounter();
//		try {
//			atCounter.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	market.manager.msgIAmAvailableToAssist(this);
    	customer = null;
    	customerDelivery = null;
    	customerDeliveryPayment = null;
		state = MarketEmployeeState.None;
	}


//  Getters and Setters
//	=====================================================================
	// Market
	public MarketBuilding getMarket() {
		return market;
	}
	
	public void setMarket(MarketBuilding market) {
		this.market = market;
	}
	
//  Utilities
//	=====================================================================	
}