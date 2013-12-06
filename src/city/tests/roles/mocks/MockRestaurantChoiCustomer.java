package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import utilities.LoggedEvent;
import utilities.RestaurantChoiMenu;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCustomer;
import city.roles.interfaces.RestaurantChoiCashier;
import city.roles.interfaces.RestaurantChoiCustomer;
import city.roles.interfaces.RestaurantChoiHost;
import city.roles.interfaces.RestaurantChoiWaiter;
import city.tests.bases.mocks.MockRole;

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
	public void msgHeresYourCheck(int checkValue) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHeresYourCheck, which says: " + checkValue));
	}

	@Override
	public void msgHeresYourChange(int amt) {
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
	public FOOD_ITEMS getChoice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public STATE getState() {
		// TODO Auto-generated method stub
		return null;
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
	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}

}
