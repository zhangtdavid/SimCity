package city.interfaces;

import utilities.RestaurantChungMenu;
import city.animations.RestaurantChungCustomerAnimation;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantChungCustomer extends RoleInterface {

	public abstract void gotHungry();
	public abstract void msgGetInLinePosition(int positionInLine);
	public abstract void msgNoTablesAvailable();
	public abstract void msgSelfDecidedToLeave();
	public abstract void msgFollowMeToTable(RestaurantChungWaiter w, RestaurantChungMenu menu);
	public abstract void msgAnimationAtSeat();
	public abstract void msgSelfReadyToOrder();
	public abstract void msgWhatWouldYouLike();
	public abstract void msgOutOfItem(String choice, RestaurantChungMenu menu);
	public abstract void msgHereIsYourFood();
	public abstract void msgSelfDoneEating();
	public abstract void msgHereIsCheck(int price);
	public abstract void msgAnimationAtCashier();
	public abstract void msgHereIsChange(int change);
	public abstract void msgAnimationFinishedLeaveRestaurant();
	public abstract void msgKickingYouOutAfterPaying(int debt);
	public abstract int getHungerLevel();
	public abstract String getState();
	public abstract String getOrder();

}