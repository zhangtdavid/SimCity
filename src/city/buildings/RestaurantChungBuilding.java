package city.buildings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import utilities.RestaurantChungRevolvingStand;
import city.Application;
import city.Application.BUILDING;
import city.animations.RestaurantChungCashierAnimation;
import city.animations.RestaurantChungCookAnimation;
import city.animations.RestaurantChungCustomerAnimation;
import city.animations.RestaurantChungWaiterAnimation;
import city.animations.interfaces.RestaurantChungAnimatedCustomer;
import city.bases.RestaurantBuilding;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Bank;
import city.buildings.interfaces.RestaurantChung;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.RestaurantChungPanel;
import city.roles.BankCustomerRole;
import city.roles.RestaurantChungCashierRole;
import city.roles.RestaurantChungCookRole;
import city.roles.RestaurantChungCustomerRole;
import city.roles.RestaurantChungHostRole;
import city.roles.RestaurantChungWaiterMessageCookRole;
import city.roles.RestaurantChungWaiterRevolvingStandRole;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.RestaurantChungCashier;
import city.roles.interfaces.RestaurantChungCook;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungHost;
import city.roles.interfaces.RestaurantChungWaiter;

public class RestaurantChungBuilding extends RestaurantBuilding implements RestaurantChung {
//	Data
//	=====================================================================
	private RestaurantChungHost host;
	private RestaurantChungCashier cashier;
	private RestaurantChungCook cook;
	private BankCustomer bankCustomer;
	private List<MyWaiter> waiters;
	private List<MyCustomer> customers;
	private Collection<Table> tables;
	
	private RestaurantChungRevolvingStand orderStand;
	
	private int numWaitingCustomers = 0; // Used to keep track of customers' positions in line
	
	public static final int WORKER_SALARY = 500;
	public static final int NUM_TABLES = 4;
	
//	Constructor
//	=====================================================================
	public RestaurantChungBuilding(String name, RestaurantChungPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		this.setCustomerRoleName("city.roles.RestaurantChungCustomerRole");
		this.setCustomerAnimationName("city.animations.RestaurantChungCustomerAnimation");
		orderStand = new RestaurantChungRevolvingStand();
		bankCustomer = new BankCustomerRole(this, (Bank)(Application.CityMap.findRandomBuilding(BUILDING.bank)));
		waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
		customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
		
		// make some tables
		tables = new Vector<Table>(NUM_TABLES);
		for (int i = 1; i < NUM_TABLES+1; i++) {
			tables.add(new Table(i));
		}
	}
	
//	Getters
//	=====================================================================	
	
	@Override
	public boolean getBusinessIsOpen() {
		boolean disposition = true;
		
		if (host == null)
			disposition = false;
		if (cashier == null)
			disposition = false;
		if (cook == null)
			disposition = false;
		if (waiters.size() == 0)
			disposition = false;

		return disposition;
	}
	
	@Override
	public RestaurantChungPanel getRestaurantChungPanel() {
		return (RestaurantChungPanel)(super.getPanel());
	}
	
	@Override
	public RestaurantChungHost getRestaurantChungHost() {
		return host;
	}
	
	@Override
	public RestaurantChungCashier getRestaurantChungCashier() {
		return cashier;
	}
	
	@Override
	public RestaurantChungCook getRestaurantChungCook() {
		return cook;
	}
	
	@Override
	public BankCustomer getBankCustomer() {
		return bankCustomer;
	}
	
	@Override
	public List<MyWaiter> getWaiters() {
		return waiters;
	}
	
	@Override
	public List<MyCustomer> getCustomers() {
		return customers;
	}

	@Override
	public Collection<Table> getTables() {
		return tables;
	}
	
	@Override
	public RestaurantChungRevolvingStand getOrderStand() {
		return orderStand;
	}
	
	@Override
	public int getNumWaitingCustomers() {
		return numWaitingCustomers;
	}
	
//	Setters
//	=====================================================================	
	//Only used in testing, usually set in addOccupyingRole() call
	@Override
	public void setRestaurantChungHost(RestaurantChungHost host) {
		this.host = host;
	}
	
	@Override
	public void setRestaurantChungCashier(RestaurantChungCashier cashier) {
		this.cashier = cashier;
	}
	
	@Override
	public void setRestaurantChungCook(RestaurantChungCook cook) {
		this.cook = cook;
	}
	
