package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import city.Application;
import city.Application.BUILDING;
import city.Application.FOOD_ITEMS;
import city.animations.MarketCashierAnimation;
import city.animations.MarketCustomerAnimation;
import city.animations.MarketEmployeeAnimation;
import city.animations.interfaces.MarketAnimatedCashier;
import city.animations.interfaces.MarketAnimatedEmployee;
import city.bases.Building;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Bank;
import city.buildings.interfaces.Market;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.MarketPanel;
import city.roles.BankCustomerRole;
import city.roles.MarketCashierRole;
import city.roles.MarketCustomerRole;
import city.roles.MarketDeliveryPersonRole;
import city.roles.MarketEmployeeRole;
import city.roles.MarketManagerRole;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.MarketCashier;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketDeliveryPerson;
import city.roles.interfaces.MarketEmployee;
import city.roles.interfaces.MarketManager;

public class MarketBuilding extends Building implements Market { 
//	Data
//	=====================================================================
	private MarketManager manager;
	private MarketCashier cashier;
	private BankCustomer bankCustomer;
	private List<MyMarketEmployee> employees;
	private List<MyDeliveryPerson> deliveryPeople;
	private List<MyMarketCustomer> customers;
	
	public static final int WORKER_SALARY = 500;
	
	private Map<FOOD_ITEMS, Integer> inventory = new ConcurrentHashMap<FOOD_ITEMS, Integer>();
	private Map<FOOD_ITEMS, Integer> prices = new ConcurrentHashMap<FOOD_ITEMS, Integer>();
	
	private int currentDeliveryPerson;

	
//	Constructor
//	=====================================================================	
	public MarketBuilding(String name, MarketPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		this.setCustomerRoleName("city.roles.MarketCustomerRole");
		this.setCustomerAnimationName("city.animations.MarketCustomerAnimation");
		
		employees = Collections.synchronizedList(new ArrayList<MyMarketEmployee>());
		deliveryPeople = Collections.synchronizedList(new ArrayList<MyDeliveryPerson>());
		customers = Collections.synchronizedList(new ArrayList<MyMarketCustomer>());

		// initializes all items in the inventory to 50
		inventory.put(FOOD_ITEMS.chicken, 50);
		inventory.put(FOOD_ITEMS.pizza, 50);
		inventory.put(FOOD_ITEMS.salad, 50);
		inventory.put(FOOD_ITEMS.steak, 50);
		
		// initializes prices
		prices.put(FOOD_ITEMS.chicken, (12)/2);
		prices.put(FOOD_ITEMS.pizza, (10)/2);
		prices.put(FOOD_ITEMS.salad, (6)/2);
		prices.put(FOOD_ITEMS.steak, (16)/2);

		bankCustomer = new BankCustomerRole(this, (Bank)(Application.CityMap.findRandomBuilding(BUILDING.bank)));
		bankCustomer.setActive();
		
		super.setCash(1000);
		
		this.addWorkerRoleName("city.roles.MarketCashierRole");
		this.addWorkerRoleName("city.roles.MarketDeliveryPersonRole");
		this.addWorkerRoleName("city.roles.MarketEmployeeRole");
		this.addWorkerRoleName("city.roles.MarketManagerRole");
		this.setBuildingClassName("city.buildings.interfaces.Market");
	}

//  Getters
//	=====================================================================	
	
	@Override
	public boolean getBusinessIsOpen() {
		boolean disposition = true;
		
		if (manager == null)
			disposition = false;
		if (cashier == null)
			disposition = false;
		if (employees.size() == 0)
			disposition = false;
		if (deliveryPeople.size() == 0)
			disposition = false;

		return disposition;
	}
	
	@Override
	public MarketManager getManager() {
		return manager;
	}

	@Override
	public MarketCashier getCashier() {
		return cashier;
	}
	
	@Override
	public MarketPanel getMarketPanel() {
		return (MarketPanel)(super.getPanel());
	}
	
	@Override
	public MarketManager getMarketManager() {
		return manager;
	}
	
	@Override
	public MarketCashier getMarketCashier() {
		return cashier;
	}
	
	@Override
	public BankCustomer getBankCustomer() {
		return bankCustomer;
	}
	
	@Override
	public List<MyMarketEmployee> getEmployees() {
		return employees;
	}
	
	@Override
	public List<MyDeliveryPerson> getDeliveryPeople() {
		return deliveryPeople;
	}
	
