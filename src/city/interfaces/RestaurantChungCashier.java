package city.interfaces;

import city.roles.RestaurantChungCustomerRole;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantChungCashier extends RoleInterface {

	public void msgComputeBill(RestaurantChungWaiterBase w, RestaurantChungCustomer c, String order);
	public abstract void msgHereIsPayment(RestaurantChungCustomer c, double bill);
	public void msgMarketOrderBill (RestaurantChungMarket m, int id, double bill);
}