	@Override
	public void setNumWaitingCustomers(int num) {
		numWaitingCustomers = num;
	}
		
//	Utilities
//	=====================================================================	
	@Override
	public void addOccupyingRole(RoleInterface r) {
		if(r instanceof RestaurantChungCustomerRole) {
			RestaurantChungCustomerRole c = (RestaurantChungCustomerRole)r;
			if(!super.occupyingRoleExists(c)) {
				RestaurantChungAnimatedCustomer anim = new RestaurantChungCustomerAnimation(c); 
				c.setAnimation(anim);
				c.setRestaurant(this);
				anim.setVisible(true); // TODO set this in setActive()
				getPanel().addVisualizationElement(anim);
//				customers.add(new MyCustomer(c));
				super.addOccupyingRole(c, anim);
				c.setActive();
				host.msgIWantToEat(c);
			}
		}
		if(r instanceof RestaurantChungWaiterMessageCookRole) {
			RestaurantChungWaiterMessageCookRole w = (RestaurantChungWaiterMessageCookRole)r;
			if(!super.occupyingRoleExists(w)) {
				RestaurantChungWaiterAnimation anim = new RestaurantChungWaiterAnimation(w); 
				addWaiter(w, anim);
				super.addOccupyingRole(w, anim);
			}
		}
		if(r instanceof RestaurantChungWaiterRevolvingStandRole) {
			RestaurantChungWaiterRevolvingStandRole w = (RestaurantChungWaiterRevolvingStandRole)r;
			if(!super.occupyingRoleExists(w)) {
				RestaurantChungWaiterAnimation anim = new RestaurantChungWaiterAnimation(w);
				addWaiter(w, anim);
				super.addOccupyingRole(w, anim);
			}
		}
		if(r instanceof RestaurantChungHostRole) {
			RestaurantChungHostRole h = (RestaurantChungHostRole)r;
			if(!super.occupyingRoleExists(h)) { 
				host = h;
				super.addOccupyingRole(h, null);
				h.setActive();
			}
		}
		if(r instanceof RestaurantChungCookRole) {
			RestaurantChungCookRole c = (RestaurantChungCookRole)r;
			if(!super.occupyingRoleExists(c)) {
				RestaurantChungCookAnimation anim = new RestaurantChungCookAnimation(c);
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				getPanel().addVisualizationElement(anim);
				cook = c;
				super.addOccupyingRole(c, anim);
				c.setActive();
			}
		}
		if(r instanceof RestaurantChungCashierRole) {
			RestaurantChungCashierRole c = (RestaurantChungCashierRole)r;
			if(!super.occupyingRoleExists(c)) {
				RestaurantChungCashierAnimation anim = new RestaurantChungCashierAnimation(c); 
				c.setAnimation(anim);
				anim.setVisible(true); // TODO set this in setActive()
				getPanel().addVisualizationElement(anim);
				cashier = c;
				super.addOccupyingRole(c, anim);
				c.setActive();
			}
		}
	}
	
	// Add
	@Override
	public void addWaiter(RestaurantChungWaiter w, RestaurantChungWaiterAnimation anim) {
		waiters.add(new MyWaiter(w));
		w.setAnimation(anim);
		anim.setVisible(true);
		getPanel().addVisualizationElement(anim);
    	for (int i = 0; i < 9; i++) {
    		anim.addTable(RestaurantChungPanel.TABLEX+((i%3)*RestaurantChungPanel.TABLEGAP), RestaurantChungPanel.TABLEY+((i/3)*RestaurantChungPanel.TABLEGAP));
    	}
    	w.setActive();
	}
	
	// Remove
	@Override
	public void removeWaiter(RestaurantChungWaiter w) {
		MyWaiter mw = findWaiter(w);
		waiters.remove(mw);	
	}
	
	@Override
	public void removeCustomerFromList(MyCustomer c) {
		for(int i = 0; i < customers.size(); i ++) {
			if(customers.get(i) == c) {
				customers.remove(c);
			}
		}
	}
	
	// Find
	@Override
	public MyWaiter findWaiter(RestaurantChungWaiter w) {
		for(MyWaiter waiter : waiters ){
			if(waiter.getRestaurantChungWaiter() == w) {
				return waiter;
			}
		}
		return null;
	}
	
	@Override
	public MyCustomer findCustomer(RestaurantChungCustomer ca) {
		for(MyCustomer customer : customers ){
			if(customer.getRestaurantChungCustomer() == ca) {
				return customer;		
			}
		}
		return null;
	}
	
	@Override
	// Find customer seated at table
	public MyCustomer findCustomer(int t) {
		for(MyCustomer customer : customers ){
			if(customer.getTable() == t) {
				return customer;		
			}
		}
		return null;
	}
	
	@Override
	public Table findTable(int t) {
		for (Table table : tables) {
			if (table.getTableNumber() == t) {
				return table;
			}
		}
		return null;
	}
}
