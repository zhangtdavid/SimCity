package city.buildings.interfaces;

import java.util.List;

import city.animations.RestaurantChungWaiterAnimation;
import city.bases.interfaces.RestaurantBuildingInterface;
import city.gui.interiors.RestaurantChungPanel;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.RestaurantChungCashier;
import city.roles.interfaces.RestaurantChungCook;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungHost;
import city.roles.interfaces.RestaurantChungWaiter;

public interface RestaurantChung extends RestaurantBuildingInterface {

	//	Getters
	public RestaurantChungPanel getRestaurantChungPanel();
	public RestaurantChungHost getRestaurantChungHost();
	public RestaurantChungCashier getRestaurantChungCashier();
	public RestaurantChungCook getRestaurantChungCook();
	public BankCustomer getBankCustomer();
	public List<RestaurantChungWaiter> getWaiters();
	public List<RestaurantChungCustomer> getCustomers();

	//	Setters
	public void setRestaurantChungHost(RestaurantChungHost host);
	public void setRestaurantChungCashier(RestaurantChungCashier cashier);
	public void setRestaurantChungCook(RestaurantChungCook cook);
	
	//	Utilities
	public void addWaiter(RestaurantChungWaiter w, RestaurantChungWaiterAnimation anim);
	public void removeWaiter(RestaurantChungWaiter waiter);

}