	@Override
	public List<MyMarketCustomer> getCustomers() {
		return customers;
	}
	
	@Override
	public Map<FOOD_ITEMS, Integer> getInventory() {
		return inventory;
	}
	
	@Override
	public Map<FOOD_ITEMS, Integer> getPrices() {
		return prices;
	}	
	
//  Setters
//	=====================================================================	
	@Override
	public void setManager(MarketManager manager) {
		this.manager = manager;
	}
	
	@Override
	public void setCashier(MarketCashier cashier) {
		this.cashier = cashier;
	}
	
	@Override
	public void setInventory(Map<FOOD_ITEMS, Integer> map){
		inventory = map;
	}
	
//	Utilities
//	=====================================================================
	@Override
	public void addOccupyingRole(RoleInterface r) {		
		if(r instanceof MarketManagerRole) {
			MarketManagerRole m = (MarketManagerRole)r;
			
			if(!super.occupyingRoleExists(m)) {
				manager = m;
				super.addOccupyingRole(m, null); // null --> anim
			}			
		}
		if(r instanceof MarketCashierRole) {
			MarketCashierRole c = (MarketCashierRole)r;
			
			if(!super.occupyingRoleExists(c)) {
				MarketAnimatedCashier anim = new MarketCashierAnimation(c); 
				c.setAnimation(anim);	
				anim.setVisible(true);
				this.getPanel().addVisualizationElement(anim);
				cashier = c;
				super.addOccupyingRole(c, anim);
			}
		}
		if(r instanceof MarketEmployeeRole) {
			MarketEmployeeRole e = (MarketEmployeeRole)r;
			
			if(!super.occupyingRoleExists(e)) {
				MarketAnimatedEmployee anim = new MarketEmployeeAnimation(e); 
				e.setAnimation(anim);
				anim.setVisible(true);
				this.getPanel().addVisualizationElement(anim);
				this.addEmployee(e);
				//e.setActive();
				super.addOccupyingRole(e, anim);
			}
		}
		if(r instanceof MarketDeliveryPersonRole) {
			MarketDeliveryPersonRole m = (MarketDeliveryPersonRole)r;
			
			if(!super.occupyingRoleExists(m)) {
				this.addDeliveryPerson(m);
				super.addOccupyingRole(m, null); // null --> anim
			}
		}
		if(r instanceof MarketCustomerRole) {
			MarketCustomerRole c = (MarketCustomerRole)r;
			if(!super.occupyingRoleExists(c)) {
				MarketCustomerAnimation anim = new MarketCustomerAnimation(c); 
				c.setAnimation(anim);
				anim.setVisible(true);
				this.getPanel().addVisualizationElement(anim);
				super.addOccupyingRole(c, anim);
			}
		}
	}
	
	// Add
	@Override
	public void addEmployee(MarketEmployee e) {
		employees.add(new MyMarketEmployee(e));
	}
	
	@Override
	public void addDeliveryPerson(MarketDeliveryPerson d) {
		deliveryPeople.add(new MyDeliveryPerson(d));
	}
	
	// Remove
	@Override
	public void removeEmployee(MarketEmployee e) {
		MyMarketEmployee me = findEmployee(e);
		employees.remove(me);	
	}
	
	@Override
	public void removeDeliveryPerson(MarketDeliveryPerson d) {
		MyDeliveryPerson dp = findDeliveryPerson(d);
		deliveryPeople.remove(dp);
	}
	
	// Find
	@Override
	public MyMarketEmployee findEmployee(MarketEmployee me) {
		for(MyMarketEmployee e : employees ){
			if(e.getEmployee() == me) {
				return e;
			}
		}
		return null;
	}
	
	@Override
	public MyDeliveryPerson findDeliveryPerson(MarketDeliveryPerson d) {
		for(MyDeliveryPerson t : deliveryPeople){
			if(t.getDeliveryPerson() == d) {
				return t;		
			}
		}
		return null;
	}
	
	@Override
	public MyMarketCustomer findCustomerDelivery(MarketCustomerDelivery cd) {
		for(MyMarketCustomer c : customers ){
			if(c.getCustomerDelivery() == cd) {
				return c;		
			}
		}
		return null;
	}

	@Override
	public int getCurrentDeliveryPerson() {
		return currentDeliveryPerson;
	}

	@Override
	public void setCurrentDeliveryPerson(int currentDeliveryPerson) {
		this.currentDeliveryPerson = currentDeliveryPerson;
	}
}
