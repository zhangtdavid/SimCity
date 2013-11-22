package city.interfaces;

import restaurant.Menu;
import restaurant.gui.CustomerGui;
import restaurant.gui.WaiterGui;

/**
 * A Waiter interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantChungWaiterMessageCook {

	public abstract void msgAnimationAskedForBreak();
	public abstract void msgApprovedForBreak();
	public abstract void msgRejectedForBreak();
	public abstract void msgAnimationBreakOver();
	public abstract void msgSitAtTable(RestaurantChungCustomer c, int table);
	public abstract void msgReadyToOrder(RestaurantChungCustomer c);
	public abstract void msgHereIsMyOrder(RestaurantChungCustomer c, String choice);
	public abstract void msgOutOfItem(String choice, int table);
	public abstract void msgOrderIsReady(String choice, int table);
	public abstract void msgGetCheck(RestaurantChungCustomer c);
	public abstract void msgLeaving(RestaurantChungCustomer c);
	public abstract void msgHereIsBill(RestaurantChungCustomer c, double price);
	public abstract void msgAnimationAtEntrance();
	public abstract void msgAnimationAtTable();
	public abstract void msgAnimationAtCook();
	public abstract void msgAnimationAtCashier();
	public abstract void setGui(RestaurantChungWaiterAnimation g);
	public abstract RestaurantChungWaiterAnimation getGui();

}