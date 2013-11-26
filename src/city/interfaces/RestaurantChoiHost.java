package city.interfaces;

import utilities.RestaurantChoiTable;
import city.animations.interfaces.RestaurantChoiAnimatedHost;

public interface RestaurantChoiHost extends RoleInterface{
	public abstract void msgImHungry(RestaurantChoiCustomer c);
	public abstract void msgImBack(RestaurantChoiWaiterAbs w);
	public abstract void msgIWantABreak(RestaurantChoiWaiterAbs w);
	public abstract void msgNotWaiting(RestaurantChoiCustomer c);
	public abstract void msgTablesClear(RestaurantChoiWaiterAbs w, RestaurantChoiTable table);
	public abstract void findLeastActiveWaiter();
	public abstract void addWaiter(RestaurantChoiWaiterAbs w);
	public abstract void waiterBreak(int i);
	public abstract void assignTable(int i);
	public abstract void setAnimation(RestaurantChoiAnimatedHost in);
	public abstract void plus1Workload(RestaurantChoiWaiterAbs w);
	public abstract void minus1Workload(RestaurantChoiWaiterAbs w);
	public abstract RestaurantChoiAnimatedHost getAnimation();
	public abstract boolean runScheduler();
}
