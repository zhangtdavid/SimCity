package city.buildings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.Animation;
import city.Building;
import city.Role;
import city.animations.RestaurantZhangCookAnimation;
import city.animations.RestaurantZhangCustomerAnimation;
import city.animations.RestaurantZhangWaiterAnimation;
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

public class RestaurantZhangBuilding extends Building {
	private int nTables = 3;
	private static final int TABLEXSTART = 80;
	private static final int TABLEXSPACING = 150;
	private static final int TABLEROW = 3;
	private static final int TABLEYSTART = 250;
	private static final int TABLEYSPACING = 150;
	private static final int TABLECOLUMN = 3;
	private static final int TABLEW = 50;
	private static final int TABLEH = 50;
	private static int numberOfWaiters = 0;

	public Collection<RestaurantZhangTable> tables;
	public RestaurantZhangMenu menu = new RestaurantZhangMenu();
	public RestaurantZhangRevolvingStand orderStand = new RestaurantZhangRevolvingStand();

	public RestaurantZhangHost host;
	private RestaurantZhangCook cook;
	//	public RestaurantZhangMarket market1;
	public RestaurantZhangCashier cashier;
	public Vector<RestaurantZhangCustomer> customers = new Vector<RestaurantZhangCustomer>();
	public Vector<RestaurantZhangWaiter> waiters = new Vector<RestaurantZhangWaiter>();

	Map<Role, Animation> allRoles = new HashMap<Role, Animation>();

	public RestaurantZhangPanel panel; //reference to main gui

	public RestaurantZhangBuilding(String name, RestaurantZhangPanel panel) {
		super(name);
		this.setCustomerRole("city.roles.RestaurantZhangCustomerRole");
		this.setCustomerAnimation("city.animations.RestaurantZhangCustomerAnimation");
		this.panel = panel;
		tables = new ArrayList<RestaurantZhangTable>(nTables);
    	for (int ix = 0; ix < nTables; ix++) {
    		tables.add(new RestaurantZhangTable(ix, TABLEXSTART + ((ix % TABLEROW) * TABLEXSPACING), 
    				TABLEYSTART + ((ix / TABLECOLUMN) * TABLEYSPACING),
    				TABLEW, TABLEH));
    	}
	}

	public Role addRole(Role r) {
		if(r instanceof RestaurantZhangCustomerRole) {
			RestaurantZhangCustomerRole c = (RestaurantZhangCustomerRole)r;
			c.setCashier(cashier);
			c.setHost(host);
			if(!allRoles.containsKey(c)) {
				RestaurantZhangCustomerAnimation anim = new RestaurantZhangCustomerAnimation(c); 
				c.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				customers.add(c);
				allRoles.put(c, anim);
			}
			return c;
		}
		if(r instanceof RestaurantZhangWaiterRegularRole) {
			RestaurantZhangWaiterRegularRole w = (RestaurantZhangWaiterRegularRole)r;
			w.setCashier(cashier);
			w.setCook(cook);
			w.setHost(host);
			w.setMenu(menu);
			host.addWaiter(w);
			if(!allRoles.containsKey(w)) {
				RestaurantZhangWaiterAnimation anim = new RestaurantZhangWaiterAnimation(w, waiters.size() * 30 + 50, 200); 
				w.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				waiters.add(w);
				allRoles.put(w, anim);
			}
			return w;
		}
		if(r instanceof RestaurantZhangWaiterSharedDataRole) {
			RestaurantZhangWaiterRegularRole w = (RestaurantZhangWaiterRegularRole)r;
			w.setCashier(cashier);
			w.setCook(cook);
			w.setHost(host);
			w.setMenu(menu);
			if(!allRoles.containsKey(w)) {
				RestaurantZhangWaiterAnimation anim = new RestaurantZhangWaiterAnimation(w, waiters.size() * 30 + 50, 200); 
				w.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				waiters.add(w);
				allRoles.put(w, anim);
			}
			return w;
		}
		if(r instanceof RestaurantZhangHostRole) {
			RestaurantZhangHostRole h = (RestaurantZhangHostRole)r;
			h.setTables(tables);
			if(!allRoles.containsKey(h)) { 
				host = h;
				allRoles.put(h, null);
			}
			return h;
		}
		if(r instanceof RestaurantZhangCookRole) {
			RestaurantZhangCookRole c = (RestaurantZhangCookRole)r;
			c.setRevolvingStand(orderStand);
			c.setMenuTimes(menu);
			if(!allRoles.containsKey(c)) { 
				RestaurantZhangCookAnimation anim = new RestaurantZhangCookAnimation(c);
				c.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				cook = c;
				allRoles.put(c, anim);
			}
			return c;
		}
		if(r instanceof RestaurantZhangCashierRole) {
			RestaurantZhangCashierRole c = (RestaurantZhangCashierRole)r;
			c.setMenu(menu);
			if(!allRoles.containsKey(c)) { 
				cashier = c;
				allRoles.put(c, null);
			}
			return c;
		}
		return null;
	}

}
