package city.buildings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.Building;
import city.Role;
import city.Application.FOOD_ITEMS;
import city.animations.RestaurantZhangCookAnimation;
import city.animations.RestaurantZhangCustomerAnimation;
import city.animations.RestaurantZhangWaiterAnimation;
import city.buildings.RestaurantBaseBuilding.Food;
import city.gui.RestaurantZhangPanel;
import city.interfaces.RestaurantZhangCashier;
import city.interfaces.RestaurantZhangCook;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangHost;
import city.interfaces.RestaurantZhangWaiter;
import city.roles.RestaurantZhangCashierRole;
import city.roles.RestaurantZhangCookRole;
import city.roles.RestaurantZhangCustomerRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.RestaurantZhangWaiterRegularRole;
import city.roles.RestaurantZhangWaiterSharedDataRole;

public class RestaurantZhangBuilding extends RestaurantBaseBuilding {
	
	// Data
	
	private int nTables = 3;
	private static final int TABLEXSTART = 80;
	private static final int TABLEXSPACING = 150;
	private static final int TABLEROW = 3;
	private static final int TABLEYSTART = 250;
	private static final int TABLEYSPACING = 150;
	private static final int TABLECOLUMN = 3;
	private static final int TABLEW = 50;
	private static final int TABLEH = 50;
	// private static int numberOfWaiters = 0;

	public Collection<RestaurantZhangTable> tables;
	public RestaurantZhangMenu menu = new RestaurantZhangMenu();
	public RestaurantZhangRevolvingStand orderStand = new RestaurantZhangRevolvingStand();

	public RestaurantZhangHost host;
	private RestaurantZhangCook cook;
	public RestaurantZhangCashier cashier;
	public Vector<RestaurantZhangCustomer> customers = new Vector<RestaurantZhangCustomer>();
	public Vector<RestaurantZhangWaiter> waiters = new Vector<RestaurantZhangWaiter>();

	public RestaurantZhangPanel panel; //reference to main gui

	// Constructor
	
	public RestaurantZhangBuilding(String name, RestaurantZhangPanel panel) {
		super(name);
		this.setCustomerRoleName("city.roles.RestaurantZhangCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantZhangCustomerAnimation");
		this.panel = panel;
		tables = new ArrayList<RestaurantZhangTable>(nTables);
    	for (int ix = 0; ix < nTables; ix++) {
    		tables.add(new RestaurantZhangTable(ix, TABLEXSTART + ((ix % TABLEROW) * TABLEXSPACING), 
    				TABLEYSTART + ((ix / TABLECOLUMN) * TABLEYSPACING),
    				TABLEW, TABLEH));
    	}
    	foods.put(FOOD_ITEMS.chicken, new Food("Chicken", 2000, 1, 0, 3, menu.getPrice("Chicken")));
        foods.put(FOOD_ITEMS.pizza, new Food("Pizza", 8000, 1, 0, 3, menu.getPrice("Pizza")));
        foods.put(FOOD_ITEMS.steak, new Food("Steak", 4000, 1, 0, 3, menu.getPrice("Steak")));
	}
	
	// Utilities

	@Override
	public void addRole(Role r) {
		if(r instanceof RestaurantZhangCustomerRole) {
			RestaurantZhangCustomerRole c = (RestaurantZhangCustomerRole)r;
			c.setCashier(cashier);
			c.setHost(host);
			if(!super.roleExists(c)) {
				RestaurantZhangCustomerAnimation anim = new RestaurantZhangCustomerAnimation(c); 
				c.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				customers.add(c);
				super.addRole(c, anim);
			}
		}
		if(r instanceof RestaurantZhangWaiterRegularRole) {
			RestaurantZhangWaiterRegularRole w = (RestaurantZhangWaiterRegularRole)r;
			w.setCashier(cashier);
			w.setCook(cook);
			w.setHost(host);
			w.setMenu(menu);
			host.addWaiter(w);
			if(!super.roleExists(w)) {
				RestaurantZhangWaiterAnimation anim = new RestaurantZhangWaiterAnimation(w, waiters.size() * 30 + 80, 200); 
				w.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				waiters.add(w);
				super.addRole(w, anim);
			}
		}
		if(r instanceof RestaurantZhangWaiterSharedDataRole) {
			RestaurantZhangWaiterSharedDataRole w = (RestaurantZhangWaiterSharedDataRole)r;
			w.setCashier(cashier);
			w.setCook(cook);
			w.setHost(host);
			w.setMenu(menu);
			w.setRevolvingStand(orderStand);
			host.addWaiter(w);
			if(!super.roleExists(w)) {
				RestaurantZhangWaiterAnimation anim = new RestaurantZhangWaiterAnimation(w, waiters.size() * 30 + 80, 200); 
				w.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				waiters.add(w);
				super.addRole(w, anim);
			}
		}
		if(r instanceof RestaurantZhangHostRole) {
			RestaurantZhangHostRole h = (RestaurantZhangHostRole)r;
			h.setTables(tables);
			if(!super.roleExists(h)) { 
				host = h;
				super.addRole(h, null);
			}
		}
		if(r instanceof RestaurantZhangCookRole) {
			RestaurantZhangCookRole c = (RestaurantZhangCookRole)r;
			c.setRevolvingStand(orderStand);
			c.setMenuTimes(menu, foods);
			if(!super.roleExists(c)) { 
				RestaurantZhangCookAnimation anim = new RestaurantZhangCookAnimation(c);
				c.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				cook = c;
				super.addRole(c, anim);
			}
		}
		if(r instanceof RestaurantZhangCashierRole) {
			RestaurantZhangCashierRole c = (RestaurantZhangCashierRole)r;
			c.setMenu(menu);
			if(!super.roleExists(c)) { 
				cashier = c;
				super.addRole(c, null);
			}
		}
	}

}
