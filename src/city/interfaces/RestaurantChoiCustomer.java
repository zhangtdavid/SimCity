package city.interfaces;

import java.util.ArrayList;

import utilities.RestaurantChoiMenu;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCustomer;

public interface RestaurantChoiCustomer extends RoleInterface{

	public enum AgentState {
		DoingNothing, WaitingInRestaurant, BeingSeated, LookingAtMenu, ReadyToOrder, 
		WaitingForFood, Eating, DoneEating, AskForCheck, Leaving, LeaveNow, GoingToDishes, DoingDishes;
	};
	public enum AgentEvent {
		none, gotHungry, notifiedFull, followWaiter, seated, askedForOrder, gotFood, doneEating, gotCheck, 
		doneLeaving, cantPay, assignedDishes, startDishes, doneWithDishes, gotChange, talkToCashier;
	};


	//real msging
	public abstract void msgNotifyFull(int i);
	public abstract void msgFollowMe(RestaurantChoiWaiter restaurantChoiWaiterRole,
			int getxCoord, int getyCoord);
	public abstract void msgHeresYourSeat(RestaurantChoiMenu restaurantChoiMenu);
	public abstract void msgWhatWouldYouLike();
	public abstract void msgHeresYourNewMenu(RestaurantChoiMenu m);
	public abstract void msgOrderArrived();
	public abstract void msgHeresYourCheck(int checkValue);
	public void msgHeresYourChange(int amt);
	public void msgDoTheDishes(int length);
	//animation msging
	public void msgAnimationFinishedGoToSeat();
	public void msgAnimationFinishedLeaveRestaurant();
	public void msgAnimationFinishedGoToCashier();
	public void msgAnimationFinishedGoToDishes();
	//actions
	public void goToRestaurant();
	public void goToTable();
	void goToDishes();
	void LookAtMenu();
	void giveOrder();
	void EatFood();
	void Checkplz();
	void ImLeaving();
	void Pay();
	void LeaveNow();
	void gotChange();
	void startDishes();
	void leaveAfterDishes();
	void ConsiderLeaving();
	//Getters
	public boolean isHungryNow();
	public void gotHungry(); // from animation?
	public abstract boolean getLocation();
	public abstract FOOD_ITEMS getChoice();	
	public abstract AgentState getState();
	public abstract String getName();
	public abstract RestaurantChoiAnimatedCustomer getGui();
	public abstract int getHungerLevel();
	//Setters
	public abstract void setWaiter(RestaurantChoiWaiter w);
	public abstract void setHost(RestaurantChoiHost h);
	public void setCashier(RestaurantChoiCashier c);
	public void setName(String n);
	public void setHungryNow();
	public void setFullNow();
	public void setHungerLevel(int hungerLevel);
	public void setInactive();
	void setGui(RestaurantChoiAnimatedCustomer anim);
	//Utilities
	FOOD_ITEMS pickRandom(int cash, ArrayList<FOOD_ITEMS> mem, boolean hasHitZero);
	FOOD_ITEMS int2Food(int index);
}
