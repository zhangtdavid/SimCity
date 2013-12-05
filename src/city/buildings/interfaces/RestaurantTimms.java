package city.buildings.interfaces;

import java.util.List;

import city.bases.interfaces.RestaurantBuildingInterface;
import city.buildings.RestaurantTimmsBuilding.Check;
import city.buildings.RestaurantTimmsBuilding.InternalMarketOrder;
import city.buildings.RestaurantTimmsBuilding.MenuItem;
import city.buildings.RestaurantTimmsBuilding.Order;
import city.buildings.RestaurantTimmsBuilding.Table;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.RestaurantTimmsCashier;
import city.roles.interfaces.RestaurantTimmsCook;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.roles.interfaces.RestaurantTimmsHost;
import city.roles.interfaces.RestaurantTimmsWaiter;

public interface RestaurantTimms extends RestaurantBuildingInterface {
	
	// Data

	public static final int WORKER_SALARY = 200;
	
	// Getters

	public RestaurantTimmsCashier getCashier();
	public RestaurantTimmsCook getCook();
	public List<RestaurantTimmsCustomer> getWaitingCustomers();
	public RestaurantTimmsCustomer getCustomer(RestaurantTimmsHost h, RestaurantTimmsWaiter w);
	public RestaurantTimmsHost getHost();
	public List<RestaurantTimmsWaiter> getWaiters();
	public RestaurantTimmsWaiter getWaiter();
	public int getWaiterIndex(RestaurantTimmsWaiter w);
	public List<Table> getTables();
	public List<MenuItem> getMenuItems();
	public List<Check> getChecks();
	public List<Order> getOrders();
	public BankCustomer getBankCustomer();

	// Setters
	
	public void setCashier(RestaurantTimmsCashier c);
	public void setCook(RestaurantTimmsCook c);
	public void setHost(RestaurantTimmsHost h);
	public void setBankCustomer(BankCustomer b);
	
	// Utilities
	
	public void addCustomer(RestaurantTimmsCustomer c, RestaurantTimmsHost h);
	public void updateCustomer(RestaurantTimmsCustomer c, RestaurantTimmsHost h, RestaurantTimmsWaiter w);
	public void removeCustomer(RestaurantTimmsCustomer c);
	public void addWaiter(RestaurantTimmsWaiter w);
	public void addMarketOrder(InternalMarketOrder o);
	public void addCheck(Check c);
	public void addOrder(Order o);
	public void removeOrder(Order o);

}