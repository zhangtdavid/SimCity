package city.interfaces;

import java.util.List;

import city.abstracts.RestaurantBuildingInterface;
import city.animations.RestaurantChungWaiterAnimation;
import city.gui.buildings.RestaurantChungPanel;

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