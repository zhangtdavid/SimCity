package city.roles;

import city.interfaces.RestaurantChoiWaiter;


public class RestaurantChoiOrder {

    private int state;
    private int item; // e.g. 1=salad, 2=soup, 3=steak, 4=lobster?
    private int tableNumber; // since wouldn't be great to get all of table.
    private RestaurantChoiWaiter waiter;
    static final int NOT_IN_EXISTENCE = -1; // what are enums? lol
    static final int ORDERED = 0;
    static final int RECOGNIZED = 1;
    static final int CHECKING = 2;
    static final int TO_COOK = 3;
    static final int COOKING = 4;
    static final int COOKED = 5;
    static final int READY_FOR_PICKUP = 6;
    static final int READY_AND_NOTIFIED = 7;
    static final int GIVEN_TO_WAITER = 8;
    static final int GIVEN_TO_CUSTOMER = 9;
		
    RestaurantChoiOrder(int c, int tn, RestaurantChoiWaiter w) { // have order be synchronized!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
        state = 0;
        item = c;
        waiter = w;
        tableNumber = tn;
    }
    public RestaurantChoiOrder(){
    	state = NOT_IN_EXISTENCE;
    	item = NOT_IN_EXISTENCE;
    	tableNumber = NOT_IN_EXISTENCE; // -1 is an error code
    }

    protected void setState(int i) {
        state = i;
    }
    public int getChoice(){
    	return item;
    }
    public int getState(){
    	return state;
    }
    public int getTableNumber(){
    	return tableNumber;
    }
    public void setTableNumber(int t){
    	tableNumber = t;
    }
    public RestaurantChoiWaiter getWaiter(){
    	return waiter;
    }
}
