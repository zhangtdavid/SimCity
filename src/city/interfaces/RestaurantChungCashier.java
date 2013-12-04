package city.interfaces;

import utilities.MarketOrder;
import utilities.MarketTransaction;
import city.RoleInterface;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChungBuilding;
import city.roles.RestaurantChungCashierRole.Transaction;

public interface RestaurantChungCashier extends RoleInterface {
	public enum WorkingState {Working, GoingOffShift, NotWorking};
	public enum TransactionState {None, Pending, Calculating, ReceivedPayment, InsufficientPayment, NotifiedHost, Done};

	// Messages
	public void msgComputeBill(RestaurantChungWaiter w, RestaurantChungCustomer c, String order);
	public void msgHereIsPayment(RestaurantChungCustomer c, int bill);
	public void msgAddMarketOrder(MarketBuilding selectedMarket, MarketOrder o);
	
	// Getters
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment();
	
	// Setters
	void setRestaurant(RestaurantChungBuilding restaurant);
	void setHost(RestaurantChungHost host);
	void setMarketCustomerDeliveryPaymentPerson();
	void setBankCustomerPerson();
	
	// Utilities
	int checkBill(MarketTransaction t);
	Transaction findTransaction(RestaurantChungCustomer c);
	MarketTransaction findMarketTransaction(int id);
	void removeOrderFromList(Transaction transaction);
	
}