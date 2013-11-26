package city.buildings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import city.Application.FOOD_ITEMS;
import city.Building;
import city.Role;
import city.gui.MarketPanel;
import city.interfaces.BankCustomer;
import city.interfaces.MarketCashier;
import city.interfaces.MarketDeliveryPerson;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;

public class MarketBuilding extends Building { 
	
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
		
		super.setCash(1000);
	}
	
	
// Utilities
//	=====================================================================	
	
	@Override
	public void addRole(Role r) {
		// TODO
		return;
	}
	
	// Employee
	public void addEmployee(MarketEmployee employee) {
		employees.add(employee);
		manager.msgNewEmployee(employee);
		
	}
	
	public void removeEmployee(MarketEmployee employee) {
		employees.remove(employee);
		manager.msgRemoveEmployee(employee);		
	}
	
	// Delivery Person
	public void addDeliveryPerson(MarketDeliveryPerson deliveryPerson) {
		deliveryPeople.add(deliveryPerson);
		cashier.msgNewDeliveryPerson(deliveryPerson);
	}
	
	public void removeDeliveryPerson(MarketDeliveryPerson deliveryPerson) {
		deliveryPeople.remove(deliveryPerson);
		cashier.msgRemoveDeliveryPerson(deliveryPerson);		
	}
	
//  Getters and Setters
//	=====================================================================	
	// Manager
	public MarketManager getManager() {
		return manager;
	}
	
	public void setManager(MarketManager manager) {
		this.manager = manager;
	}
	
	// Cashier
	public MarketCashier getCashier() {
		return cashier;
	}
	
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
