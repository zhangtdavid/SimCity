package city.interfaces;

import java.util.List;
import java.util.Map;

import city.RoleInterface;
import city.Application.FOOD_ITEMS;
import city.buildings.MarketBuilding;
import city.roles.MarketCashierRole.MyDeliveryPerson;
import city.roles.MarketCashierRole.Transaction;

public interface MarketCashier extends RoleInterface {

	// Data
	public enum WorkingState {Working, GoingOffShift, NotWorking};
	public enum TransactionState {Pending, Calculating, ReceivedPayment, PendingDelivery, Delivering};
	
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
	List<Transaction> getTransactions();
	List<MyDeliveryPerson> getDeliveryPeople();
	
	// Setters
	public void setMarket(Market market);

	
	// Utilities
	
	// Classes
	
}
