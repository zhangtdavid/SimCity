package utilities;

import city.Application.FOOD_ITEMS;
import city.roles.interfaces.RestaurantChungWaiter;

public class RestaurantChungOrder {
	private RestaurantChungWaiter w;
	private FOOD_ITEMS choice;
	private int table;
	private OrderState s;
    
    public RestaurantChungOrder(RestaurantChungWaiter w, FOOD_ITEMS selection, int tableNum, OrderState state) {
            this.w = w;
            choice = selection;
            table = tableNum;
            s = state;
    }
    
    public enum OrderState
    {Pending, Cooking, Cancelled, DoneCooking, Plating, DonePlating};
    
    
    // Getters
    public RestaurantChungWaiter getRestaurantChungWaiter() {
		return w;
	}
    
    public FOOD_ITEMS getChoice() {
		return choice;
	}
    
    public int getTable() {
		return table;
	}
    
    public OrderState getOrderState() {
		return s;
	}
    
    // Setters
    
    public void setRestaurantChungWaiter(RestaurantChungWaiter w) {
		this.w = w;
	}
    
    public void setChoice(FOOD_ITEMS choice) {
		this.choice = choice;
	}
    
    public void setTable(int table) {
		this.table = table;
	}
    
    public void setOrderState(OrderState s) {
		this.s = s;
	}
  
}