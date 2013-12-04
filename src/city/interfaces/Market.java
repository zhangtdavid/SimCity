package city.interfaces;

import java.util.List;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.BuildingInterface;
import city.gui.buildings.MarketPanel;

public interface Market extends BuildingInterface {
	// Employee
	public void addEmployee(MarketEmployee employee);
	public void removeEmployee(MarketEmployee employee);

	// Delivery Person
	public void addDeliveryPerson(MarketDeliveryPerson deliveryPerson);
	public void removeDeliveryPerson(MarketDeliveryPerson deliveryPerson);

	//  Getters
	public MarketPanel getMarketPanel();
	public MarketManager getMarketManager();
	public MarketCashier getMarketCashier();
	public MarketManager getManager();
	public MarketCashier getCashier();
	public BankCustomer getBankCustomer();
	public List<MarketEmployee> getEmployees();
	public List<MarketDeliveryPerson> getDeliveryPeople();
	
	//	Setters
	public void setManager(MarketManager manager);
	public void setCashier(MarketCashier cashier);
	Map<FOOD_ITEMS, Integer> getInventory();
	Map<FOOD_ITEMS, Integer> getPrices();

}