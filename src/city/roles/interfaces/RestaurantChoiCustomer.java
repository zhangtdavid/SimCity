package city.roles.interfaces;

import utilities.RestaurantChoiMenu;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCustomer;
import city.bases.interfaces.RoleInterface;

public interface RestaurantChoiCustomer extends RoleInterface{
	
	// Data

	public static enum STATE { DoingNothing, WaitingInRestaurant, BeingSeated, LookingAtMenu, ReadyToOrder, WaitingForFood, Eating, DoneEating, AskForCheck, Leaving, LeaveNow, GoingToDishes, DoingDishes };
	public static enum EVENT { none, gotHungry, notifiedFull, followWaiter, seated, askedForOrder, gotFood, doneEating, gotCheck, doneLeaving, cantPay, assignedDishes, startDishes, doneWithDishes, gotChange, talkToCashier };

	// Messages
	
	public void msgNotifyFull(int i);
	public void msgFollowMe(RestaurantChoiWaiter restaurantChoiWaiterRole, int getxCoord, int getyCoord);
	public void msgHeresYourSeat(RestaurantChoiMenu restaurantChoiMenu);
	public void msgWhatWouldYouLike();
	public void msgHeresYourNewMenu(RestaurantChoiMenu m);
	public void msgOrderArrived();
	public void msgHeresYourCheck(int checkValue);
	public void msgHeresYourChange(int amt);
	public void msgDoTheDishes(int length);
	public void msgAnimationFinishedGoToSeat();
	public void msgAnimationFinishedLeaveRestaurant();
	public void msgAnimationFinishedGoToCashier();
	public void msgAnimationFinishedGoToDishes();
	
	// Actions

	// Getters
	
	public boolean isHungryNow();
	public void gotHungry(); // from animation?
	public boolean getLocation();
	public FOOD_ITEMS getChoice();	
	public STATE getState();
	public RestaurantChoiAnimatedCustomer getGui();
	public int getHungerLevel();
	
	// Setters
	
	public void setWaiter(RestaurantChoiWaiter w);
	public void setHost(RestaurantChoiHost h);
	public void setCashier(RestaurantChoiCashier c);
	public void setHungryNow();
	public void setFullNow();
	public void setHungerLevel(int hungerLevel);
	public void setInactive();
	public void setGui(RestaurantChoiAnimatedCustomer anim);
	
	// Utilities

}
