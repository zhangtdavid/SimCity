package city.roles;

import java.util.HashMap;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;
import city.Application.FOOD_ITEMS;
import city.Role;

public class MarketCustomerRole extends Role implements MarketCustomer {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private MarketBuilding market;
	private MarketManager manager;
	private MarketCashier cashier;
	private MarketEmployee employee;
	
	private MarketOrder order;
    private Map<FOOD_ITEMS, Integer> receivedItems = new HashMap<FOOD_ITEMS, Integer>();
	
	int loc;
	
	int money;
	int bill;
	
	private enum MarketCustomerState
	{None, WaitingForService, WaitingForOrder, Paying};
	MarketCustomerState state;
	
	private enum MarketCustomerEvent
	{ArrivedAtMarket, AskedForOrder, OrderReady, PaymentReceived};
	MarketCustomerEvent event;

//	Gui
//	---------------------------------------------------------------
//	private MarketCustomerGui marketCustomerGui;
//	private Semaphore atServiceLine = new Semaphore(0, true);
//	private Semaphore atCounter = new Semaphore(0, true);
//	private Semaphore atOrderLine = new Semaphore(0, true);
//	private Semaphore atCashier = new Semaphore(0, true);
	
//	Constructor
//	---------------------------------------------------------------
	public MarketCustomerRole(MarketOrder o) {
		super(); // TODO
        for (FOOD_ITEMS s: order.orderItems.keySet()) {
        	receivedItems.put(s, 0); // initialize all values in collectedItems to 0
        }
    }	
	
//  Messages
//	=====================================================================	
	public void msgAnimationArrivedAtMarket() {
		System.out.println("Market customer received msgAnimationArrivedAtMarket");
		event = MarketCustomerEvent.ArrivedAtMarket;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike(MarketEmployee e, int loc) {
		log.add(new LoggedEvent("Market Customer received msgWhatWouldYouLike from Market Employee."));
		System.out.println("Market Customer received msgWhatWouldYouLike from Market Employee.");
		event = MarketCustomerEvent.AskedForOrder;
		employee = e;
		this.loc = loc;
		stateChanged();
	}
	
	public void msgHereIsOrderandBill(Map<FOOD_ITEMS, Integer> collectedItems, int bill, int id) {
		log.add(new LoggedEvent("Market Customer received msgHereIsOrderandBill from Market Cashier."));
		System.out.println("Market Customer received msgHereIsOrderandBill from Market Cashier.");
		event = MarketCustomerEvent.OrderReady;
        for (FOOD_ITEMS item: collectedItems.keySet()) {
            receivedItems.put(item, collectedItems.get(item)); // Create a deep copy of the order map
        }
        this.bill = bill;
		stateChanged();
	}
	
	public void msgPaymentReceived() {
		log.add(new LoggedEvent("Market Customer received msgPaymentReceived from Market Cashier."));
		System.out.println("Market Customer received msgPaymentReceived from Market Cashier.");
		event = MarketCustomerEvent.PaymentReceived;
		stateChanged();
	}
	
//  Scheduler
//	=====================================================================	

	@Override
	public boolean runScheduler() {
		if (state == MarketCustomerState.None && event == MarketCustomerEvent.ArrivedAtMarket) {
			requestService();
			return true;
		}
		if (state == MarketCustomerState.WaitingForService && event == MarketCustomerEvent.AskedForOrder) {
			giveOrder();
			return true;
		}
		if (state == MarketCustomerState.WaitingForOrder && event == MarketCustomerEvent.OrderReady) {
			pickUpOrderAndPay();
			return true;
		}
		if (state == MarketCustomerState.Paying && event == MarketCustomerEvent.PaymentReceived) {
			leaveMarket();
			return true;
		}
		return false;
	}


	
//  Actions
//	=====================================================================	
	private void requestService() {
		state = MarketCustomerState.WaitingForService;
		manager.msgIWouldLikeToPlaceAnOrder(this);
//		marketCustomerGui.DoStandInWaitingForServiceLine();
//		try {
//		atServiceLine.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			
	}
	
	private void giveOrder() {
		state = MarketCustomerState.WaitingForOrder;
//		marketCustomerGui.DoGoToCounter(loc);
//		try {
//		atCounter.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		employee.msgHereIsMyOrder(this, order.orderItems, order.orderId);
//		marketCustomerGui.DoStandInWaitingForOrderLine();
//		try {
//		atOrderLine.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}			
	}
	
	private void pickUpOrderAndPay() {
		state = MarketCustomerState.Paying;
//		marketCustomerGui.DoGoToCashier();
//		try {
//		atCashier.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		int payment = checkBill();
		cashier.msgHereIsPayment(this, payment);			
	}
	
	private void leaveMarket() {
		state = MarketCustomerState.None;
//		marketCustomerGui.DoLeaveMarket();
//		return back to normal person role

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
