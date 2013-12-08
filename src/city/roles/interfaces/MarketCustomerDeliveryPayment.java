package city.roles.interfaces;

import utilities.MarketTransaction;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Market;

public interface MarketCustomerDeliveryPayment extends RoleInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	public void msgHereIsBill(int bill, int id);
	public void msgPaymentReceived(int id);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public Market getMarket();
	
	// Setters
	
	public void setMarket(Market market);
	
	// Utilities

	public int checkBill(MarketTransaction mt);
	public MarketTransaction findMarketTransaction(int id);
	
	// Classes
	
}
