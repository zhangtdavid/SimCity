package city.interfaces;

import city.buildings.MarketBuilding;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantChungCashier extends RoleInterface {

	public void msgComputeBill(RestaurantChungWaiterBase w, RestaurantChungCustomer c, String order);
	public abstract void msgHereIsPayment(RestaurantChungCustomer c, int bill);
	public void msgMarketOrderBill (MarketCashier c, int id, int bill);
//	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPaymentRole(); TODO
}