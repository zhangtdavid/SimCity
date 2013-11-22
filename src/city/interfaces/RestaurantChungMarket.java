package city.interfaces;

import java.util.Map;

import city.roles.RestaurantChungMarketRole.Order;

/**
 * A Market interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantChungMarket extends RoleInterface {

	public abstract void msgHereIsAnOrder(RestaurantChungCook c, int id, boolean rush, Map<String, Integer> cookOrder);
	public abstract void msgSelfDoneProcessing(Order o);
	public abstract void msgSelfDoneShipping(Order o);
	public abstract void msgHereIsPayment(int id, double payment);
	public abstract void setCashier(RestaurantChungCashier c);
	public abstract Order findOrderFromID(int id);
	public abstract void removeOrderFromList(Order order);
	
}