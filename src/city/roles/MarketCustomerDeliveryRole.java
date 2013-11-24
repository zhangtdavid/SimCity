package city.roles;

import java.util.HashMap;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;
import city.Application.FOOD_ITEMS;
import city.Role;

public class MarketCustomerDeliveryRole extends Role implements MarketCustomerDelivery {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private MarketBuilding market;
	private MarketManager manager;
	private MarketEmployee employee;
	
	private MarketCustomerDeliveryPaymentRole restaurantCashier;
	
	private MarketOrder order;
	private Map<FOOD_ITEMS, Integer> receivedItems = new HashMap<FOOD_ITEMS, Integer>();
		
	int money;
	int bill;
	
	private enum MarketCustomerState
	{None, WaitingForOrder, Paying, WaitingForDelivery};
	MarketCustomerState state;
	
	private enum MarketCustomerEvent
	{NeedOrderFromMarket, OrderReady, PaymentReceived};
	MarketCustomerEvent event;
	
//	Constructor
//	---------------------------------------------------------------
	public MarketCustomerDeliveryRole(MarketOrder o, MarketCustomerDeliveryPaymentRole c) {
		super(); // TODO
        for (FOOD_ITEMS s: order.orderItems.keySet()) {
        	receivedItems.put(s, 0); // initialize all values in receivedItems to 0
        }
        restaurantCashier = c;
    }	
	
//  Messages
//	=====================================================================	
//	public void msgWhatWouldYouLike(MarketEmployee e) {
//		log.add(new LoggedEvent("Market CustomerDelivery received msgWhatWouldYouLike from Market Employee."));
//		System.out.println("Market CustomerDelivery received msgWhatWouldYouLike from Market Employee.");
//		event = MarketCustomerEvent.AskedForOrder;
//		employee = e;
//		stateChanged();
//	}
	
	public void msgHereIsOrder(Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("Market CustomerDelivery received msgHereIsOrder from Market DeliveryPerson."));
		System.out.println("Market customerDelivery received msgHereIsOrder from Market DeliveryPerson.");
		state = MarketCustomerState.None;
        for (FOOD_ITEMS item: collectedItems.keySet()) {
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
//		if (state == MarketCustomerState.WaitingForService && event == MarketCustomerEvent.AskedForOrder) {
//			giveOrder();
//			return true;
//		}
		return false;
	}


	
//  Actions
//	=====================================================================	
	private void callMarket() {
		state = MarketCustomerState.WaitingForOrder;
		manager.msgIWouldLikeToPlaceADeliveryOrder(this, restaurantCashier, order.orderItems, order.orderId);			
	}
	
//	private void giveOrder() {
//		state = MarketCustomerState.WaitingForOrder;
//		employee.msgHereIsMyDeliveryOrder(this, order);	
//	}

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
	
	public void setManager(MarketManager manager) {
		this.manager = manager;
	}
	
//  Utilities
//	=====================================================================
	

	
	// Classes
}
