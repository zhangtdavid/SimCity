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
import city.animations.interfaces.MarketAnimatedCashier;
import city.bases.Building;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Bank;
import city.buildings.interfaces.Market;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.MarketPanel;
import city.roles.BankCustomerRole;
import city.roles.MarketCashierRole;
import city.roles.MarketEmployeeRole;
import city.roles.MarketManagerRole;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.MarketCashier;
import city.roles.interfaces.MarketDeliveryPerson;
import city.roles.interfaces.MarketEmployee;
import city.roles.interfaces.MarketManager;

public class MarketBuilding extends Building implements Market { 
//	Data
//	=====================================================================
	private MarketManager manager;
	private MarketCashier cashier;
	private BankCustomer bankCustomer;
	private List<MarketEmployee> employees = new ArrayList<MarketEmployee>();
	private List<MyDeliveryPerson> deliveryPeople;
	
	public static final int WORKER_SALARY = 500;
	
	private Map<FOOD_ITEMS, Integer> inventory = new ConcurrentHashMap<FOOD_ITEMS, Integer>(); // TODO does concurrent hash map make it safer as a public variable?
	private Map<FOOD_ITEMS, Integer> prices = new ConcurrentHashMap<FOOD_ITEMS, Integer>();
	
//	Constructor
//	=====================================================================	
	public MarketBuilding(String name, MarketPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		this.setCustomerRoleName("city.roles.MarketCustomerRole");
		this.setCustomerAnimationName("city.animations.MarketCustomerAnimation");
		
		deliveryPeople = Collections.synchronizedList(new ArrayList<MyDeliveryPerson>());

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
	public List<MarketEmployee> getEmployees() {
		return employees;
	}
	
	@Override
	public List<MyDeliveryPerson> getDeliveryPeople() {
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
				manager = m;
//				m.setActive();
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
//				c.setActive();
				super.addOccupyingRole(c, null); // null --> anim
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
	
	// Add
	@Override
	public void addEmployee(MarketEmployee employee) {
		employees.add(employee);
		manager.msgNewEmployee(employee);
	}
	
	@Override
	public void addDeliveryPerson(MarketDeliveryPerson d) {
		deliveryPeople.add(new MyDeliveryPerson(d));
	}
	
	// Remove
	@Override
	public void removeEmployee(MarketEmployee employee) {
		employees.remove(employee);
		manager.msgRemoveEmployee(employee);		
	}
	
	@Override
	public void removeDeliveryPerson(MarketDeliveryPerson d) {
		MyDeliveryPerson dp = findDeliveryPerson(d);
		deliveryPeople.remove(dp);
	}
	
	// Find
	@Override
	public MyDeliveryPerson findDeliveryPerson(MarketDeliveryPerson d) {
		for(MyDeliveryPerson t : deliveryPeople){
			if(t.getDeliveryPerson() == d) {
				return t;		
			}
		}
		return null;
	}
}
