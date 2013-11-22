package city.interfaces;

import java.util.Map;

import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.MarketAgent.Order;

/**
 * A Market interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantChungMarket {

	public abstract void msgHereIsAnOrder(CookAgent c, int id, boolean rush, Map<String, Integer> cookOrder);
	public abstract void msgSelfDoneProcessing(Order o);
	public abstract void msgSelfDoneShipping(Order o);
	public abstract void msgHereIsPayment(int id, double payment);
	public abstract void setCashier(CashierAgent c);
	public abstract Order findOrderFromID(int id);
	public abstract void removeOrderFromList(Order order);
	
}