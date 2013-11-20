package city.interfaces;

import city.animations.interfaces.RestaurantChoiAnimatedHost;
import city.roles.RestaurantChoiTable;

public interface RestaurantChoiHost extends RoleInterface{
	public abstract void msgImHungry(RestaurantChoiCustomer c);
	public abstract void msgImBack(RestaurantChoiWaiter w);
	public abstract void msgIWantABreak(RestaurantChoiWaiter w);
	public abstract void msgNotWaiting(RestaurantChoiCustomer c);
	public abstract void msgTablesClear(RestaurantChoiWaiter w, RestaurantChoiTable table);
	public abstract void findLeastActiveWaiter();
	public abstract void addWaiter(RestaurantChoiWaiter w);
	public abstract void waiterBreak(int i);
	public abstract void assignTable(int i);
	public abstract void setAnimation(RestaurantChoiAnimatedHost in);
	public abstract void plus1Workload(RestaurantChoiWaiter w);
	public abstract void minus1Workload(RestaurantChoiWaiter w);
	public abstract RestaurantChoiAnimatedHost getAnimation();
	public abstract boolean runScheduler();
}
