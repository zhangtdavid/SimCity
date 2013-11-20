package city.buildings;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import city.Building;
import city.interfaces.MarketCashier;
import city.interfaces.MarketDeliveryPerson;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;

public class MarketBuilding extends Building {
	MarketManager manager;
	MarketCashier cashier;
	public List<MarketEmployee> employees = new ArrayList<MarketEmployee>();
	public List<MarketDeliveryPerson> deliveryPeople = new ArrayList<MarketDeliveryPerson>();
	public Map<String, Integer> inventory = new ConcurrentHashMap<String, Integer>(); // TODO does concurrent hash map make it safer as a public variable?
	public Map<String, Double> prices = new ConcurrentHashMap<String, Double>();
	public double money;
	
	public MarketBuilding(String name) {
		super(name);
		
		// initializes all items in the inventory to 50
		inventory.put("Steak", 50);
		inventory.put("Chicken", 50);
		inventory.put("Salad", 50);
		inventory.put("Pizza", 50);
		
		// initializes prices
		prices.put("Steak", (16.00)/2);
		prices.put("Chicken", (12.00)/2);
		prices.put("Salad", (6.00)/2);
		prices.put("Pizza", (10.00)/2);
		
		money = 1000.0;
	}
	
	
// Utilities
//	=====================================================================	
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
	
	// Employees

	// TODO should there be a getEmployee?
	
	// Delivery People

	// TODO should there be a getDeliveryPerson?
}
