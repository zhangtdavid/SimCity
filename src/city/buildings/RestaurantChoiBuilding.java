package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.RestaurantChoiMenu;
import utilities.RestaurantChoiRevolvingStand;
import utilities.RestaurantChoiTable;
import city.Animation;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.abstracts.RestaurantBuildingBase;
import city.abstracts.RestaurantChoiWaiterBase;
import city.animations.RestaurantChoiCashierAnimation;
import city.animations.RestaurantChoiCookAnimation;
import city.animations.RestaurantChoiCustomerAnimation;
import city.animations.RestaurantChoiWaiterAnimation;
import city.gui.buildings.RestaurantChoiPanel;
import city.gui.views.CityViewBuilding;
import city.interfaces.RestaurantChoiCustomer;
import city.roles.BankCustomerRole;
import city.roles.RestaurantChoiCashierRole;
import city.roles.RestaurantChoiCookRole;
import city.roles.RestaurantChoiCustomerRole;
import city.roles.RestaurantChoiHostRole;
import city.roles.RestaurantChoiWaiterDirectRole;
import city.roles.RestaurantChoiWaiterQueueRole;

public class RestaurantChoiBuilding extends RestaurantBuildingBase{

	// Data
	
	public RestaurantChoiCookRole cook;
	public RestaurantChoiCashierRole cashier;
	public RestaurantChoiHostRole host;
	public RestaurantChoiMenu menu;
	public int seatedCustomers = 0;
	public RestaurantChoiPanel panel; //reference to main gui
	public Map<Role, Animation> allRoles = new HashMap<Role, Animation>();
	public List<RestaurantChoiCustomer> customers = Collections.synchronizedList(new ArrayList<RestaurantChoiCustomer>());
	public List<RestaurantChoiWaiterBase> waiters = Collections.synchronizedList(new ArrayList<RestaurantChoiWaiterBase>());
	public RestaurantChoiRevolvingStand rs;
	public RestaurantChoiTable t;
	public BankBuilding bank;
	public BankCustomerRole bankConnection; 
	public static final int DAILY_CAPITAL = 1000;
	public static final int DEPOSIT_THRESHOLD = 1005; // low enough so that I can see depositing behavior
	public static final int WITHDRAW_THRESHOLD = 200;
	private static final int WORKER_SALARY = 500; // this high value helps accelerate normative testing. Also everyone makes the same amount!

	// Constructor

	public RestaurantChoiBuilding(String name, RestaurantChoiPanel panel, CityViewBuilding cityBuilding) {
		super(name);
		setCash(DAILY_CAPITAL); // initial launch
		menu = new RestaurantChoiMenu();
		this.setCustomerRoleName("city.roles.RestaurantChoiCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantChoiCustomerAnimation");
		this.panel = panel;
		bankConnection = new BankCustomerRole(this);
		this.setCityViewBuilding(cityBuilding);
		//this.setCashOnSite(cash_on_site);	
		//set up tables
		// Add items and their data times to a map

		int rand = 7+(int)Math.ceil(10*Math.random());
		super.addFood(FOOD_ITEMS.steak, new Food("Steak", (int)(Math.ceil(Math.random()*6)*1000),
				1, ((int)Math.floor(rand*0.2)), rand, 16));
		rand = 7+(int)Math.ceil(10*Math.random());
		super.addFood(FOOD_ITEMS.pizza, new Food("Pizza", (int)(Math.ceil(Math.random()*6)*1000),
				1, ((int)Math.floor(rand*0.2)), rand, 11));
		rand = 7+(int)Math.ceil(10*Math.random());
		super.addFood(FOOD_ITEMS.chicken, new Food("Chicken", (int)(Math.ceil(Math.random()*6)*1000),
				1, ((int)Math.floor(rand*0.2)), rand, 9));
		rand = 7+(int)Math.ceil(10*Math.random());
		super.addFood(FOOD_ITEMS.salad, new Food("Salad", (int)(Math.ceil(Math.random()*6)*1000),
				1, ((int)Math.floor(rand*0.2)), rand, 6));
	}

	// Getters

	public static int getWorkerSalary() {
		return WORKER_SALARY;
	}

	// Utilities

	public void addRole(Role r) {
		if(r instanceof RestaurantChoiCustomerRole) {
			RestaurantChoiCustomerRole c = (RestaurantChoiCustomerRole)r;
			c.setCashier(cashier);
			c.setHost(host);
			if(!super.roleExists(c)) {
				RestaurantChoiCustomerAnimation anim = new RestaurantChoiCustomerAnimation(c); 
				c.setGui(anim);	
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				customers.add(c);
				super.addRole(c, anim);
			}
		}
		if(r instanceof RestaurantChoiWaiterDirectRole) {
			RestaurantChoiWaiterDirectRole w = (RestaurantChoiWaiterDirectRole)r;
			w.setCashier(cashier);
			w.setCook(cook);
			w.setHost(host);
			host.addWaiter(w);
			if(!super.roleExists(w)) {
				RestaurantChoiWaiterAnimation anim = new RestaurantChoiWaiterAnimation(w); 
				w.setAnimation(anim);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				waiters.add(w);
				super.addRole(w, anim);
			}
		}
		if(r instanceof RestaurantChoiWaiterQueueRole) {
			RestaurantChoiWaiterQueueRole w = (RestaurantChoiWaiterQueueRole)r;
			w.setCashier(cashier);
			w.setCook(cook);
			w.setHost(host);
			w.setRevolvingStand(rs);
			host.addWaiter(w);
			if(!super.roleExists(w)) {
				RestaurantChoiWaiterAnimation anim = new RestaurantChoiWaiterAnimation(w); 
				w.setAnimation(anim);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				waiters.add(w);
				super.addRole(w, anim);			
			}
		}
		if(r instanceof RestaurantChoiHostRole) {
			RestaurantChoiHostRole h = (RestaurantChoiHostRole)r;
			if(!super.roleExists(h)) { 
				host = h;
				super.addRole(h, null);
			}
		}
		if(r instanceof RestaurantChoiCookRole) {
			RestaurantChoiCookRole c = (RestaurantChoiCookRole)r;
			rs = new RestaurantChoiRevolvingStand(); // set revolving stand
			c.setRevolvingStand(rs);
			c.CheckBack();
			if(!super.roleExists(c)) { 
				RestaurantChoiCookAnimation anim = new RestaurantChoiCookAnimation(c);
				c.setGui(anim);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				cook = c;
				super.addRole(c, anim);
			}
		}
		if(r instanceof RestaurantChoiCashierRole) {
			RestaurantChoiCashierRole c = (RestaurantChoiCashierRole)r;
			if(!super.roleExists(c)) { 
				RestaurantChoiCashierAnimation anim = new RestaurantChoiCashierAnimation();
				cashier = c;
				c.setGui(anim);
				allRoles.put(c, null);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				super.addRole(c, anim);
			}
		}
	}
}
