package utilities;

import java.util.ArrayList;
import java.util.List;

public class RestaurantChungRevolvingStand {
	List<RestaurantChungOrder> orderList = new ArrayList<RestaurantChungOrder>();
	private int count = 0;

	public RestaurantChungRevolvingStand() { }

	synchronized public void addOrder(RestaurantChungOrder o) {
		orderList.add(o);
		count++;
	}

	synchronized public RestaurantChungOrder remove() {
		if(count <= 0) {
			return null;
		}
		count--;
		return orderList.remove(0);
	}
}