package city.buildings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.Application.FOOD_ITEMS;
import city.RoleInterface;
import city.abstracts.RestaurantBuildingBase;
import city.animations.RestaurantZhangCookAnimation;
import city.animations.RestaurantZhangCustomerAnimation;
import city.animations.RestaurantZhangWaiterAnimation;
import city.gui.buildings.RestaurantZhangPanel;
import city.gui.views.CityViewBuilding;
import city.interfaces.RestaurantZhang;
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

public class RestaurantZhangBuilding extends RestaurantBuildingBase implements RestaurantZhang {
	
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


	// Constructor
	
	public RestaurantZhangBuilding(String name, RestaurantZhangPanel panel, CityViewBuilding cityBuilding) {
		super(name);
		this.setCustomerRoleName("city.roles.RestaurantZhangCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantZhangCustomerAnimation");
		this.setPanel(panel);
		this.setCityViewBuilding(cityBuilding);
		// Specific to my restaurant panel
		tables = new ArrayList<RestaurantZhangTable>(nTables);
    	for (int ix = 0; ix < nTables; ix++) {
    		tables.add(new RestaurantZhangTable(ix, TABLEXSTART + ((ix % TABLEROW) * TABLEXSPACING), 
    				TABLEYSTART + ((ix / TABLECOLUMN) * TABLEYSPACING),
    				TABLEW, TABLEH));
    	}
    	foods.put(FOOD_ITEMS.chicken, new Food("Chicken", 2000, 50, 0, 3, menu.getPrice("Chicken")));
        foods.put(FOOD_ITEMS.pizza, new Food("Pizza", 8000, 50, 0, 3, menu.getPrice("Pizza")));
        foods.put(FOOD_ITEMS.steak, new Food("Steak", 4000, 50, 0, 3, menu.getPrice("Steak")));
	}
	
	// Utilities

	@Override
	public void addOccupyingRole(RoleInterface r) {
		if(r instanceof RestaurantZhangCustomerRole) {
			RestaurantZhangCustomerRole c = (RestaurantZhangCustomerRole)r;
			c.setCashier(cashier);
			c.setHost(host);
			if(!super.occupyingRoleExists(c)) {
				RestaurantZhangCustomerAnimation anim = new RestaurantZhangCustomerAnimation(c); 
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				this.getPanel().addVisualizationElement(anim);
				customers.add(c);
				super.addOccupyingRole(c, anim);
			}
		}
		if(r instanceof RestaurantZhangWaiterRegularRole) {
			RestaurantZhangWaiterRegularRole w = (RestaurantZhangWaiterRegularRole)r;
			w.setCashier(cashier);
			w.setCook(cook);
			w.setHost(host);
			w.setMenu(menu);
			host.addWaiter(w);
			if(!super.occupyingRoleExists(w)) {
				RestaurantZhangWaiterAnimation anim = new RestaurantZhangWaiterAnimation(w, waiters.size() * 30 + 80, 200); 
				w.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				this.getPanel().addVisualizationElement(anim);
				waiters.add(w);
				super.addOccupyingRole(w, anim);
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
			if(!super.occupyingRoleExists(w)) {
				RestaurantZhangWaiterAnimation anim = new RestaurantZhangWaiterAnimation(w, waiters.size() * 30 + 80, 200); 
				w.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				this.getPanel().addVisualizationElement(anim);
				waiters.add(w);
				super.addOccupyingRole(w, anim);
			}
		}
		if(r instanceof RestaurantZhangHostRole) {
			RestaurantZhangHostRole h = (RestaurantZhangHostRole)r;
			h.setTables(tables);
			if(!super.occupyingRoleExists(h)) { 
				host = h;
				super.addOccupyingRole(h, null);
			}
		}
		if(r instanceof RestaurantZhangCookRole) {
			RestaurantZhangCookRole c = (RestaurantZhangCookRole)r;
			c.setRevolvingStand(orderStand);
			c.setMenuTimes(menu, foods);
//			c.addMarket(new MarketBuilding("Market"));
			if(!super.occupyingRoleExists(c)) { 
				RestaurantZhangCookAnimation anim = new RestaurantZhangCookAnimation(c);
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				this.getPanel().addVisualizationElement(anim);
				cook = c;
				super.addOccupyingRole(c, anim);
			}
		}
		if(r instanceof RestaurantZhangCashierRole) {
			RestaurantZhangCashierRole c = (RestaurantZhangCashierRole)r;
			c.setMenu(menu);
			if(!super.occupyingRoleExists(c)) { 
				cashier = c;
				super.addOccupyingRole(c, null);
			}
		}
	}

}
