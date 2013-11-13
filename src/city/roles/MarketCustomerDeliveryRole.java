package city.roles;

import java.util.HashMap;
import java.util.Map;

import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketEmployee;
import base.Role;

public class MarketCustomerDeliveryRole extends Role implements MarketCustomerDelivery {

//  Data
//	=====================================================================	
    private Map<String, Integer> order = new HashMap<String, Integer>();
    private Map<String, Integer> receivedItems = new HashMap<String, Integer>();

	private MarketManagerRole manager;
	private MarketCashierRole cashier;
	private MarketEmployeeRole employee;
		
	double money;
	double bill;
	
	private enum MarketCustomerState
	{None, WaitingForService, WaitingForOrder, Paying, WaitingForDelivery};
	MarketCustomerState state;
	
	private enum MarketCustomerEvent
	{NeedOrderFromMarket, AskedForOrder, OrderReady, PaymentReceived};
	MarketCustomerEvent event;
	
//	Constructor
//	---------------------------------------------------------------
	public MarketCustomerDeliveryRole() {
		super(); // TODO
        for (String s: order.keySet()) {
        	receivedItems.put(s, 0); // initialize all values in collectedItems to 0
        }
    }	
	
//  Messages
//	=====================================================================	
	public void msgWhatWouldYouLike(MarketEmployeeRole e) {
		System.out.println("Market customerDelivery received msgWhatWouldYouLike");
		event = MarketCustomerEvent.AskedForOrder;
		employee = e;
		stateChanged();
	}
	
	public void msgHereIsBill(double bill) {
		System.out.println("Market customerDelivery received msgHereIsOrderandBill");
		event = MarketCustomerEvent.OrderReady;
        this.bill = bill;
		stateChanged();
	}
		
	public void msgPaymentReceived() {
		System.out.println("Market customerDelivery received msgPaymentReceived");
		state = MarketCustomerState.WaitingForDelivery;
	}
	
	public void msgHereIsOrder(Map<String, Integer> collectedItems) {
		System.out.println("Market customerDelivery received msgHereIsOrder");
		state = MarketCustomerState.None;
        for (String item: collectedItems.keySet()) {
            receivedItems.put(item, collectedItems.get(item)); // Create a deep copy of the order map
        }
        event = MarketCustomerEvent.PaymentReceived;
	}
	
//  Scheduler
//	=====================================================================	

	@Override
	public boolean runScheduler() {
		if (state == MarketCustomerState.None && event == MarketCustomerEvent.NeedOrderFromMarket) {
			callMarket();
			return true;
		}
		if (state == MarketCustomerState.WaitingForService && event == MarketCustomerEvent.AskedForOrder) {
			giveOrder();
			return true;
		}
		if (state == MarketCustomerState.WaitingForOrder && event == MarketCustomerEvent.OrderReady) {
			pay();
			return true;
		}
		return false;
	}


	
//  Actions
//	=====================================================================	
	private void callMarket() {
		state = MarketCustomerState.WaitingForService;
		manager.msgIWouldLikeToPlaceADeliveryOrder(this);			
	}
	
	private void giveOrder() {
		state = MarketCustomerState.WaitingForOrder;
		employee.msgHereIsMyDeliveryOrder(this, order);	
	}
	
	private void pay() {
		state = MarketCustomerState.Paying;
//		double payment = checkBill(); TODO
//		cashier.msgHereIsPayment(this, payment);			
	}

	// Getters
	
	// Setters
	
//  Utilities
//	=====================================================================	
	
	// Classes
}
