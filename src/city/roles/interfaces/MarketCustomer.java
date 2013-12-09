package city.roles.interfaces;

import java.util.HashMap;
import java.util.Map;

import utilities.MarketOrder;
import city.Application.FOOD_ITEMS;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Market;

public interface MarketCustomer extends RoleInterface {
	
	// Data
	public enum MarketCustomerState {None, WaitingForService, WaitingForOrder, Paying};
	public enum MarketCustomerEvent {ArrivedAtMarket, ArrivedAtEntrance, AskedForOrder, OrderReady, PaymentReceived};
	
	// Constructor
	
	// Messages
	public void msgWhatWouldYouLike(MarketEmployee e);
	public void msgHereIsOrderandBill(HashMap<FOOD_ITEMS, Integer> collectedItems, int bill, int id);
	public void msgPaymentReceived();
	public void msgAnimationFinishedLeaveMarket();
	public void msgAnimationAtCounter();
	public void msgAnimationAtCashier();

	// Scheduler
	
	// Actions
	
	// Getters
	public Market getMarket();
	public MarketOrder getOrder();
	public MarketEmployee getEmployee();
	public Map<FOOD_ITEMS, Integer> getReceivedItems();
	public int getBill();
	public MarketCustomerEvent getMarketCustomerEvent();
	public MarketCustomerState getMarketCustomerState();
	
	// Setters
	public void setMarket(Market market);
	
	// Utilities
	public int checkBill();
	
	// Classes
	
}
