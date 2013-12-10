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
import city.animations.interfaces.MarketAnimatedCustomer;
import city.bases.Role;
import city.buildings.interfaces.Market;
import city.roles.interfaces.MarketCustomer;
import city.roles.interfaces.MarketEmployee;

public class MarketCustomerRole extends Role implements MarketCustomer {
	
//  Data
//	=====================================================================	
	public EventLog log = new EventLog();
	private Semaphore atCounter = new Semaphore(0, true);	
	private Semaphore atCashier = new Semaphore(0, true);
	
	private Market market;
	private MarketEmployee employee;
	
	private MarketOrder order;

	private HashMap<FOOD_ITEMS, Integer> receivedItems = new HashMap<FOOD_ITEMS, Integer>();
	private int bill;

	private MarketCustomerEvent event;
	private MarketCustomerState state;
	
//	Constructor
//	=====================================================================
	public MarketCustomerRole(Market market, MarketOrder o) {
		super(); // TODO
		this.market = market;
        for (FOOD_ITEMS s: o.getOrderItems().keySet()) {
        	receivedItems.put(s, 0); // initialize all values in collectedItems to 0
        }
        order = o;
		state = MarketCustomerState.None;
  }	
	
//	Activity
//	=====================================================================
	@Override
	public void setActive(){
		event = MarketCustomerEvent.ArrivedAtMarket;
		super.setActive();
		super.setActivityBegun();
		stateChanged();
	}
	
//  Messages
//	=====================================================================
	@Override
	public void msgWhatWouldYouLike(MarketEmployee e) {
		log.add(new LoggedEvent("Market Customer received msgWhatWouldYouLike from Market Employee."));
		print("Market Customer received msgWhatWouldYouLike from Market Employee.");
		event = MarketCustomerEvent.AskedForOrder;
		employee = e;
		stateChanged();
	}
	
	@Override
	public void msgHereIsOrderandBill(HashMap<FOOD_ITEMS, Integer> collectedItems, int bill, int id) {
		log.add(new LoggedEvent("Market Customer received msgHereIsOrderandBill from Market Cashier."));
		print("Market Customer received msgHereIsOrderandBill from Market Cashier.");
		event = MarketCustomerEvent.OrderReady;
        for (FOOD_ITEMS item: collectedItems.keySet()) {
            receivedItems.put(item, collectedItems.get(item)); // Create a deep copy of the order map
        }
        this.getPerson().getHome().setFood(this.getPerson(), receivedItems);
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
		super.setInactive();
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
		market.getManager().msgIWouldLikeToPlaceAnOrder(this);
		this.getAnimation(MarketAnimatedCustomer.class).DoStandInWaitingForServiceLine();
	}
	
	private void giveOrder() {
		state = MarketCustomerState.WaitingForOrder;
		this.getAnimation(MarketAnimatedCustomer.class).DoGoToCounter(employee);
		try {
			atCounter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		employee.msgHereIsMyOrder(this, order.getOrderItems(), order.getOrderId());
		this.getAnimation(MarketAnimatedCustomer.class).DoStandInWaitingForItemsLine();
	}
	
	private void pickUpOrderAndPay() {
		state = MarketCustomerState.Paying;
		this.getAnimation(MarketAnimatedCustomer.class).DoGoToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int payment = checkBill();
		if (payment != -1) 
			market.getCashier().msgHereIsPayment(order.getOrderId(), payment);
			this.getPerson().setCash(this.getPerson().getCash() - payment);
	}
	
	private void leaveMarket() {
		state = MarketCustomerState.None;
		this.getAnimation(MarketAnimatedCustomer.class).DoExitMarket();
	}
	
//  Getters
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
	public MarketEmployee getEmployee() {
		return employee;
	}
	
	@Override
	public Map<FOOD_ITEMS, Integer> getReceivedItems() {
		return receivedItems;
	}
	
	@Override
	public int getBill() {
		return bill;
	}
	
	@Override
	public MarketCustomerEvent getMarketCustomerEvent() {
		return event;
	}
	
	@Override
	public MarketCustomerState getMarketCustomerState() {
		return state;
	}
	
	@Override
	public String getStateString() {
		return state.toString();
	}

//  Setters
//	=====================================================================	
	@Override
	public void setMarket(Market market) {
		this.market = market;
	}

//  Utilities
//	=====================================================================
	@Override
	public int checkBill() {
		int tempBill = 0;
        for (FOOD_ITEMS item: order.getOrderItems().keySet()) {
        	tempBill += order.getOrderItems().get(item)*market.getPrices().get(item);
        }

        if (tempBill == bill)
        	return bill;
        
		return -1;
	}
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("MarketCustomer", msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketCustomerRole " + this.getPerson().getName(), msg);
    }
}
