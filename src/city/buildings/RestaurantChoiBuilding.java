package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.RestaurantChoiRevolvingStand;
import city.Animation;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.animations.RestaurantChoiCashierAnimation;
import city.animations.RestaurantChoiCookAnimation;
import city.animations.RestaurantChoiCustomerAnimation;
import city.animations.RestaurantChoiWaiterAnimation;
import city.gui.RestaurantChoiPanel;
import city.interfaces.RestaurantChoiCustomer;
import city.interfaces.RestaurantChoiWaiterAbs;
import city.roles.BankCustomerRole;
import city.roles.RestaurantChoiCashierRole;
import city.roles.RestaurantChoiCookRole;
import city.roles.RestaurantChoiCustomerRole;
import city.roles.RestaurantChoiHostRole;
import city.roles.RestaurantChoiWaiter2Role;
import city.roles.RestaurantChoiWaiterRole;

public class RestaurantChoiBuilding extends RestaurantBaseBuilding{
	
	// Data
	
	public RestaurantChoiCookRole cook;
	public RestaurantChoiCashierRole cashier;
	public RestaurantChoiHostRole host;
	public int seatedCustomers = 0;
	public RestaurantChoiPanel panel; //reference to main gui
	public Map<Role, Animation> allRoles = new HashMap<Role, Animation>();
	public List<RestaurantChoiCustomer> customers = Collections.synchronizedList(new ArrayList<RestaurantChoiCustomer>());
	public List<RestaurantChoiWaiterAbs> waiters = Collections.synchronizedList(new ArrayList<RestaurantChoiWaiterAbs>());
	public RestaurantChoiRevolvingStand rs;
	public BankBuilding bank;
	public BankCustomerRole bankConnection; 
	public static final int DAILY_CAPITAL = 1000;
	public static final int DEPOSIT_THRESHOLD = 1005; // low enough so that I can see depositing behavior
	public static final int WITHDRAW_THRESHOLD = 200; // low enough so that I can see depositing behavior
	private int cash_on_site;
	private static final int WORKER_SALARY = 500; // this high value helps accelerate normative testing. Also everyone makes the same amount!
	
	// Constructor
	
	public RestaurantChoiBuilding(String name, RestaurantChoiPanel panel) {
		super(name);
		bank = new BankBuilding("bank1");
		this.setCustomerRoleName("city.roles.RestaurantChoiCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantChoiCustomerAnimation");
		this.panel = panel;
		bankConnection = new BankCustomerRole(this);
		this.setCashOnSite(DAILY_CAPITAL);	
        // Add items and their cooking times to a map
		int rand = 7+(int)Math.ceil(10*Math.random());
        foods.put(FOOD_ITEMS.steak, new Food("Steak", (int)(Math.ceil(Math.random()*6)*1000),
				(3+(int)Math.ceil(4*Math.random())), ((int)Math.floor(rand*0.2)), rand, 16));
		rand = 7+(int)Math.ceil(10*Math.random());
		foods.put(FOOD_ITEMS.pizza, new Food("Pizza", (int)(Math.ceil(Math.random()*6)*1000),
				(3+(int)Math.ceil(4*Math.random())), ((int)Math.floor(rand*0.2)), rand, 11));
		rand = 7+(int)Math.ceil(10*Math.random());
		foods.put(FOOD_ITEMS.chicken, new Food("Chicken", (int)(Math.ceil(Math.random()*6)*1000),
				(3+(int)Math.ceil(4*Math.random())), ((int)Math.floor(rand*0.2)), rand, 9));
		rand = 7+(int)Math.ceil(10*Math.random());
		foods.put(FOOD_ITEMS.salad, new Food("Salad", (int)(Math.ceil(Math.random()*6)*1000),
				(3+(int)Math.ceil(4*Math.random())), ((int)Math.floor(rand*0.2)), rand, 6));
	}

	// Getters
	
	public static int getWorkerSalary() {
		return WORKER_SALARY;
	}
	
	protected int getCashOnSite() {
		return cash_on_site;
	}
	private void setCashOnSite(int in){
		cash_on_site = in;
	}
	
	// Utilities
	
	public void addRole(Role r) {
		if(r instanceof RestaurantChoiCustomerRole) {
			RestaurantChoiCustomerRole c = (RestaurantChoiCustomerRole)r;
			c.setCashier(cashier);
			c.setHost(host);
			if(!allRoles.containsKey(c)) {
				RestaurantChoiCustomerAnimation anim = new RestaurantChoiCustomerAnimation(c); 
				c.setAnimation(anim);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				customers.add(c);
				allRoles.put(c, anim);
				host.msgImHungry(c);
			}
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
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				waiters.add(w);
				allRoles.put(w, anim);
			}
		}
		if(r instanceof RestaurantChoiWaiterRole) {
			RestaurantChoiWaiterRole w = (RestaurantChoiWaiterRole)r;
			w.setCashier(cashier);
			w.setCook(cook);
			w.setHost(host);
			w.setRevolvingStand(rs);
			if(!allRoles.containsKey(w)) {
				RestaurantChoiWaiterAnimation anim = new RestaurantChoiWaiterAnimation(w); 
				w.setAnimation(anim);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				waiters.add(w);
				allRoles.put(w, anim);
			}
		}
		if(r instanceof RestaurantChoiHostRole) {
			RestaurantChoiHostRole h = (RestaurantChoiHostRole)r;
			if(!allRoles.containsKey(h)) { 
				host = h;
				allRoles.put(h, null);
			}
		}
		if(r instanceof RestaurantChoiCookRole) {
			RestaurantChoiCookRole c = (RestaurantChoiCookRole)r;
			rs = new RestaurantChoiRevolvingStand(); // set revolving stand
			c.setRevolvingStand(rs);
			if(!allRoles.containsKey(c)) { 
				RestaurantChoiCookAnimation anim = new RestaurantChoiCookAnimation(c);
				c.setAnimation(anim);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				cook = c;
				allRoles.put(c, anim);
			}
		}
		if(r instanceof RestaurantChoiCashierRole) {
			RestaurantChoiCashierRole c = (RestaurantChoiCashierRole)r;
			if(!allRoles.containsKey(c)) { 
				RestaurantChoiCashierAnimation anim = new RestaurantChoiCashierAnimation();
				cashier = c;
				c.setAnimation(anim);
				allRoles.put(c, null);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				allRoles.put(c, anim);
			}
		}
	}
	
}
