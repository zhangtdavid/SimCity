package utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantChoiMenu {
	int[] foodOptions = new int[]{1,2,3,4};
	public HashMap<Integer, Double> foodPrice = new HashMap<Integer, Double>();
	// these keep the orders simple
	public final static int STEAK = 1;
	public final static int CHICKEN = 2;
	public final static int SALAD = 3;
	public final static int PIZZA = 4;
	
	public ArrayList<Integer> dontHaveThis = new ArrayList<Integer>();
	public RestaurantChoiMenu(){ 
		foodPrice.put(1,15.99);
		foodPrice.put(2,10.99);
		foodPrice.put(3,5.99);
		foodPrice.put(4,8.99);
	}

	public double getPrice(int index){
		return foodPrice.get(index);
	}
	public int getNumberOfItems(){
		return foodPrice.size();
	}

	//if food is on the list of things that aren't available, can't order.
	public boolean isAvailable(int food){
		return !dontHaveThis.contains(food); 
	}
}
