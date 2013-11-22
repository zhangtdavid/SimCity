package city.interfaces;

import restaurant.CashierAgent;
import restaurant.HostAgent;
import restaurant.Menu;
import restaurant.WaiterAgentBase;
import restaurant.gui.CustomerGui;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantChungCustomer {

	public abstract void gotHungry();
	public abstract void msgGetInLinePosition(int positionInLine);
	public abstract void msgNoTablesAvailable();
	public abstract void msgSelfDecidedToLeave();
	public abstract void msgFollowMeToTable(WaiterAgentBase w, Menu menu);
	public abstract void msgAnimationAtSeat();
	public abstract void msgSelfReadyToOrder();
	public abstract void msgWhatWouldYouLike();
	public abstract void msgOutOfItem(String choice, Menu menu);
	public abstract void msgHereIsYourFood();
	public abstract void msgSelfDoneEating();
	public abstract void msgHereIsCheck(double price);
	public abstract void msgAnimationAtCashier();
	public abstract void msgHereIsChange(double change);
	public abstract void msgAnimationFinishedLeaveRestaurant();
	public abstract void msgKickingYouOutAfterPaying(double debt);
	public abstract void setGui(RestaurantChungCustomerAnimation g);
	public abstract void setHost(RestaurantChungHost host);
	public abstract void setCashier(CashierAgent cashier);
	public abstract int getHungerLevel();
	public abstract RestaurantChungCustomerAnimation getGui();
	public abstract String getState();
	public abstract String getOrder();

	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
//	public abstract void HereIsYourTotal(double total);

	/**
	 * @param total change (if any) due to the customer
	 *
	 * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
	 */
//	public abstract void HereIsYourChange(double total);


	/**
	 * @param remaining_cost how much money is owed
	 * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
	 */
//	public abstract void YouOweUs(double remaining_cost);

}