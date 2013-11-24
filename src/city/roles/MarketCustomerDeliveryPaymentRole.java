package city.roles;

import java.util.HashMap;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;
import city.Application.FOOD_ITEMS;
import city.Role;

public class MarketCustomerDeliveryPaymentRole extends Role implements MarketCustomerDeliveryPayment {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private MarketBuilding market;
	private MarketManager manager;
	private MarketCashier cashier;
	
	private MarketOrder order;
	
	double money;
	int bill;
	
	private enum MarketCustomerState
	{None, Paying};
	MarketCustomerState state;
	
	private enum MarketCustomerEvent
	{OrderReady, PaymentReceived};
	MarketCustomerEvent event;
	
//	Constructor
//	---------------------------------------------------------------
	public MarketCustomerDeliveryPaymentRole() {
		super(); // TODO
    }	
	
//  Messages
//	=====================================================================	
	public void msgHereIsBill(MarketCashier c, int bill, int id) {
		log.add(new LoggedEvent("Market CustomerDelivery received msgWhatWouldYouLike from Market Cashier."));
		System.out.println("Market customerDelivery received msgHereIsOrderandBill from Market Cashier.");
        if (order.orderId == id) { // TODO double check this
    		event = MarketCustomerEvent.OrderReady;
    		cashier = c;
            this.bill = bill;
        }
		stateChanged();
	}
		
	public void msgPaymentReceived() {
		log.add(new LoggedEvent("Market CustomerDelivery received msgPaymentReceived from Market Cashier."));
		System.out.println("Market customerDelivery received msgPaymentReceived from Market Cashier.");
	}
	
//  Scheduler
//	=====================================================================	

	@Override
	public boolean runScheduler() {
		if (state == MarketCustomerState.None && event == MarketCustomerEvent.OrderReady) {
			pay();
			return true;
		}
		return false;
	}


	
//  Actions
//	=====================================================================
	private void pay() {
		state = MarketCustomerState.Paying;
		int payment = checkBill();
		cashier.msgHereIsPayment(this, payment);			
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
	
	public void setManager(MarketManager manager) {
		this.manager = manager;
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
	public int checkBill() {
		int tempBill = 0;
        for (FOOD_ITEMS item: order.orderItems.keySet()) {
        	tempBill += order.orderItems.get(item)*market.prices.get(item);
        }

        if (tempBill == bill)
        	return bill;
        
		return -1;
	}	

	
	// Classes
}
