package city.interfaces;

import utilities.MarketTransaction;
import city.RoleInterface;
import city.buildings.MarketBuilding;

public interface MarketCustomerDeliveryPayment extends RoleInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	public void msgHereIsBill(int bill, int id);
	public void msgPaymentReceived(int id);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public MarketBuilding getMarket();
	
	// Setters
	
	public void setMarket(MarketBuilding market);
	
	// Utilities

	public int checkBill(MarketTransaction mt);
	public void removeMarketTransactionFromList(MarketTransaction transaction);
	
	// Classes
	
}
