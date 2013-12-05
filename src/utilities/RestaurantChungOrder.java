package utilities;

import city.roles.interfaces.RestaurantChungWaiter;

public class RestaurantChungOrder {
	public RestaurantChungWaiter w;
    public String choice;
    public int table;
    public OrderState s;
    
    public RestaurantChungOrder(RestaurantChungWaiter w, String selection, int tableNum, OrderState state) {
            this.w = w;
            choice = selection;
            table = tableNum;
            s = state;
    }
    
    public enum OrderState
    {Pending, Cooking, Cancelled, DoneCooking, Plating, DonePlating};
  
}