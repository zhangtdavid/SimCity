package city.roles.interfaces;

import java.util.List;

import utilities.MarketOrder;
import utilities.MarketTransaction;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Market;
import city.buildings.interfaces.RestaurantChung;
import city.roles.RestaurantChungCashierRole.Transaction;

public interface RestaurantChungCashier extends RoleInterface {
	public enum WorkingState {Working, GoingOffShift, NotWorking};
	public enum TransactionState {None, Pending, Calculating, ReceivedPayment, InsufficientPayment, NotifiedHost, Done};

	// Messages
	public void msgComputeBill(RestaurantChungWaiter w, RestaurantChungCustomer c, String order);
	public void msgHereIsPayment(RestaurantChungCustomer c, int bill);
	public void msgAddMarketOrder(Market selectedMarket, MarketOrder o);
	
	// Getters
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment();
	
	// Setters
	void setRestaurant(RestaurantChung restaurant);
	void setMarketCustomerDeliveryPaymentPerson();
	void setBankCustomerPerson();
	
	// Utilities
	int checkBill(MarketTransaction t);
	Transaction findTransaction(RestaurantChungCustomer c);
	MarketTransaction findMarketTransaction(int id);
	void removeOrderFromList(Transaction transaction);
	List<Transaction> getTransactions();
	List<MarketTransaction> getMarketTransactions();
}