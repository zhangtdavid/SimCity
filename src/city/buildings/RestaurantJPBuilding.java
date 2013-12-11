package city.buildings;

import java.util.Vector;

import utilities.RestaurantJPRevolvingStand;
import city.animations.RestaurantJPCookAnimation;
import city.animations.RestaurantJPCustomerAnimation;
import city.animations.RestaurantJPWaiterAnimation;
import city.bases.RestaurantBuilding;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.RestaurantJP;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.RestaurantJPPanel;
import city.roles.RestaurantJPCashierRole;
import city.roles.RestaurantJPCookRole;
import city.roles.RestaurantJPCustomerRole;
import city.roles.RestaurantJPHostRole;
import city.roles.RestaurantJPWaiterRole;
import city.roles.RestaurantJPWaiterSharedDataRole;
import city.roles.interfaces.RestaurantJPCashier;
import city.roles.interfaces.RestaurantJPCustomer;
import city.roles.interfaces.RestaurantJPWaiter;

public class RestaurantJPBuilding extends RestaurantBuilding implements RestaurantJP {
	
	// Data
	
	public RestaurantJPCookRole cook;
	public RestaurantJPCashier cashier;
	public RestaurantJPHostRole host;
	public int seatedCustomers = 0;
	int customerCounter = 0;
	int waiterCounter = 0;
	RestaurantJPRevolvingStand orderStand = new RestaurantJPRevolvingStand();
	
	// Constructor
	
	@Override
	public boolean getBusinessIsOpen() {
		boolean disposition = false;

		return disposition;
	}
	
	// Utilities
	
	@Override
	public void setCashier(RestaurantJPCashier c){
		this.cashier = c;
	}
	
	public void setHost(RestaurantJPHostRole h){
		host = h;
	}
	
	public Vector<RestaurantJPCustomer> customers = new Vector<RestaurantJPCustomer>();
	public Vector<RestaurantJPWaiter> waiters = new Vector<RestaurantJPWaiter>();


	// Constructor
	
	public RestaurantJPBuilding(String name, RestaurantJPPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		this.setCustomerRoleName("city.roles.RestaurantJPCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantJPCustomerAnimation");
		setCash(2000);
		
		this.addWorkerRoleName("city.roles.RestaurantJPCashierRole");
		this.addWorkerRoleName("city.roles.RestaurantJPCookRole");
		this.addWorkerRoleName("city.roles.RestaurantJPHostRole");
		this.addWorkerRoleName("city.roles.RestaurantJPWaiterRole");
		this.addWorkerRoleName("city.roles.RestaurantJPWaiterSharedDataRole");
		this.setBuildingClassName("city.buildings.interfaces.RestaurantJP");
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
				c.setHost(host);
				c.setCashier(cashier);
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				this.getPanel().addVisualizationElement(anim);
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
				this.getPanel().addVisualizationElement(anim);
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
				this.getPanel().addVisualizationElement(anim);
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
				this.getPanel().addVisualizationElement(anim);
				cook = c;
				super.addOccupyingRole(c, anim);
			}
		}
		if(r instanceof RestaurantJPCashierRole) {
			RestaurantJPCashierRole c = (RestaurantJPCashierRole)r;
			if(!super.occupyingRoleExists(c)) { 
				cashier = c;
				super.addOccupyingRole(c, null);
			}
		}
	}

}

