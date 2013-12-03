package city.roles;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.buildings.MarketBuilding;
import city.interfaces.Market;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketEmployee;

public class MarketCustomerRole extends Role implements MarketCustomer {
	
//  Data
//	=====================================================================	
	private MarketBuilding market;
	private MarketOrder order;
	private Semaphore atCounter = new Semaphore(0, true);	
	private Semaphore atCashier = new Semaphore(0, true);
	
	// TODO Change these to private and add getters/setters
	public MarketCustomerEvent event;
    public Map<FOOD_ITEMS, Integer> receivedItems = new HashMap<FOOD_ITEMS, Integer>();
	public int loc; // stall number of employee
	public int bill;
	public enum MarketCustomerState {None, WaitingForService, WaitingForOrder, Paying};
	public MarketCustomerState state;
	public enum MarketCustomerEvent {ArrivedAtMarket, ArrivedAtEntrance, AskedForOrder, OrderReady, PaymentReceived};
	public MarketEmployee employee;
	public EventLog log = new EventLog();
	
//	Constructor
//	=====================================================================
	public MarketCustomerRole(MarketOrder o) {
		super(); // TODO
        for (FOOD_ITEMS s: o.orderItems.keySet()) {
        	receivedItems.put(s, 0); // initialize all values in collectedItems to 0
        }
        order = o;
		state = MarketCustomerState.None;
  }	
	
//  Messages
//	=====================================================================
	@Override
	public void msgWhatWouldYouLike(MarketEmployee e, int loc) {
		log.add(new LoggedEvent("Market Customer received msgWhatWouldYouLike from Market Employee."));
		System.out.println("Market Customer received msgWhatWouldYouLike from Market Employee.");
		event = MarketCustomerEvent.AskedForOrder;
		employee = e;
		this.loc = loc;
		stateChanged();
	}
	
	@Override
	public void msgHereIsOrderandBill(Map<FOOD_ITEMS, Integer> collectedItems, int bill, int id) {
		log.add(new LoggedEvent("Market Customer received msgHereIsOrderandBill from Market Cashier."));
		System.out.println("Market Customer received msgHereIsOrderandBill from Market Cashier.");
		event = MarketCustomerEvent.OrderReady;
        for (FOOD_ITEMS item: collectedItems.keySet()) {
            receivedItems.put(item, collectedItems.get(item)); // Create a deep copy of the order map
        }
        this.getPerson().getHome().addFood(receivedItems);
        this.bill = bill;
		stateChanged();
	}
	
	@Override
	public void msgPaymentReceived() {
		log.add(new LoggedEvent("Market Customer received msgPaymentReceived from Market Cashier."));
		System.out.println("Market Customer received msgPaymentReceived from Market Cashier.");
		event = MarketCustomerEvent.PaymentReceived;
		stateChanged();
	}
	
//	Gui
//	---------------------------------------------------------------
	@Override
	public void msgAnimationAtCounter() {
		print("Market Customer received msgAnimationAtCounter");
		atCounter.release();
		stateChanged();
	}
	
	@Override
	public void msgAnimationAtCashier() {
		print("Market Customer received msgAnimationAtCounter");
		atCashier.release();
		stateChanged();
	}
	
	@Override
	public void msgAnimationFinishedLeaveMarket() {
		print("Market Customer received msgAnimationFinishedLeaveMarket");
		super.setActive();
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
		market.manager.msgIWouldLikeToPlaceAnOrder(this);
//		marketCustomerGui.DoStandInWaitingForServiceLine();			
	}
	
	private void giveOrder() {
		state = MarketCustomerState.WaitingForOrder;
//		marketCustomerGui.DoGoToCounter(loc);
//		try {
//			atCounter.acquire();
	//	} catch (InterruptedException e) {
	//		// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
		employee.msgHereIsMyOrder(this, order.orderItems, order.orderId);
//		marketCustomerGui.DoStandInWaitingForServiceLine();		
	}
	
	private void pickUpOrderAndPay() {
		state = MarketCustomerState.Paying;
//		masrketCustomerGui.DoGoToCashier();
//		try {
//		atCashier.acquire();
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
		int payment = checkBill();
		if (payment != -1) 
			market.cashier.msgHereIsPayment(order.orderId, payment);
			this.getPerson().setCash(this.getPerson().getCash() - payment);
	}
	
	private void leaveMarket() {
		state = MarketCustomerState.None;
//		marketCustomerGui.DoExitMarket();
	}
	
//  Getters and Setters
//	=====================================================================
	// Market
	@Override
	public Market getMarket() {
		return market;
	}
	
	@Override
	public MarketOrder getOrder() {
		return order;
	}
	
	@Override
	public void setMarket(MarketBuilding market) {
		this.market = market;
	}
	
	@Override
	public void setActive(){
		event = MarketCustomerEvent.ArrivedAtMarket;
		super.setActive();
		this.setActivityBegun();
		stateChanged();
	}
	
//  Utilities
//	=====================================================================
	@Override
	public int checkBill() {
		int tempBill = 0;
        for (FOOD_ITEMS item: order.orderItems.keySet()) {
        	tempBill += order.orderItems.get(item)*market.prices.get(item);
        }

        if (tempBill == bill)
        	return bill;
        
		return -1;
	}
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketCustomerRole " + this.getPerson().getName(), msg);
    }
}
