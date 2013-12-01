package city.buildings;

import java.util.Vector;

import utilities.RestaurantJPRevolvingStand;
import city.RoleInterface;
import city.abstracts.RestaurantBuildingBase;
import city.animations.RestaurantJPCookAnimation;
import city.animations.RestaurantJPCustomerAnimation;
import city.animations.RestaurantJPWaiterAnimation;
import city.gui.buildings.RestaurantJPPanel;
import city.gui.views.CityViewRestaurant;
import city.interfaces.RestaurantJP;
import city.interfaces.RestaurantJPCashier;
import city.interfaces.RestaurantJPCustomer;
import city.interfaces.RestaurantJPWaiter;
import city.roles.BankCustomerRole;
import city.roles.RestaurantJPCashierRole;
import city.roles.RestaurantJPCookRole;
import city.roles.RestaurantJPCustomerRole;
import city.roles.RestaurantJPHostRole;
import city.roles.RestaurantJPWaiterRole;
import city.roles.RestaurantJPWaiterSharedDataRole;
import city.roles.RestaurantZhangCashierRole;

public class RestaurantJPBuilding extends RestaurantBuildingBase implements RestaurantJP {
	
	// Data
	
	public RestaurantJPCookRole cook;
	public RestaurantJPCashier cashier;
	public RestaurantJPHostRole host;
	public int seatedCustomers = 0;
	public int funds = 2000;
	int customerCounter = 0;
	int waiterCounter = 0;
	RestaurantJPPanel panel;
	RestaurantJPRevolvingStand orderStand = new RestaurantJPRevolvingStand();
	public BankCustomerRole bankCustomer;
	
	// Constructor
	
	// Utilities
	
	@Override
	public void setCashier(RestaurantJPCashier c){
		this.cashier = c;
	}
	
	public Vector<RestaurantJPCustomer> customers = new Vector<RestaurantJPCustomer>();
	public Vector<RestaurantJPWaiter> waiters = new Vector<RestaurantJPWaiter>();


	// Constructor
	
	public RestaurantJPBuilding(String name, RestaurantJPPanel panel, CityViewRestaurant cvr2) {
		super(name);
		this.setCustomerRoleName("city.roles.RestaurantJPCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantJPCustomerAnimation");
		this.panel = panel;
		bankCustomer = new BankCustomerRole(this);
	}
	
	// Utilities

	@Override
	public void addOccupyingRole(RoleInterface r) {
		if(r instanceof RestaurantJPCustomerRole) {
			RestaurantJPCustomerRole c = (RestaurantJPCustomerRole)r;
			c.setCashier(cashier);
			if(!super.occupyingRoleExists(c)) {
				RestaurantJPCustomerAnimation anim = new RestaurantJPCustomerAnimation(c, customerCounter);
				customerCounter++;
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				customers.add(c);
				super.addOccupyingRole(c, anim);
			}
		}
		if(r instanceof RestaurantJPWaiterRole) {
			RestaurantJPWaiterRole w = (RestaurantJPWaiterRole)r;
			if(!super.occupyingRoleExists(w)) {
				RestaurantJPWaiterAnimation anim = new RestaurantJPWaiterAnimation(w, waiterCounter);
				waiterCounter++;
				w.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				waiters.add(w);
				super.addOccupyingRole(w, anim);
			}
		}
		if(r instanceof RestaurantJPWaiterSharedDataRole) {
			RestaurantJPWaiterSharedDataRole w = (RestaurantJPWaiterSharedDataRole)r;
			w.setRevolvingStand(orderStand);
			if(!super.occupyingRoleExists(w)) {
				RestaurantJPWaiterAnimation anim = new RestaurantJPWaiterAnimation(w, waiterCounter); 
				w.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				waiters.add(w);
				super.addOccupyingRole(w, anim);
			}
		}
		if(r instanceof RestaurantJPHostRole) {
			RestaurantJPHostRole h = (RestaurantJPHostRole)r;
			if(!super.occupyingRoleExists(h)) { 
				host = h;
				super.addOccupyingRole(h, null);
			}
		}
		if(r instanceof RestaurantJPCookRole) {
			RestaurantJPCookRole c = (RestaurantJPCookRole)r;
			c.setRevolvingStand(orderStand);
			if(!super.occupyingRoleExists(c)) { 
				RestaurantJPCookAnimation anim = new RestaurantJPCookAnimation(c);
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				panel.addVisualizationElement(anim);
				cook = c;
				super.addOccupyingRole(c, anim);
			}
		}
		if(r instanceof RestaurantZhangCashierRole) {
			RestaurantJPCashierRole c = (RestaurantJPCashierRole)r;
			if(!super.occupyingRoleExists(c)) { 
				cashier = c;
				super.addOccupyingRole(c, null);
			}
		}
	}

}

