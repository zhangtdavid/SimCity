package utilities;

import java.util.ArrayList;
import java.util.List;

public class RestaurantChungRevolvingStand {
	private List<RestaurantChungOrder> orderList = new ArrayList<RestaurantChungOrder>();
	private int count = 0;

	public RestaurantChungRevolvingStand() { }

	synchronized public void addOrder(RestaurantChungOrder o) {
		getOrderList().add(o);
		count++;
	}

	synchronized public RestaurantChungOrder remove() {
		if(count <= 0) {
			return null;
		}
		count--;
		return getOrderList().remove(0);
	}

	public List<RestaurantChungOrder> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<RestaurantChungOrder> orderList) {
		this.orderList = orderList;
	}
}