package city.interfaces;

import java.util.Map;

import city.RoleInterface;
import city.Application.FOOD_ITEMS;
import city.buildings.MarketBuilding;

public interface MarketCashier extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public void msgNewDeliveryPerson(MarketDeliveryPerson d);
	public void msgRemoveDeliveryPerson(MarketDeliveryPerson d);
	public void msgComputeBill(MarketEmployee e, MarketCustomer c, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id);
	public void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id);
	public void msgHereIsPayment(int id, int money);
	public void msgDeliveringItems(MarketDeliveryPerson d);
	public void msgFinishedDeliveringItems(MarketDeliveryPerson d, int orderId);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public Market getMarket();
	
	// Setters
	
	public void setMarket(MarketBuilding market);
	
	// Utilities
	
	// Classes
	
}
