package city.roles.interfaces;

import java.util.List;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Market;
import city.roles.MarketManagerRole.MyMarketCustomer;
import city.roles.MarketManagerRole.MyMarketEmployee;

public interface MarketManager extends RoleInterface {
	// Data
	public enum WorkingState {Working, GoingOffShift, NotWorking};
	
	// Constructor
	
	// Messages
	public void msgNewEmployee(MarketEmployee e);
	public void msgRemoveEmployee(MarketEmployee e);
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
	List<MyMarketEmployee> getEmployees();
	List<MyMarketCustomer> getCustomers();
	WorkingState getWorkingState();
	
	// Setters
	public void setMarket(Market market);

	// Utilities
	MyMarketEmployee findEmployee(MarketEmployee me);
	MyMarketCustomer findCustomerDelivery(MarketCustomerDelivery cd);
	
	// Classes
}
