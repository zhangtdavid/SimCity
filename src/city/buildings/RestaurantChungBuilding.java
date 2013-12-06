package city.buildings;

import java.util.ArrayList;
import java.util.List;

import utilities.RestaurantChungRevolvingStand;
import city.Application;
import city.Application.BUILDING;
import city.Application.FOOD_ITEMS;
import city.animations.RestaurantChungCashierAnimation;
import city.animations.RestaurantChungCookAnimation;
import city.animations.RestaurantChungCustomerAnimation;
import city.animations.RestaurantChungWaiterAnimation;
import city.animations.interfaces.RestaurantChungAnimatedCustomer;
import city.bases.RestaurantBuilding;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Bank;
import city.buildings.interfaces.RestaurantChung;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.RestaurantChungPanel;
import city.roles.BankCustomerRole;
import city.roles.RestaurantChungCashierRole;
import city.roles.RestaurantChungCookRole;
import city.roles.RestaurantChungCustomerRole;
import city.roles.RestaurantChungHostRole;
import city.roles.RestaurantChungWaiterMessageCookRole;
import city.roles.RestaurantChungWaiterRevolvingStandRole;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.RestaurantChungCashier;
import city.roles.interfaces.RestaurantChungCook;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungHost;
import city.roles.interfaces.RestaurantChungWaiter;

public class RestaurantChungBuilding extends RestaurantBuilding implements RestaurantChung {
//	Data
//	=====================================================================
	private RestaurantChungHost host;
	private RestaurantChungCashier cashier;
	private RestaurantChungCook cook;
	private BankCustomer bankCustomer;
	private List<RestaurantChungWaiter> waiters = new ArrayList<RestaurantChungWaiter>();
	private List<RestaurantChungCustomer> customers = new ArrayList<RestaurantChungCustomer>();
	
	private RestaurantChungRevolvingStand orderStand;
	
	public static final int WORKER_SALARY = 500;
	
//	Constructor
//	=====================================================================
	public RestaurantChungBuilding(String name, RestaurantChungPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		this.setCustomerRoleName("city.roles.RestaurantChungCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantChungCustomerAnimation");
		orderStand = new RestaurantChungRevolvingStand();
		bankCustomer = new BankCustomerRole(this, (Bank)(Application.CityMap.findRandomBuilding(BUILDING.bank)));
	}
	
//	Getters
//	=====================================================================	
	@Override
	public RestaurantChungPanel getRestaurantChungPanel() {
		return (RestaurantChungPanel)(super.getPanel());
	}
	
	@Override
	public RestaurantChungHost getRestaurantChungHost() {
		return host;
	}
	
	@Override
	public RestaurantChungCashier getRestaurantChungCashier() {
		return cashier;
	}
	
	@Override
	public RestaurantChungCook getRestaurantChungCook() {
		return cook;
	}
	
	@Override
	public BankCustomer getBankCustomer() {
		return bankCustomer;
	}
	
	@Override
	public List<RestaurantChungWaiter> getWaiters() {
		return waiters;
	}
	
	@Override
	public List<RestaurantChungCustomer> getCustomers() {
		return customers;
	}

//	Setters
//	=====================================================================	
	//Only used in testing, usually set in addOccupyingRole() call
	@Override
	public void setRestaurantChungHost(RestaurantChungHost host) {
		this.host = host;
	}
	
	@Override
	public void setRestaurantChungCashier(RestaurantChungCashier cashier) {
		this.cashier = cashier;
	}
	
	@Override
	public void setRestaurantChungCook(RestaurantChungCook cook) {
		this.cook = cook;
	}
		
//	Utilities
//	=====================================================================	
	@Override
	public void addOccupyingRole(RoleInterface r) {
		if(r instanceof RestaurantChungCustomerRole) {
			RestaurantChungCustomerRole c = (RestaurantChungCustomerRole)r;
			if(!super.occupyingRoleExists(c)) {
				RestaurantChungAnimatedCustomer anim = new RestaurantChungCustomerAnimation(c); 
				c.setAnimation(anim);
				c.setRestaurant(this);
				anim.setVisible(true); // TODO set this in setActive()
				getPanel().addVisualizationElement(anim);
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
				getPanel().addVisualizationElement(anim);
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
				getPanel().addVisualizationElement(anim);
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
		getPanel().addVisualizationElement(anim);
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
