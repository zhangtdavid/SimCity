package city.roles.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Market;

public interface MarketManager extends RoleInterface {
	// Data
	public enum WorkingState {Working, GoingOffShift, NotWorking};
	
	// Constructor
	
	// Messages
	public void msgIWouldLikeToPlaceAnOrder(MarketCustomer c);
	public void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, int id);
	public void msgWhatWouldCustomerDeliveryLike(MarketEmployee e);
	public void msgIAmAvailableToAssist(MarketEmployee e);
	public void msgItemLow();

	// Scheduler
	
	// Actions
	
	// Getters
	public Market getMarket();
	boolean getItemsLow();
	WorkingState getWorkingState();
	
	// Setters
	public void setMarket(Market market);

	// Utilities
	
	// Classes
}
