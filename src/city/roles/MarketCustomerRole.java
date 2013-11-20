package city.roles;

import java.util.HashMap;
import java.util.Map;

import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;
import city.Role;

public class MarketCustomerRole extends Role implements MarketCustomer {

//  Data
//	=====================================================================	
    private Map<String, Integer> order = new HashMap<String, Integer>();
    private Map<String, Integer> receivedItems = new HashMap<String, Integer>();

	private MarketManager manager;
	private MarketCashier cashier;
	private MarketEmployee employee;
	
	int loc;
	
	double money;
	double bill;
	
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
	public MarketCustomerRole() {
		super(); // TODO
        for (String s: order.keySet()) {
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
		System.out.println("Market customer received msgWhatWouldYouLike");
		event = MarketCustomerEvent.AskedForOrder;
		employee = e;
		this.loc = loc;
		stateChanged();
	}
	
	public void msgHereIsOrderandBill(Map<String, Integer> collectedItems, double bill) {
		System.out.println("Market customer received msgHereIsOrderandBill");
		event = MarketCustomerEvent.OrderReady;
        for (String item: collectedItems.keySet()) {
            receivedItems.put(item, collectedItems.get(item)); // Create a deep copy of the order map
        }
        this.bill = bill;
		stateChanged();
	}
	
	public void msgPaymentReceived() {
		System.out.println("Market customer received msgPaymentReceived");
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
		employee.msgHereIsMyOrder(this, order);
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
//		double payment = checkBill(); TODO
//		cashier.msgHereIsPayment(this, payment);			
	}
	
	private void leaveMarket() {
		state = MarketCustomerState.None;
//		marketCustomerGui.DoLeaveMarket();
//		return back to normal person role

	}
	
	// Setters
	
//  Utilities
//	=====================================================================	
	// Getters
	public MarketManager getManager() {
		return manager;
	}
	
	public MarketCashier getCashier() {
		return cashier;
	}
	
	// Setters
	public MarketManager setManager() {
		return manager;
	}
	
	public void setCashier(MarketCashier cashier) {
		this.cashier = cashier;
	}
	// Classes
}
