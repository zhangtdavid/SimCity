package city.interfaces;

import java.util.Map;

import utilities.MarketOrder;
import city.Application.FOOD_ITEMS;
import city.RoleInterface;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomer.MarketCustomerEvent;
import city.interfaces.MarketCustomer.MarketCustomerState;

public interface MarketCustomer extends RoleInterface {
	// Data
	public enum MarketCustomerState {None, WaitingForService, WaitingForOrder, Paying};
	public enum MarketCustomerEvent {ArrivedAtMarket, ArrivedAtEntrance, AskedForOrder, OrderReady, PaymentReceived};
	
	// Constructor
	
	// Messages
	public void msgWhatWouldYouLike(MarketEmployee e, int loc);
	public void msgHereIsOrderandBill(Map<FOOD_ITEMS, Integer> collectedItems, int bill, int id);
	public void msgPaymentReceived();
	public void msgAnimationFinishedLeaveMarket();
	public void msgAnimationAtCounter();
	public void msgAnimationAtCashier();

	// Scheduler
	
	// Actions
	
	// Getters
	
	public Market getMarket();
	public MarketOrder getOrder();
	MarketEmployee getEmployee();
	Map<FOOD_ITEMS, Integer> getReceivedItems();
	int getLoc();
	int getBill();
	MarketCustomerEvent getMarketCustomerEvent();
	MarketCustomerState getMarketCustomerState();
	
	// Setters
	public void setMarket(MarketBuilding market);
	
	// Utilities
	public int checkBill();
	
	// Classes
	
}
