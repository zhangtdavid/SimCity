package city.roles;

import java.util.HashMap;
import java.util.Map;

import city.Role;
import city.interfaces.MarketEmployee;

public class MarketEmployeeRole extends Role implements MarketEmployee {

//  Data
//	=====================================================================
//	Market market;
	
	private int loc; // location at front counter
	
	MarketManagerRole manager;
	MarketCashierRole cashier;
	private MarketCustomerRole customer;
	private MarketCustomerDeliveryRole customerDelivery;
	
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
		super(); // TODO
        for (String s: order.keySet()) {
        	collectedItems.put(s, 0); // initialize all values in collectedItems to 0
        }
    }

//  Messages
//	=====================================================================
	public void msgAssistCustomer(MarketCustomerRole c) {
		event = MarketEmployeeEvent.AskedToAssistCustomer;
		customer = c;
		customerDelivery = null;
		stateChanged();
	}
	
	public void msgAssistCustomerDelivery(MarketCustomerDeliveryRole c) {
		event = MarketEmployeeEvent.AskedToAssistCustomer;
		customer = null;
		customerDelivery = c;
		stateChanged();
	}
	
	public void msgHereIsMyOrder(MarketCustomerRole c, Map<String, Integer> o) {
		if (customer == c) { // Makes sure it is the same customer
			event = MarketEmployeeEvent.OrderReceived;
            for (String item: o.keySet()) {
                order.put(item, o.get(item)); // Create a deep copy of the order map
            }
            stateChanged();
		}
	}

	public void msgHereIsMyDeliveryOrder(MarketCustomerDeliveryRole c, Map<String, Integer> o) {
		if (customerDelivery == c) { // Makes sure it is the same customer
			event = MarketEmployeeEvent.OrderReceived;
            for (String item: o.keySet()) {
                order.put(item, o.get(item)); // Create a deep copy of the order map
            }
            stateChanged();
		}
	}
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
			customerDelivery.msgWhatWouldYouLike(this);
		}
	
	private void collectItems() {
        for (String item: order.keySet()) {
        	// TODO make market class with marketInventory
//        	if (marketInventory.get(item) < order.get(item) && marketInventory.get(item) > 0) {
//        		collectedItems.set(item, collectedItems.get(item) + marketInventory.get(item));
//        		marketInventory.set(item, 0);
//        	}
//        	else if (marketInventory.get(item) >= order.get(item)) {
//        		marketInventory.set(item, marketInventory.get(item) - order.get(item));
//        		collectedItems.set(item, order.get(item));
////        		marketEmployeeGui.doCollectItems(order);
//        	}
//        	if (marketInventory.get(item) < 10)
//        		manager.msgItemLow();
//        	marketEmployeeGui.doDeliverItems();
//    		try {
//			atCashier.acquire();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        	// dependent on custome type
        	if (customer != null)
        		cashier.msgComputeBill(this, customer, order, collectedItems);
        	else
        		cashier.msgComputeBill(this, customerDelivery, order, collectedItems);
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


	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
}
