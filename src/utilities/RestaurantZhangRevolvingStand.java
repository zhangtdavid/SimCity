package utilities;

import java.util.ArrayList;
import java.util.List;

public class RestaurantZhangRevolvingStand {
	public List<RestaurantZhangOrder> RestaurantZhangOrderList = new ArrayList<RestaurantZhangOrder>();
	public int count = 0;

	public RestaurantZhangRevolvingStand() { }

	synchronized public void addOrder(RestaurantZhangOrder o) {
		RestaurantZhangOrderList.add(o);
		count++;
	}

	synchronized public RestaurantZhangOrder remove() {
		if(count <= 0) {
			return null;
		}
		count--;
		return RestaurantZhangOrderList.remove(0);
	}
}
