package city.roles;

import java.util.HashMap;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;
import city.Role;

public class MarketCustomerDeliveryPaymentRole extends Role implements MarketCustomerDeliveryPayment {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private MarketBuilding market;
	private MarketManager manager;
	private MarketCashier cashier;
	
	private Map<String, Integer> order = new HashMap<String, Integer>();
    private Map<String, Integer> receivedItems = new HashMap<String, Integer>();
		
	double money;
	int bill;
	
	private enum MarketCustomerState
	{None, WaitingForService, WaitingForOrder, Paying, WaitingForDelivery};
	MarketCustomerState state;
	
	private enum MarketCustomerEvent
	{NeedOrderFromMarket, AskedForOrder, OrderReady, PaymentReceived};
	MarketCustomerEvent event;
	
//	Constructor
//	---------------------------------------------------------------
	public MarketCustomerDeliveryPaymentRole() {
		super(); // TODO
        for (String s: order.keySet()) {
        	receivedItems.put(s, 0); // initialize all values in collectedItems to 0
        }
    }	
	
//  Messages
//	=====================================================================	
	public void msgHereIsBill(int bill) {
		log.add(new LoggedEvent("Market CustomerDelivery received msgWhatWouldYouLike from Market Cashier."));
		System.out.println("Market customerDelivery received msgHereIsOrderandBill from Market Cashier.");
		event = MarketCustomerEvent.OrderReady;
        this.bill = bill;
		stateChanged();
	}
		
	public void msgPaymentReceived() {
		log.add(new LoggedEvent("Market CustomerDelivery received msgPaymentReceived from Market Cashier."));
		System.out.println("Market customerDelivery received msgPaymentReceived from Market Cashier.");
		state = MarketCustomerState.WaitingForDelivery;
	}
	
//  Scheduler
//	=====================================================================	

	@Override
	public boolean runScheduler() {
		if (state == MarketCustomerState.WaitingForOrder && event == MarketCustomerEvent.OrderReady) {
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
        for (String item: order.keySet()) {
        	tempBill += order.get(item)*market.prices.get(item);
        }

        if (tempBill == bill)
        	return bill;
        
		return -1;
	}	

	
	// Classes
}
