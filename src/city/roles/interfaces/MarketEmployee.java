package city.roles.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Market;

public interface MarketEmployee extends RoleInterface {
	// Data
	public enum WorkingState {Working, GoingOffShift};
	public enum MarketEmployeeState {None, AskedForOrder};
	public enum MarketEmployeeEvent {AskedToAssistCustomer, OrderReceived};
	
	// Constructor
	
	// Messages
	public void msgAssistCustomer(MarketCustomer c);
	public void msgAssistCustomerDelivery(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay);
	public void msgHereIsCustomerDeliveryOrder(Map<FOOD_ITEMS, Integer> o, int id);
	public void msgHereIsMyOrder(MarketCustomer c, Map<FOOD_ITEMS, Integer> o, int id);
	public void msgAnimationAtPhone();
	public void msgFinishedCollectingItems();
	public void msgAnimationAtCashier();
	public void msgAnimationAtCounter();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public Market getMarket();
	MarketCustomer getMarketCustomer();
	MarketCustomerDelivery getMarketCustomerDelivery();
	MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment();
	Map<FOOD_ITEMS, Integer> getOrder();
	int getOrderId();
	Map<FOOD_ITEMS, Integer> getCollectedItems();
	MarketEmployeeState getMarketEmployeeState();
	MarketEmployeeEvent getMarketEmployeeEvent();
	
	// Setters
	public void setMarket(Market market);
	WorkingState getWorkingState();
	
	// Utilities
	
	// Classes
	
}
