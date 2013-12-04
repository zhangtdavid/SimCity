package city.buildings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
//	Data
//	=====================================================================
	private MarketPanel panel;	
	private MarketManager manager;
	private MarketCashier cashier;
	private BankCustomer bankCustomer;
	private List<MarketEmployee> employees = new ArrayList<MarketEmployee>();
	private List<MarketDeliveryPerson> deliveryPeople = new ArrayList<MarketDeliveryPerson>();
	
	public static final int WORKER_SALARY = 500;
	
	private Map<FOOD_ITEMS, Integer> inventory = new ConcurrentHashMap<FOOD_ITEMS, Integer>(); // TODO does concurrent hash map make it safer as a public variable?
	private Map<FOOD_ITEMS, Integer> prices = new ConcurrentHashMap<FOOD_ITEMS, Integer>();
	
//	Constructor
//	=====================================================================	
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
		
		bankCustomer = new BankCustomerRole();
		
		super.setCash(1000);
	}

//  Getters
//	=====================================================================	
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
		return panel;
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
	public List<MarketEmployee> getEmployees() {
		return employees;
	}
	
	@Override
	public List<MarketDeliveryPerson> getDeliveryPeople() {
		return deliveryPeople;
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
	
//	Utilities
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
}
