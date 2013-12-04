package city.interfaces;

import java.util.Map;

import city.RoleInterface;
import city.Application.FOOD_ITEMS;
import city.buildings.MarketBuilding;

public interface MarketEmployee extends RoleInterface {
	// Data
	public enum WorkingState {Working, GoingOffShift, NotWorking};
	public enum MarketEmployeeState {None, AskedForOrder};
	public enum MarketEmployeeEvent {AskedToAssistCustomer, OrderReceived};
	
	// Constructor
	
	// Messages
	public void msgAssistCustomer(MarketCustomer c);
	public void msgAssistCustomerDelivery(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay);
	public void msgHereIsCustomerDeliveryOrder(Map<FOOD_ITEMS, Integer> o, int id);
	public void msgHereIsMyOrder(MarketCustomer c, Map<FOOD_ITEMS, Integer> o, int id);

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
	
	// Utilities
	
	// Classes
	
}
