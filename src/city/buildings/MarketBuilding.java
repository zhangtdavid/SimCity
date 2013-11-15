package city.buildings;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import city.Building;
import city.interfaces.MarketCashier;
import city.interfaces.MarketEmployee;

public class MarketBuilding extends Building {
//	MarketManager manager;
	// manager is set through the base class
	MarketCashier cashier;
	public List<MarketEmployee> employees = new ArrayList<MarketEmployee>();
	public Map<String, Integer> inventory = new ConcurrentHashMap<String, Integer>(); // TODO does concurrent hash map make it safer as a public variable?
	public Map<String, Double> prices = new ConcurrentHashMap<String, Double>();
	
	MarketBuilding() {
		super("Market1", null);
		inventory.put("Steak", 50);
		inventory.put("Chicken", 50);
		inventory.put("Salad", 50);
		inventory.put("Pizza", 50);
		
		prices.put("Steak", 15.99);
		prices.put("Chicken", 10.99);
		prices.put("Salad", 5.99);
		prices.put("Pizza", 8.99);
	}
	
	public MarketCashier getCashier() {
		return cashier;
	}
	
	public void setCashier(MarketCashier cashier) {
		this.cashier = cashier;
	}
}
