package utilities;

import city.interfaces.RestaurantChoiWaiter;


public class RestaurantChoiOrder {

    private int state;
    private int item; // e.g. 1=salad, 2=soup, 3=steak, 4=lobster?
    private int tableNumber; // since wouldn't be great to get all of table.
    private RestaurantChoiWaiter waiter;
    public static final int NOT_IN_EXISTENCE = -1; // what are enums? lol
    public static final int ORDERED = 0;
    public static final int IN_QUEUE = 1;
    public static final int RECOGNIZED = 2;
    public static final int CHECKING = 3;
    public static final int TO_COOK = 4;
    public static final int COOKING = 5;
    public static final int COOKED = 6;
    public static final int READY_FOR_PICKUP = 7;
    public static final int READY_AND_NOTIFIED = 8;
    public static final int GIVEN_TO_WAITER = 9;
    public static final int GIVEN_TO_CUSTOMER = 10;
		
    public RestaurantChoiOrder(int c, int tn, RestaurantChoiWaiter w) { // have order be synchronized!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
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

    public void setState(int i) {
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
