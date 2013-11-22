package city.roles;

import java.util.concurrent.ConcurrentLinkedQueue;
import city.interfaces.RestaurantChoiCook;

public class RestaurantChoiRevolvingStand {
	private ConcurrentLinkedQueue<RestaurantChoiOrder> ordersInQueue;
	private RestaurantChoiCook cook;
	
	public RestaurantChoiRevolvingStand(RestaurantChoiCook c){
		cook = c;
		ordersInQueue = new ConcurrentLinkedQueue<RestaurantChoiOrder>();
	}
	
	synchronized public void add(RestaurantChoiOrder in){
		ordersInQueue.add(in);
		in.setState(RestaurantChoiOrder.IN_QUEUE);
		cook.msgOrderInQueue();
	}
	
	synchronized public RestaurantChoiOrder poll(){
		return ordersInQueue.poll();
	}
	
	synchronized public RestaurantChoiOrder peek(){
		return ordersInQueue.peek();
	}
	
	synchronized public boolean isEmpty(){
		return ordersInQueue.isEmpty();
	}
}
