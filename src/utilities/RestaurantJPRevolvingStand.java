package utilities;

import java.util.ArrayList;
import java.util.List;

import city.roles.RestaurantJPCookRole.Order;

public class RestaurantJPRevolvingStand {
	List<Order> OrderList = new ArrayList<Order>();
	private int count = 0;

	public RestaurantJPRevolvingStand() { }

	synchronized public void addOrder(Order o) {
		OrderList.add(o);
		count++;
	}

	synchronized public Order remove() {
		if(count <= 0) {
			return null;
		}
		count--;
		return OrderList.remove(0);
	}
}
