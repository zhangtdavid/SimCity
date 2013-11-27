package city.interfaces;

public interface RestaurantChungCashier extends RoleInterface {

	public void msgComputeBill(RestaurantChungWaiter w, RestaurantChungCustomer c, String order);
	public abstract void msgHereIsPayment(RestaurantChungCustomer c, int bill);
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment();
}