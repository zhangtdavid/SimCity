package city.roles.interfaces;

import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangTable;
import city.animations.interfaces.RestaurantZhangAnimatedCustomer;
import city.bases.interfaces.RoleInterface;

public interface RestaurantZhangCustomer extends RoleInterface {
	
	// Data
	public static enum AGENTSTATE {DoingNothing, AtEntrance, GoingToWaitingPosition, WaitingInRestaurant, ChoosingToLeave, DecidedToWait, BeingSeated, Deciding, Ordering, Ordered, Eating, WaitingForCheck, PayingForCheck, Leaving};
	public enum AGENTEVENT {none, gotWaitingPosition, atWaitingPosition, gotHungry, restaurantFull, restaurantClosed, followWaiter, Seated, Decided, TellWaiterOrder, OrderAgain, GotFood, gotCheck, gotChange, gotTab, DoneEating};
	public static final int DECIDINGTIME = 3000;
	public static final int EATINGTIME = 6000;
	public static final int CHANCETOLEAVE = 2;
	
	// Messages
	
	public void msgHereIsCustCheck(RestaurantZhangCheck c);
	public void msgHereIsChange(int change);
	public void msgPayLater(int tab);
	public void msgRestaurantFull();
	public void msgHereIsYourWaitingPosition(int pos);
	public void msgFollowMe(RestaurantZhangWaiter w, RestaurantZhangMenu waiterMenu, RestaurantZhangTable table);
	public void msgWhatWouldYouLike();
	public void msgOrderAgain();
	public void msgHereIsYourFood(String choice);
	public void msgAnimationFinishedEnterRestaurant();
	public void msgAnimationFinishedGoToSeat();
	public void msgAnimationFinishedLeaveRestaurant();
	public void msgGotHungry();
	public void msgRestaurantClosed();
	
	// Getters
	
	public RestaurantZhangTable getTable();
	public RestaurantZhangHost getHost();
	public RestaurantZhangWaiter getWaiter();
	public int getWaitingPosition();
	public RestaurantZhangMenu getCustomerMenu();
	public String getChoice();
	public RestaurantZhangCashier getCashier();
	public RestaurantZhangCheck getCheck();
	public int getMoney();
	public int getTab();
	public AGENTSTATE getState();
	public AGENTEVENT getEvent();
	
	// Setters
	
	public void setWaitingPosition(int newPos);
	public void setAnimation(RestaurantZhangAnimatedCustomer g);
	public void setCashier(RestaurantZhangCashier c);
	public void setHost(RestaurantZhangHost host);
	public void setMoney(int newMoney);
	
}