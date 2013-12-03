package city.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.buildings.MarketBuilding;

public interface MarketCustomer extends RoleInterface {

	// Data
	
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
	
	public MarketBuilding getMarket();
	
	// Setters
	
	public void setMarket(MarketBuilding market);
	
	// Utilities
	
	public int checkBill();
	
	// Classes
	
}
