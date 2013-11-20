package city.roles;

import java.util.HashMap;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;
import city.Role;

public class MarketCustomerDeliveryRole extends Role implements MarketCustomerDelivery {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private MarketBuilding market;
	private MarketManager manager;
	private MarketCashier cashier;
	private MarketEmployee employee;
	
	private Map<String, Integer> order = new HashMap<String, Integer>();
    private Map<String, Integer> receivedItems = new HashMap<String, Integer>();
		
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
	public void msgWhatWouldYouLike(MarketEmployee e) {
		log.add(new LoggedEvent("Market CustomerDelivery received msgWhatWouldYouLike from Market Employee."));
		System.out.println("Market CustomerDelivery received msgWhatWouldYouLike from Market Employee.");
		event = MarketCustomerEvent.AskedForOrder;
		employee = e;
		stateChanged();
	}
	
	public void msgHereIsBill(double bill) {
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
	
	public void msgHereIsOrder(Map<String, Integer> collectedItems) {
		log.add(new LoggedEvent("Market CustomerDelivery received msgHereIsOrder from Market DeliveryPerson."));
		System.out.println("Market customerDelivery received msgHereIsOrder from Market DeliveryPerson.");
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
	

	
	// Classes
}
