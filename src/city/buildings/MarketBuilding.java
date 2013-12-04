package city.buildings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import city.Application.BUILDING;
import city.Application.CityMap;
import city.Application.FOOD_ITEMS;
import city.Building;
import city.RoleInterface;
import city.gui.buildings.MarketPanel;
import city.interfaces.BankCustomer;
import city.interfaces.Market;
import city.interfaces.MarketCashier;
import city.interfaces.MarketDeliveryPerson;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;
import city.roles.BankCustomerRole;
import city.roles.MarketCashierRole;
import city.roles.MarketEmployeeRole;
import city.roles.MarketManagerRole;

public class MarketBuilding extends Building implements Market { 
	
	// Data
	public MarketPanel panel;	
	public MarketManager manager;
	public MarketCashier cashier;
	public BankCustomer bankCustomer;
	public List<MarketEmployee> employees = new ArrayList<MarketEmployee>();
	public List<MarketDeliveryPerson> deliveryPeople = new ArrayList<MarketDeliveryPerson>();
	
	private static final int WORKER_SALARY = 500;
	
	public Map<FOOD_ITEMS, Integer> inventory = new ConcurrentHashMap<FOOD_ITEMS, Integer>(); // TODO does concurrent hash map make it safer as a public variable?
	public Map<FOOD_ITEMS, Integer> prices = new ConcurrentHashMap<FOOD_ITEMS, Integer>();
	
	// Constructor
	
	public MarketBuilding(String name, MarketPanel panel) {
		super(name);
		this.setCustomerRoleName("city.roles.MarketCustomerRole");
		this.setCustomerAnimationName("city.animations.MarketCustomerAnimation");
		this.panel = panel;
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
		
		bankCustomer = new BankCustomerRole(this, (BankBuilding)CityMap.findRandomBuilding(BUILDING.bank));
		
		super.setCash(1000);
	}
	
	
// Utilities
//	=====================================================================	
	
	@Override
	public void addOccupyingRole(RoleInterface r) {
		if(r instanceof MarketManagerRole) {
			MarketManagerRole m = (MarketManagerRole)r;
			
			if(!super.occupyingRoleExists(m)) {
				//MarketManagerAnimation anim = new MarketManagerAnimation(m); 
				//c.setGui(anim);	
//				c.setAnimation(anim);
				//anim.setVisible(true);
				//panel.addVisualizationElement(anim);
				this.manager = m;
				m.setActive();
				super.addOccupyingRole(m, null); // null --> anim
			}
		}
		if(r instanceof MarketCashierRole) {
			MarketCashierRole m = (MarketCashierRole)r;
			
			if(!super.occupyingRoleExists(m)) {
				//RestaurantChoiCustomerAnimation anim = new RestaurantChoiCustomerAnimation(c); 
				//c.setGui(anim);	
//				c.setAnimation(anim);
				//anim.setVisible(true);
				//panel.addVisualizationElement(anim);
				this.cashier = m;
				m.setActive();
				super.addOccupyingRole(m, null); // null --> anim
			}
		}
		if(r instanceof MarketEmployeeRole) {
			MarketEmployeeRole m = (MarketEmployeeRole)r;
			
			if(!super.occupyingRoleExists(m)) {
				//RestaurantChoiCustomerAnimation anim = new RestaurantChoiCustomerAnimation(c); 
				//c.setGui(anim);	
//				c.setAnimation(anim);
				//anim.setVisible(true);
				//panel.addVisualizationElement(anim);
				this.addEmployee(m);
				m.setActive();
				super.addOccupyingRole(m, null); // null --> anim
			}
		}
	}
	
	// Employee
	@Override
	public void addEmployee(MarketEmployee employee) {
		employees.add(employee);
		manager.msgNewEmployee(employee);
	}
	
	@Override
	public void removeEmployee(MarketEmployee employee) {
		employees.remove(employee);
		manager.msgRemoveEmployee(employee);		
	}
	
	// Delivery Person
	@Override
	public void addDeliveryPerson(MarketDeliveryPerson deliveryPerson) {
		deliveryPeople.add(deliveryPerson);
		cashier.msgNewDeliveryPerson(deliveryPerson);
	}
	
	@Override
	public void removeDeliveryPerson(MarketDeliveryPerson deliveryPerson) {
		deliveryPeople.remove(deliveryPerson);
		cashier.msgRemoveDeliveryPerson(deliveryPerson);		
	}
	
//  Getters and Setters
//	=====================================================================	
	// Manager
	@Override
	public MarketManager getManager() {
		return manager;
	}
	
	@Override
	public void setManager(MarketManager manager) {
		this.manager = manager;
	}
	
	// Cashier
	@Override
	public MarketCashier getCashier() {
		return cashier;
	}
	
	@Override
	public void setCashier(MarketCashier cashier) {
		this.cashier = cashier;
	}


	public static int getWorkerSalary() {
		return WORKER_SALARY;
	}
	
	// Employees

	// TODO should there be a getEmployee?
	
	// Delivery People

	// TODO should there be a getDeliveryPerson?
}
