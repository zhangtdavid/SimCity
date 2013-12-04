package city.interfaces;

import city.BuildingInterface;

public interface Market extends BuildingInterface {

	// Employee
	public void addEmployee(MarketEmployee employee);

	public void removeEmployee(MarketEmployee employee);

	// Delivery Person
	public void addDeliveryPerson(MarketDeliveryPerson deliveryPerson);

	public void removeDeliveryPerson(MarketDeliveryPerson deliveryPerson);

	//  Getters and Setters
	//	=====================================================================	
	// Manager
	public MarketManager getManager();

	public void setManager(MarketManager manager);

	// Cashier
	public MarketCashier getCashier();

	public void setCashier(MarketCashier cashier);

}