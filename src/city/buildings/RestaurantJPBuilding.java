package city.buildings;

import java.util.ArrayList;
import java.util.Vector;

import utilities.RestaurantJPRevolvingStand;
import utilities.RestaurantZhangTable;
import city.Role;
import city.animations.RestaurantJPCookAnimation;
import city.animations.RestaurantJPCustomerAnimation;
import city.animations.RestaurantJPWaiterAnimation;
import city.animations.RestaurantZhangCookAnimation;
import city.animations.RestaurantZhangCustomerAnimation;
import city.animations.RestaurantZhangWaiterAnimation;
import city.gui.RestaurantJPPanel;
import city.gui.RestaurantZhangPanel;
import city.interfaces.RestaurantJPCashier;
import city.interfaces.RestaurantJPCustomer;
import city.interfaces.RestaurantJPWaiter;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangWaiter;
import city.roles.RestaurantJPCashierRole;
import city.roles.RestaurantJPCookRole;
import city.roles.RestaurantJPCustomerRole;
import city.roles.RestaurantJPHostRole;
import city.roles.RestaurantJPWaiterRole;
import city.roles.RestaurantJPWaiterSharedDataRole;
import city.roles.RestaurantZhangCashierRole;
import city.roles.RestaurantZhangCookRole;
import city.roles.RestaurantZhangCustomerRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.RestaurantZhangWaiterRegularRole;
import city.roles.RestaurantZhangWaiterSharedDataRole;

public class RestaurantJPBuilding extends RestaurantBaseBuilding {
	
	// Data
	
	public RestaurantJPCookRole cook;
	public RestaurantJPCashier cashier;
	public RestaurantJPHostRole host;
	public int seatedCustomers = 0;
	public int funds = 2000;
	public static final int WORKER_SALARY = 200;
	int customerCounter = 0;
	int waiterCounter = 0;
	RestaurantJPPanel panel;
	RestaurantJPRevolvingStand orderStand = new RestaurantJPRevolvingStand();
	
	// Constructor
	
	// Utilities
	
	public void setCashier(RestaurantJPCashier c){
		this.cashier = c;
	}
	
	public Vector<RestaurantJPCustomer> customers = new Vector<RestaurantJPCustomer>();
	public Vector<RestaurantJPWaiter> waiters = new Vector<RestaurantJPWaiter>();


	// Constructor
	
	public RestaurantJPBuilding(String name, RestaurantJPPanel panel) {
		super(name);
		this.setCustomerRoleName("city.roles.RestaurantJPCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantJPCustomerAnimation");
		this.panel = panel;
	}
	
	// Utilities

	@Override
	public void addRole(Role r) {
		if(r instanceof RestaurantJPCustomerRole) {
			RestaurantJPCustomerRole c = (RestaurantJPCustomerRole)r;
			c.setCashier(cashier);
			if(!super.roleExists(c)) {
				RestaurantJPCustomerAnimation anim = new RestaurantJPCustomerAnimation(c, customerCounter);
				customerCounter++;
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				customers.add(c);
				super.addRole(c, anim);
			}
		}
		if(r instanceof RestaurantJPWaiterRole) {
			RestaurantJPWaiterRole w = (RestaurantJPWaiterRole)r;
			if(!super.roleExists(w)) {
				RestaurantJPWaiterAnimation anim = new RestaurantJPWaiterAnimation(w, waiterCounter);
				waiterCounter++;
				w.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				waiters.add(w);
				super.addRole(w, anim);
			}
		}
		if(r instanceof RestaurantJPWaiterSharedDataRole) {
			RestaurantJPWaiterSharedDataRole w = (RestaurantJPWaiterSharedDataRole)r;
			w.setRevolvingStand(orderStand);
			if(!super.roleExists(w)) {
				RestaurantJPWaiterAnimation anim = new RestaurantJPWaiterAnimation(w, waiterCounter); 
				w.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				waiters.add(w);
				super.addRole(w, anim);
			}
		}
		if(r instanceof RestaurantJPHostRole) {
			RestaurantJPHostRole h = (RestaurantJPHostRole)r;
			if(!super.roleExists(h)) { 
				host = h;
				super.addRole(h, null);
			}
		}
		if(r instanceof RestaurantJPCookRole) {
			RestaurantJPCookRole c = (RestaurantJPCookRole)r;
			c.setRevolvingStand(orderStand);
			if(!super.roleExists(c)) { 
				RestaurantJPCookAnimation anim = new RestaurantJPCookAnimation(c);
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				cook = c;
				super.addRole(c, anim);
			}
		}
		if(r instanceof RestaurantZhangCashierRole) {
			RestaurantJPCashierRole c = (RestaurantJPCashierRole)r;
			if(!super.roleExists(c)) { 
				cashier = c;
				super.addRole(c, null);
			}
		}
	}

}

