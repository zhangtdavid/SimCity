package city.buildings;

import java.util.ArrayList;
import java.util.List;

import utilities.RestaurantChungRevolvingStand;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.animations.RestaurantChungCashierAnimation;
import city.animations.RestaurantChungCookAnimation;
import city.animations.RestaurantChungCustomerAnimation;
import city.animations.RestaurantChungWaiterAnimation;
import city.gui.RestaurantChungPanel;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCook;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungHost;
import city.interfaces.RestaurantChungWaiter;
import city.roles.RestaurantChungCashierRole;
import city.roles.RestaurantChungCookRole;
import city.roles.RestaurantChungCustomerRole;
import city.roles.RestaurantChungHostRole;
import city.roles.RestaurantChungWaiterMessageCookRole;
import city.roles.RestaurantChungWaiterRevolvingStandRole;

public class RestaurantChungBuilding extends RestaurantBaseBuilding {
	
	// Data
	public RestaurantChungPanel panel;
	public RestaurantChungHost host;
	public RestaurantChungCashier cashier;
	public RestaurantChungCook cook;
	public List<RestaurantChungWaiter> waiters = new ArrayList<RestaurantChungWaiter>();
	public List<RestaurantChungCustomer> customers = new ArrayList<RestaurantChungCustomer>();
	
	private RestaurantChungRevolvingStand orderStand;
	
	private static final int WORKER_SALARY = 500;
	
	// Constructor
	public RestaurantChungBuilding(String name, RestaurantChungPanel panel) {
		super(name);
		this.setCustomerRoleName("city.roles.RestaurantChungCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantChungCustomerAnimation");
		this.panel = panel;
		orderStand = new RestaurantChungRevolvingStand();
		
        // Add items and their cooking times to a map
		super.addFood(FOOD_ITEMS.chicken, new Food("chicken", 10, 6, 5, 10, 16));
		super.addFood(FOOD_ITEMS.pizza, new Food("pizza", 15, 6, 5, 10, 12));
		super.addFood(FOOD_ITEMS.salad, new Food("salad", 5, 6, 5, 10, 6));
		super.addFood(FOOD_ITEMS.steak, new Food("steak", 20, 6, 5, 10, 10));
        
        setCash(500);
	}
	
	// Getters
	
	public static int getWorkerSalary() {
		return WORKER_SALARY;
	}

	// Utilities
	public void addRole(Role r) {
		if(r instanceof RestaurantChungCustomerRole) {
			RestaurantChungCustomerRole c = (RestaurantChungCustomerRole)r;
			if(!super.roleExists(c)) {
				RestaurantChungCustomerAnimation anim = new RestaurantChungCustomerAnimation(c); 
				c.setAnimation(anim);
				c.setRestaurant(this);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				customers.add(c);
				super.addRole(c, anim);
				host.msgIWantToEat(c);
			}
		}
		if(r instanceof RestaurantChungWaiterMessageCookRole) {
			RestaurantChungWaiterMessageCookRole w = (RestaurantChungWaiterMessageCookRole)r;
			if(!super.roleExists(w)) {
				RestaurantChungWaiterAnimation anim = new RestaurantChungWaiterAnimation(w); 
				addWaiter(w, anim);
				super.addRole(w, anim);
			}
		}
		if(r instanceof RestaurantChungWaiterRevolvingStandRole) {
			RestaurantChungWaiterRevolvingStandRole w = (RestaurantChungWaiterRevolvingStandRole)r;
			if(!super.roleExists(w)) {
				RestaurantChungWaiterAnimation anim = new RestaurantChungWaiterAnimation(w);
				addWaiter(w, anim);
				w.setRevolvingStand(orderStand);
				super.addRole(w, anim);
			}
		}
		if(r instanceof RestaurantChungHostRole) {
			RestaurantChungHostRole h = (RestaurantChungHostRole)r;
			if(!super.roleExists(h)) { 
				host = h;
				super.addRole(h, null);
			}
		}
		if(r instanceof RestaurantChungCookRole) {
			RestaurantChungCookRole c = (RestaurantChungCookRole)r;
			if(!super.roleExists(c)) {
				RestaurantChungCookAnimation anim = new RestaurantChungCookAnimation(c);
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				cook = c;
				c.setRevolvingStand(orderStand);
				super.addRole(c, anim);
			}
		}
		if(r instanceof RestaurantChungCashierRole) {
			RestaurantChungCashierRole c = (RestaurantChungCashierRole)r;
			if(!super.roleExists(c)) {
				RestaurantChungCashierAnimation anim = new RestaurantChungCashierAnimation(c); 
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				cashier = c;
				super.addRole(c, anim);
			}
		}
	}
		
	public void addWaiter(RestaurantChungWaiter w, RestaurantChungWaiterAnimation anim) {
		waiters.add(w);
		w.setAnimation(anim);
		anim.setVisible(true); // TODO set this in setActive()
		panel.addVisualizationElement(anim);
    	for (int i = 0; i < 9; i++) {
    		anim.addTable(RestaurantChungPanel.TABLEX+((i%3)*RestaurantChungPanel.TABLEGAP), RestaurantChungPanel.TABLEY+((i/3)*RestaurantChungPanel.TABLEGAP));
    	}	
		host.msgNewWaiter(w);
	}
	
	public void removeWaiter(RestaurantChungWaiter waiter) {
		waiters.remove(waiter);
		host.msgRemoveWaiter(waiter);		
	}
}
