package utilities;

import city.roles.interfaces.RestaurantZhangWaiter;

public class RestaurantZhangOrder {
	public RestaurantZhangWaiter w;
	public enum OrderStatus {created, cooking, doneCooking, plated, removed};
	public String choice;
	public RestaurantZhangTable t;
	public int pos;
	public OrderStatus status;
	public RestaurantZhangOrder(RestaurantZhangWaiter w_, String choice_, RestaurantZhangTable t_, int p) {
		w = w_;
		choice = choice_;
		t = t_;
		pos = p;
		status = OrderStatus.created;
	}
	public RestaurantZhangOrder(RestaurantZhangOrder o) {
		this(o.w, o.choice, o.t, o.pos);
	}
	public boolean equals(RestaurantZhangOrder o) {
		if(w.equals(o.w) && choice.equals(o.choice) && t.equals(o.t))
			return true;
		else
			return false;
	}
}