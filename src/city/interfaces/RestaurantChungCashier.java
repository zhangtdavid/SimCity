package city.interfaces;

import utilities.MarketOrder;
import city.buildings.MarketBuilding;

public interface RestaurantChungCashier extends RoleInterface {

	// Messages

	public void msgComputeBill(RestaurantChungWaiter w, RestaurantChungCustomer c, String order);
	public void msgHereIsPayment(RestaurantChungCustomer c, int bill);
	public void msgAddMarketOrder(MarketBuilding selectedMarket, MarketOrder o);
	
	// Getters
	
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment();
	
}