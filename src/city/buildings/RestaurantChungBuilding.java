package city.buildings;

import java.util.ArrayList;
import java.util.List;

import utilities.RestaurantChungRevolvingStand;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Application.FOOD_ITEMS;
import city.RoleInterface;
import city.abstracts.RestaurantBuildingBase;
import city.animations.RestaurantChungCashierAnimation;
import city.animations.RestaurantChungCookAnimation;
import city.animations.RestaurantChungCustomerAnimation;
import city.animations.RestaurantChungWaiterAnimation;
import city.gui.buildings.RestaurantChungPanel;
import city.gui.views.CityViewBuilding;
import city.interfaces.RestaurantChung;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCook;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungHost;
import city.interfaces.RestaurantChungWaiter;
import city.roles.BankCustomerRole;
import city.roles.RestaurantChungCashierRole;
import city.roles.RestaurantChungCookRole;
import city.roles.RestaurantChungCustomerRole;
import city.roles.RestaurantChungHostRole;
import city.roles.RestaurantChungWaiterMessageCookRole;
import city.roles.RestaurantChungWaiterRevolvingStandRole;

public class RestaurantChungBuilding extends RestaurantBuildingBase implements RestaurantChung {
	
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
	public RestaurantChungBuilding(String name, RestaurantChungPanel panel, CityViewBuilding cityBuilding) {
		super(name);
		this.setCustomerRoleName("city.roles.RestaurantChungCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantChungCustomerAnimation");
		this.panel = panel;
		orderStand = new RestaurantChungRevolvingStand();
		this.setCityViewBuilding(cityBuilding);
		bankCustomer = new BankCustomerRole(this, (BankBuilding) CityMap.findRandomBuilding(BUILDING.bank));
		
        // Add items and their cooking times to a map
		super.addFood(FOOD_ITEMS.chicken, new Food("chicken", 10, 6, 5, 10, 16));
		super.addFood(FOOD_ITEMS.pizza, new Food("pizza", 15, 6, 5, 10, 12));
		super.addFood(FOOD_ITEMS.salad, new Food("salad", 5, 6, 5, 10, 6));
		super.addFood(FOOD_ITEMS.steak, new Food("steak", 20, 6, 5, 10, 10));
        
        setCash(999);
	}
	
	// Getters
	
	public static int getWorkerSalary() {
		return WORKER_SALARY;
	}

	// Utilities
	
	@Override
	public void addOccupyingRole(RoleInterface r) {
		if(r instanceof RestaurantChungCustomerRole) {
			RestaurantChungCustomerRole c = (RestaurantChungCustomerRole)r;
			if(!super.occupyingRoleExists(c)) {
				RestaurantChungCustomerAnimation anim = new RestaurantChungCustomerAnimation(c); 
				c.setAnimation(anim);
				c.setRestaurant(this);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				customers.add(c);
				super.addOccupyingRole(c, anim);
				host.msgIWantToEat(c);
			}
		}
		if(r instanceof RestaurantChungWaiterMessageCookRole) {
			RestaurantChungWaiterMessageCookRole w = (RestaurantChungWaiterMessageCookRole)r;
			if(!super.occupyingRoleExists(w)) {
				RestaurantChungWaiterAnimation anim = new RestaurantChungWaiterAnimation(w); 
				addWaiter(w, anim);
				super.addOccupyingRole(w, anim);
			}
		}
		if(r instanceof RestaurantChungWaiterRevolvingStandRole) {
			RestaurantChungWaiterRevolvingStandRole w = (RestaurantChungWaiterRevolvingStandRole)r;
			if(!super.occupyingRoleExists(w)) {
				RestaurantChungWaiterAnimation anim = new RestaurantChungWaiterAnimation(w);
				addWaiter(w, anim);
				w.setRevolvingStand(orderStand);
				super.addOccupyingRole(w, anim);
			}
		}
		if(r instanceof RestaurantChungHostRole) {
			RestaurantChungHostRole h = (RestaurantChungHostRole)r;
			if(!super.occupyingRoleExists(h)) { 
				host = h;
				super.addOccupyingRole(h, null);
			}
		}
		if(r instanceof RestaurantChungCookRole) {
			RestaurantChungCookRole c = (RestaurantChungCookRole)r;
			if(!super.occupyingRoleExists(c)) {
				RestaurantChungCookAnimation anim = new RestaurantChungCookAnimation(c);
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				cook = c;
				c.setRevolvingStand(orderStand);
				super.addOccupyingRole(c, anim);
			}
		}
		if(r instanceof RestaurantChungCashierRole) {
			RestaurantChungCashierRole c = (RestaurantChungCashierRole)r;
			if(!super.occupyingRoleExists(c)) {
				RestaurantChungCashierAnimation anim = new RestaurantChungCashierAnimation(c); 
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				cashier = c;
				super.addOccupyingRole(c, anim);
			}
		}
	}
		
	@Override
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
	
	@Override
	public void removeWaiter(RestaurantChungWaiter waiter) {
		waiters.remove(waiter);
		host.msgRemoveWaiter(waiter);		
	}
}
