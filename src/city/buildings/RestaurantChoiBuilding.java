package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.RestaurantChoiMenu;
import utilities.RestaurantChoiRevolvingStand;
import utilities.RestaurantChoiWaiterBase;
import city.Application;
import city.Application.BUILDING;
import city.animations.RestaurantChoiCashierAnimation;
import city.animations.RestaurantChoiCookAnimation;
import city.animations.RestaurantChoiCustomerAnimation;
import city.animations.RestaurantChoiWaiterAnimation;
import city.bases.Animation;
import city.bases.RestaurantBuilding;
import city.bases.Role;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Bank;
import city.buildings.interfaces.RestaurantChoi;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.RestaurantChoiPanel;
import city.roles.BankCustomerRole;
import city.roles.RestaurantChoiCashierRole;
import city.roles.RestaurantChoiCookRole;
import city.roles.RestaurantChoiCustomerRole;
import city.roles.RestaurantChoiHostRole;
import city.roles.RestaurantChoiWaiterDirectRole;
import city.roles.RestaurantChoiWaiterQueueRole;
import city.roles.interfaces.RestaurantChoiCustomer;

public class RestaurantChoiBuilding extends RestaurantBuilding implements RestaurantChoi{

	// Data
	
	private RestaurantChoiCookRole cook;
	private RestaurantChoiCashierRole cashier;
	private RestaurantChoiHostRole host;
	public RestaurantChoiMenu menu; // anyone can look up a menu of a restaurant.
	public int seatedCustomers = 0; // anyone can know how many seated customers there are in a restaurant.
	private RestaurantChoiPanel panel; //reference to main gui
	private Map<Role, Animation> allRoles = new HashMap<Role, Animation>();
	private List<RestaurantChoiCustomer> customers = Collections.synchronizedList(new ArrayList<RestaurantChoiCustomer>());
	private List<RestaurantChoiWaiterBase> waiters = Collections.synchronizedList(new ArrayList<RestaurantChoiWaiterBase>());
	private RestaurantChoiRevolvingStand rs;
	private static final int WORKER_SALARY = 500; // this high value helps accelerate normative testing. Also everyone makes the same amount!

	// Constructor

	public RestaurantChoiBuilding(String name, RestaurantChoiPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		setCash(DAILY_CAPITAL); // initial launch
		menu = new RestaurantChoiMenu();
		this.setCustomerRoleName("city.roles.RestaurantChoiCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantChoiCustomerAnimation");
		this.panel = panel;
		this.setBankCustomer(new BankCustomerRole(this, (Bank)(Application.CityMap.findRandomBuilding(BUILDING.bank))));
		this.setCityViewBuilding(cityBuilding);
		//set up tables
		// Add items and their data times to a map
/*
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
	*/			
		
		this.addWorkerRoleName("city.roles.RestaurantChoiCashierRole");
		this.addWorkerRoleName("city.roles.RestaurantChoiCookRole");
		this.addWorkerRoleName("city.roles.RestaurantChoiHostRole");
		this.addWorkerRoleName("city.roles.RestaurantChoiWaiterDirectRole");
		this.addWorkerRoleName("city.roles.RestaurantChoiWaiterQueueRole");
		this.setBuildingClassName("city.buildings.interfaces.RestaurantChoi");
	}

	// Getters
	
	@Override
	public boolean getBusinessIsOpen() {
		boolean disposition = false;
		if(this.getCashier().getActive() && this.cook.getActive() && this.getHost().getActive()) // if these guys are here
			for (RestaurantChoiWaiterBase w : waiters) { // and if one waiter is here
				if(w.getActive())
					disposition = true;
			}
		return disposition;
	}

	public static int getWorkerSalary() {
		return WORKER_SALARY;
	}

	// Utilities

	@Override
	public void addOccupyingRole(RoleInterface r) {
		if(r instanceof RestaurantChoiCustomerRole) {
			RestaurantChoiCustomerRole c = (RestaurantChoiCustomerRole)r;
			c.setCashier(getCashier());
			c.setHost(getHost());
			if(!super.occupyingRoleExists(c)) {
				RestaurantChoiCustomerAnimation anim = new RestaurantChoiCustomerAnimation(c); 
				c.setGui(anim);	
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				customers.add(c);
				super.addOccupyingRole(c, anim);
			}
		}
		if(r instanceof RestaurantChoiWaiterDirectRole) {
			RestaurantChoiWaiterDirectRole w = (RestaurantChoiWaiterDirectRole)r;
			w.setCashier(getCashier());
			w.setCook(cook);
			w.setHost(getHost());
			getHost().addWaiter(w);
			if(!super.occupyingRoleExists(w)) {
				RestaurantChoiWaiterAnimation anim = new RestaurantChoiWaiterAnimation(w); 
				w.setAnimation(anim);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				waiters.add(w);
				super.addOccupyingRole(w, anim);
			}
		}
		if(r instanceof RestaurantChoiWaiterQueueRole) {
			RestaurantChoiWaiterQueueRole w = (RestaurantChoiWaiterQueueRole)r;
			w.setCashier(getCashier());
			w.setCook(cook);
			w.setHost(getHost());
			w.setRevolvingStand(rs);
			getHost().addWaiter(w);
			if(!super.occupyingRoleExists(w)) {
				RestaurantChoiWaiterAnimation anim = new RestaurantChoiWaiterAnimation(w); 
				w.setAnimation(anim);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				waiters.add(w);
				super.addOccupyingRole(w, anim);			
			}
		}
		if(r instanceof RestaurantChoiHostRole) {
			RestaurantChoiHostRole h = (RestaurantChoiHostRole)r;
			if(!super.occupyingRoleExists(h)) { 
				setHost(h);
				super.addOccupyingRole(h, null);
			}
		}
		if(r instanceof RestaurantChoiCookRole) {
			RestaurantChoiCookRole c = (RestaurantChoiCookRole)r;
			rs = new RestaurantChoiRevolvingStand(); // set revolving stand
			c.setRevolvingStand(rs);
			c.CheckBack();
			if(!super.occupyingRoleExists(c)) { 
				RestaurantChoiCookAnimation anim = new RestaurantChoiCookAnimation(c);
				c.setGui(anim);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				cook = c;
				super.addOccupyingRole(c, anim);
			}
		}
		if(r instanceof RestaurantChoiCashierRole) {
			RestaurantChoiCashierRole c = (RestaurantChoiCashierRole)r;
			this.getBankCustomer().setPerson(r.getPerson()); // TODO this solves null pointers on print?
			if(!super.occupyingRoleExists(c)) { 
				RestaurantChoiCashierAnimation anim = new RestaurantChoiCashierAnimation();
				setCashier(c);
				c.setGui(anim);
				allRoles.put(c, null);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				super.addOccupyingRole(c, anim);
			}
		}
	}

	public RestaurantChoiHostRole getHost() {
		return host;
	}

	public void setHost(RestaurantChoiHostRole host) {
		this.host = host;
	}

	public RestaurantChoiCashierRole getCashier() {
		return cashier;
	}

	public void setCashier(RestaurantChoiCashierRole cashier) {
		this.cashier = cashier;
	}
}
