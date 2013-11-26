package city.interfaces;

import city.buildings.MarketBuilding;
import city.roles.MarketCustomerDeliveryPaymentRole;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantChungCashier extends RoleInterface {

	public void msgComputeBill(RestaurantChungWaiter w, RestaurantChungCustomer c, String order);
	public abstract void msgHereIsPayment(RestaurantChungCustomer c, int bill);
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment();
}