package city.roles;

import java.util.HashMap;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Role;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;

public class MarketEmployeeRole extends Role implements MarketEmployee {

//  Data
//	=====================================================================
	public EventLog log = new EventLog();

	private MarketBuilding market;
	
	private int loc; // location at front counter
	
	private MarketManager manager;
	private MarketCashier cashier;
	private MarketCustomer customer;
	private MarketCustomerDelivery customerDelivery;
	private MarketCustomerDeliveryPayment customerDeliveryPayment;
	
	private enum MarketEmployeeState
	{None, AskedForOrder};
	private MarketEmployeeState state;

	private enum MarketEmployeeEvent
	{AskedToAssistCustomer, OrderReceived};
	private MarketEmployeeEvent event;
	
    private Map<String, Integer> order = new HashMap<String, Integer>();
    private Map<String, Integer> collectedItems = new HashMap<String, Integer>();
	
//	Gui
//	---------------------------------------------------------------
//	private MarketEmployeeGui marketEmployeeGui;
//	private Semaphore atCounter = new Semaphore(0, true);
//	private Semaphore atPhone = new Semaphore(0, true);
//	private Semaphore atCashier = new Semaphore(0, true);
	
//	Constructor
//	---------------------------------------------------------------
	public MarketEmployeeRole() {
		super();
        for (String s: order.keySet()) {
        	collectedItems.put(s, 0); // initialize all values in collectedItems to 0
        }
    }

//  Messages
//	=====================================================================
//	Manager
//	---------------------------------------------------------------
	public void msgAssistCustomer(MarketCustomer c) {
		log.add(new LoggedEvent("Market Employee received msgAssistCustomer from Market Manager."));
		System.out.println("Market Employee received msgAssistCustomer from Market Manager.");
		event = MarketEmployeeEvent.AskedToAssistCustomer;
		customer = c;
		customerDelivery = null;
		customerDeliveryPayment = null;
		stateChanged();
	}
	
	public void msgAssistCustomerDelivery(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay) {
		log.add(new LoggedEvent("Market Employee received msgAssistCustomerDelivery from Market Manager."));
		System.out.println("Market Employee received msgAssistCustomerDelivery from Market Manager.");
		event = MarketEmployeeEvent.AskedToAssistCustomer;
		customer = null;
		customerDelivery = c;
		customerDeliveryPayment = cPay;
		stateChanged();
	}
	
	public void msgHereIsCustomerDeliveryOrder(Map<String, Integer> o) {
		log.add(new LoggedEvent("Market Employee received msgHereIsCustomerDeliveryOrder from Market Manager."));
		System.out.println("Market Employee received msgHereIsCustomerDeliveryOrder from Market Manager.");
		event = MarketEmployeeEvent.OrderReceived;
        for (String item: o.keySet()) {
            order.put(item, o.get(item)); // Create a deep copy of the order map
        }
        stateChanged();
	}
	
//	Customer
//	---------------------------------------------------------------
	public void msgHereIsMyOrder(MarketCustomer c, Map<String, Integer> o) {
		log.add(new LoggedEvent("Market Employee received msgHereIsMyOrder from Market Customer."));
		System.out.println("Market Employee received msgHereIsMyOrder from Market Customer.");
		if (customer == c) { // Makes sure it is the same customer
			event = MarketEmployeeEvent.OrderReceived;
            for (String item: o.keySet()) {
                order.put(item, o.get(item)); // Create a deep copy of the order map
            }
            stateChanged();
		}
	}

//	public void msgHereIsMyDeliveryOrder(MarketCustomerDelivery c, Map<String, Integer> o) {
//		log.add(new LoggedEvent("Market Employee received msgHereIsMyDeliveryOrder from Market Customer."));
//		System.out.println("Market Employee received msgHereIsMyDeliveryOrder from Market Customer.");
//		if (customerDelivery == c) { // Makes sure it is the same customer
//			event = MarketEmployeeEvent.OrderReceived;
//            for (String item: o.keySet()) {
//                order.put(item, o.get(item)); // Create a deep copy of the order map
//            }
//            stateChanged();
//		}
//	}

//	Animation
//	---------------------------------------------------------------
//	public void msgAnimationAtCounter() {
//		print("Employee at Counter");
//		atCounter.release();
//		stateChanged();
//	}
//	
//	public void msgAnimationAtPhone() {
//		print("Employee at Phone");
//		atPhone.release();
//		stateChanged();
//	}
//	
//	public void msgAnimationAtCashier() {
//		print("Employee at Cashier");
//		atCashierrelease();
//		stateChanged();
//	}
	
//  Scheduler
//	=====================================================================
	@Override
	public boolean runScheduler() {
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
		else
//			MarketEmployeeGui.doGoToPhone();
//			try {
//				atPhone.acquire();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			manager.msgWhatWouldCustomerDeliveryLike(this);
		}
	
	private void collectItems() {
        for (String item: order.keySet()) {
        	if (market.inventory.get(item) < order.get(item) && market.inventory.get(item) > 0) {
        		collectedItems.put(item, collectedItems.get(item) + market.inventory.get(item));
        		market.inventory.put(item, 0);
        	}
        	else if (market.inventory.get(item) >= order.get(item)) {
        		market.inventory.put(item, market.inventory.get(item) - order.get(item));
        		collectedItems.put(item, order.get(item));
//        		marketEmployeeGui.doCollectItems(order);
        	}
        	if (market.inventory.get(item) < 10)
        		manager.msgItemLow();
//        	marketEmployeeGui.doDeliverItems();
//    		try {
//			atCashier.acquire();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        	// dependent on customer type
        	if (customer != null)
        		cashier.msgComputeBill(this, customer, order, collectedItems);
        	else
        		cashier.msgComputeBill(this, customerDelivery, customerDeliveryPayment, order, collectedItems);
//        	marketEmployeeGui.doGoToCounter();
//    		try {
//			atCounter.acquire();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        	manager.msgIAmAvailableToAssist(this);
        	customer = null;
        	customerDelivery = null;
    		state = MarketEmployeeState.None;
        }
        	
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
	
	// Manager
	public MarketManager getManager() {
		return manager;
	}
	
	public MarketManager setManager() {
		return manager;
	}
	
	// Cashier
	public MarketCashier getCashier() {
		return cashier;
	}
	
	public void setCashier(MarketCashier cashier) {
		this.cashier = cashier;
	}
	
//  Utilities
//	=====================================================================	

}