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
	
	private MarketOrder order;
		
	int money;
	int bill;
	
	private enum MarketCustomerState
	{None, WaitingForOrder, Paying, WaitingForDelivery};
	MarketCustomerState state;
	
	private enum MarketCustomerEvent
	{NeedOrderFromMarket, OrderReady};
	MarketCustomerEvent event;
	
//	Constructor
//	---------------------------------------------------------------
	public MarketCustomerDeliveryRole() {
		super(); // TODO
    }	
	
//  Messages
//	=====================================================================
	public void msgHereIsOrder(MarketOrder o) {
		log.add(new LoggedEvent("Market CustomerDelivery received msgHereIsOrder from Market DeliveryPerson."));
		System.out.println("Market customerDelivery received msgHereIsOrder from Market DeliveryPerson.");
        event = MarketCustomerEvent.OrderReady;
        for (FOOD_ITEMS item: order.collectedItems.keySet()) {
            order.receivedItems.put(item, order.collectedItems.get(item));
        }
		state = MarketCustomerState.None;
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
//		manager.msgIWouldLikeToPlaceADeliveryOrder(this, order);			
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
