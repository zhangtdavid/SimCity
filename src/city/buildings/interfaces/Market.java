package city.buildings.interfaces;

import java.util.List;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.bases.interfaces.BuildingInterface;
import city.gui.interiors.MarketPanel;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.MarketCashier;
import city.roles.interfaces.MarketDeliveryPerson;
import city.roles.interfaces.MarketEmployee;
import city.roles.interfaces.MarketManager;

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