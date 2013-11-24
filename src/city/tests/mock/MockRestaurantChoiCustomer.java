package city.tests.mock;

import java.util.ArrayList;

import utilities.LoggedEvent;
import city.MockRole;
import city.animations.interfaces.RestaurantChoiAnimatedCustomer;
import city.interfaces.RestaurantChoiCashier;
import city.interfaces.RestaurantChoiCustomer;
import city.interfaces.RestaurantChoiHost;
import city.interfaces.RestaurantChoiWaiter;
import city.roles.RestaurantChoiMenu;

public class MockRestaurantChoiCustomer extends MockRole implements RestaurantChoiCustomer  {

	public int choice;
	public String name;
	
	public MockRestaurantChoiCustomer(String name){
		this.name = name;
	}
	@Override
	public void msgNotifyFull(int i) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgNotifyFull."));
	}

	@Override
	public void msgFollowMe(RestaurantChoiWaiter restaurantChoiWaiterRole,
			int getxCoord, int getyCoord) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgFollowMe."));
	}

	@Override
	public void msgHeresYourSeat(RestaurantChoiMenu restaurantChoiMenu) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHeresYourSeat."));
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgWhatWouldYouLike."));
	}

	@Override
	public void msgHeresYourNewMenu(RestaurantChoiMenu m) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHeresYourNewMenu."));
	}

	@Override
	public void msgOrderArrived() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgOrderArrived"));
	}

	@Override
	public void msgHeresYourCheck(double checkValue) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHeresYourCheck, which says: " + checkValue));
	}

	@Override
	public void msgHeresYourChange(double amt) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHeresYourChange from cashier."));
	}

	@Override
	public void msgDoTheDishes(int length) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgDoTheDishes from the cashier."));
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgAnimationFinishedGoToSeat."));
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgAnimationFinishedLeaveRestaurant.")); 

	}

	@Override
	public void msgAnimationFinishedGoToCashier() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgAnimationFinishedGoToCashier")); 

	}

	@Override
	public void msgAnimationFinishedGoToDishes() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgAnimationFinishedGoToDishes.")); 

	}

	@Override
	public void goToRestaurant() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgGetHungry (in goToRestaurant).")); 

	}

	@Override
	public void goToTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToDishes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void LookAtMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveOrder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int pickRandom(double cash, ArrayList<Integer> mem,
			boolean hasHitZero) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void EatFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checkplz() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ImLeaving() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Pay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void LeaveNow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gotChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startDishes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveAfterDishes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ConsiderLeaving() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isHungryNow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getLocation() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getChoice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AgentState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public RestaurantChoiAnimatedCustomer getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHungerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWaiter(RestaurantChoiWaiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(RestaurantChoiHost h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(RestaurantChoiCashier c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setName(String n) {
		name = n;		
	}

	@Override
	public void setHungryNow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFullNow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHungerLevel(int hungerLevel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(RestaurantChoiAnimatedCustomer anim) {
		// TODO Auto-generated method stub
		
	}
	
}
