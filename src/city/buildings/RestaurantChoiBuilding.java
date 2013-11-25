package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.RestaurantChoiRevolvingStand;
import utilities.RestaurantChoiTable;
import city.Animation;
import city.Building;
import city.Role;
import city.animations.RestaurantChoiCashierAnimation;
import city.animations.RestaurantChoiCookAnimation;
import city.animations.RestaurantChoiCustomerAnimation;
import city.animations.RestaurantChoiWaiterAnimation;
import city.gui.RestaurantChoiPanel;
import city.interfaces.RestaurantChoiCustomer;
import city.interfaces.RestaurantChoiWaiterAbs;
import city.roles.RestaurantChoiCashierRole;
import city.roles.RestaurantChoiCookRole;
import city.roles.RestaurantChoiHostRole;
import city.roles.RestaurantChoiCustomerRole;
import city.roles.RestaurantChoiWaiter2Role;
import city.roles.RestaurantChoiWaiterRole;

public class RestaurantChoiBuilding extends Building{
	public RestaurantChoiCookRole cook;
	public RestaurantChoiCashierRole cashier;
	public RestaurantChoiHostRole host;
	public int seatedCustomers = 0;
	public RestaurantChoiPanel panel; //reference to main gui
	public Map<Role, Animation> allRoles = new HashMap<Role, Animation>();
	public List<RestaurantChoiCustomer> customers = Collections.synchronizedList(new ArrayList<RestaurantChoiCustomer>());
	public List<RestaurantChoiWaiterAbs> waiters = Collections.synchronizedList(new ArrayList<RestaurantChoiWaiterAbs>());
	public RestaurantChoiRevolvingStand rs;
	
	public RestaurantChoiBuilding(String name, RestaurantChoiPanel panel){
		super(name);
		this.setCustomerRole("city.roles.RestaurantChoiCustomerRole");
		this.setCustomerAnimation("city.animations.RestaurantChoiCustomerAnimation");
		this.panel = panel;
		
	}
	public static int getWorkerSalary() {
		return WORKER_SALARY;
	}
	private static final int WORKER_SALARY = 500; 
	// this high value helps accelerate normative testing. Also everyone makes the same amount!
	
	public Role addRole(Role r) {
		if(r instanceof RestaurantChoiCustomerRole) {
			RestaurantChoiCustomerRole c = (RestaurantChoiCustomerRole)r;
			c.setCashier(cashier);
			c.setHost(host);
			if(!allRoles.containsKey(c)) {
				RestaurantChoiCustomerAnimation anim = new RestaurantChoiCustomerAnimation(c); 
				c.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				customers.add(c);
				allRoles.put(c, anim);
				host.msgImHungry(c);
			}
			return c;
		}
		if(r instanceof RestaurantChoiWaiter2Role) {
			RestaurantChoiWaiter2Role w = (RestaurantChoiWaiter2Role)r;
			w.setCashier(cashier);
			w.setCook(cook);
			w.setHost(host);
			host.addWaiter(w);
			if(!allRoles.containsKey(w)) {
				RestaurantChoiWaiterAnimation anim = new RestaurantChoiWaiterAnimation(w); 
				w.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				waiters.add(w);
				allRoles.put(w, anim);
			}
			return w;
		}
		if(r instanceof RestaurantChoiWaiterRole) {
			RestaurantChoiWaiterRole w = (RestaurantChoiWaiterRole)r;
			w.setCashier(cashier);
			w.setCook(cook);
			w.setHost(host);
			if(!allRoles.containsKey(w)) {
				RestaurantChoiWaiterAnimation anim = new RestaurantChoiWaiterAnimation(w); 
				w.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				waiters.add(w);
				allRoles.put(w, anim);
			}
			return w;
		}
		if(r instanceof RestaurantChoiHostRole) {
			RestaurantChoiHostRole h = (RestaurantChoiHostRole)r;
			if(!allRoles.containsKey(h)) { 
				host = h;
				allRoles.put(h, null);
			}
			return h;
		}
		if(r instanceof RestaurantChoiCookRole) {
			RestaurantChoiCookRole c = (RestaurantChoiCookRole)r;
			rs = new RestaurantChoiRevolvingStand(cook); // set revolving stand
			c.setRevolvingStand(rs);
			if(!allRoles.containsKey(c)) { 
				RestaurantChoiCookAnimation anim = new RestaurantChoiCookAnimation(c);
				c.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				cook = c;
				allRoles.put(c, anim);
			}
			return c;
		}
		if(r instanceof RestaurantChoiCashierRole) {
			RestaurantChoiCashierRole c = (RestaurantChoiCashierRole)r;
			if(!allRoles.containsKey(c)) { 
				RestaurantChoiCashierAnimation anim = new RestaurantChoiCashierAnimation();
				cashier = c;
				c.setAnimation(anim);
				allRoles.put(c, null);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				allRoles.put(c, anim);
			}
			return c;
		}
		return null;
	}
}
