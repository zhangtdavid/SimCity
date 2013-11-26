package utilities;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RestaurantChoiRevolvingStand {
	private ConcurrentLinkedQueue<RestaurantChoiOrder> ordersInQueue;
	
	public RestaurantChoiRevolvingStand(){
		ordersInQueue = new ConcurrentLinkedQueue<RestaurantChoiOrder>();
	}
	
	synchronized public void add(RestaurantChoiOrder in){
		ordersInQueue.add(in);
		in.setState(RestaurantChoiOrder.IN_QUEUE);
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
