package city.buildings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import city.Animation;
import city.Building;
import city.Role;
import city.Application.FOOD_ITEMS;
import city.animations.RestaurantChungCustomerAnimation;
import city.animations.RestaurantZhangCookAnimation;
import city.animations.RestaurantZhangWaiterAnimation;
import city.gui.RestaurantChungPanel;
import city.interfaces.BankCustomer;
import city.interfaces.MarketEmployee;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCook;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungHost;
import city.interfaces.RestaurantChungWaiter;
import city.roles.RestaurantChungCustomerRole;
import city.roles.RestaurantZhangCashierRole;
import city.roles.RestaurantZhangCookRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.RestaurantZhangWaiterRegularRole;
import city.roles.RestaurantZhangWaiterSharedDataRole;

public class RestaurantChungBuilding extends Building {

//  Food
//  =====================================================================
	public RestaurantChungPanel panel; //reference to main gui
	Map<Role, Animation> allRoles = new HashMap<Role, Animation>();
	
	public RestaurantChungHost host;
	public RestaurantChungCashier cashier;
	public BankCustomer bankCustomer;
	public RestaurantChungCook cook;
	public List<RestaurantChungWaiter> waiters = new ArrayList<RestaurantChungWaiter>();
	public List<RestaurantChungCustomer> customers = new ArrayList<RestaurantChungCustomer>();
	
	private static final int WORKER_SALARY = 500;
	
	public Map<FOOD_ITEMS, Food> foods = new HashMap<FOOD_ITEMS, Food>();
    public class Food {
        public String item;
        public int cookingTime;
        public int amount;
        public int low;
        public int capacity;
        public int price;
        public FoodOrderState s;
        
        public Food(String item, int cookingTime, int amount, int low, int capacity, int price) {
            this.item = item;
            this.cookingTime = cookingTime;
            this.amount = amount;
            this.low = low;
            this.capacity = capacity;
            s = FoodOrderState.None;
        }
    }
    public enum FoodOrderState
    {None, Pending, Ordered};
	
	public RestaurantChungBuilding(String name, RestaurantChungPanel panel) {
		super(name);
		this.setCustomerRole("city.roles.RestaurantChungCustomerRole");
		this.setCustomerAnimation("city.animations.RestaurantChungCustomerAnimation");
		this.panel = panel;
        // Add items and their cooking times to a map
        foods.put(FOOD_ITEMS.chicken, new Food("chicken", 10, 10, 5, 10, 16));
        foods.put(FOOD_ITEMS.pizza, new Food("pizza", 15, 10, 5, 10, 12));
        foods.put(FOOD_ITEMS.salad, new Food("salad", 5, 10, 5, 10, 6));
        foods.put(FOOD_ITEMS.steak, new Food("steak", 20, 10, 5, 10, 10));
        
        setCash(500);
	}

	public Role addRole(Role r) {
//		if(r instanceof RestaurantChungCustomerRole) {
//			RestaurantChungCustomerRole c = (RestaurantChungCustomerRole)r;
//			c.setCashier(cashier);
//			c.setHost(host);
//			if(!allRoles.containsKey(c)) {
//				RestaurantChungCustomerAnimation anim = new RestaurantChungCustomerAnimation(c); 
//				c.setAnimation(anim);
//				anim.isVisible = true;
//				panel.addVisualizationElement(anim);
//				customers.add(c);
//				allRoles.put(c, anim);
//			}
//			return c;
//		}
//		if(r instanceof RestaurantZhangWaiterRegularRole) {
//			RestaurantZhangWaiterRegularRole w = (RestaurantZhangWaiterRegularRole)r;
//			w.setCashier(cashier);
//			w.setCook(cook);
//			w.setHost(host);
//			w.setMenu(menu);
//			host.addWaiter(w);
//			if(!allRoles.containsKey(w)) {
//				RestaurantZhangWaiterAnimation anim = new RestaurantZhangWaiterAnimation(w, waiters.size() * 30 + 80, 200); 
//				w.setAnimation(anim);
//				anim.isVisible = true;
//				panel.addVisualizationElement(anim);
//				waiters.add(w);
//				allRoles.put(w, anim);
//			}
//			return w;
//		}
//		if(r instanceof RestaurantZhangWaiterSharedDataRole) {
//			RestaurantZhangWaiterRegularRole w = (RestaurantZhangWaiterRegularRole)r;
//			w.setCashier(cashier);
//			w.setCook(cook);
//			w.setHost(host);
//			w.setMenu(menu);
//			if(!allRoles.containsKey(w)) {
//				RestaurantZhangWaiterAnimation anim = new RestaurantZhangWaiterAnimation(w, waiters.size() * 30 + 80, 200); 
//				w.setAnimation(anim);
//				anim.isVisible = true;
//				panel.addVisualizationElement(anim);
//				waiters.add(w);
//				allRoles.put(w, anim);
//			}
//			return w;
//		}
//		if(r instanceof RestaurantZhangHostRole) {
//			RestaurantZhangHostRole h = (RestaurantZhangHostRole)r;
//			h.setTables(tables);
//			if(!allRoles.containsKey(h)) { 
//				host = h;
//				allRoles.put(h, null);
//			}
//			return h;
//		}
//		if(r instanceof RestaurantZhangCookRole) {
//			RestaurantZhangCookRole c = (RestaurantZhangCookRole)r;
//			c.setRevolvingStand(orderStand);
//			c.setMenuTimes(menu);
//			if(!allRoles.containsKey(c)) { 
//				RestaurantZhangCookAnimation anim = new RestaurantZhangCookAnimation(c);
//				c.setAnimation(anim);
//				anim.isVisible = true;
//				panel.addVisualizationElement(anim);
//				cook = c;
//				allRoles.put(c, anim);
//			}
//			return c;
//		}
//		if(r instanceof RestaurantZhangCashierRole) {
//			RestaurantZhangCashierRole c = (RestaurantZhangCashierRole)r;
//			c.setMenu(menu);
//			if(!allRoles.containsKey(c)) { 
//				cashier = c;
//				allRoles.put(c, null);
//			}
//			return c;
//		}
		return null;
	}

	
//	Utilities
//	=====================================================================	
//	Waiter
		public static int getWorkerSalary() {
			return WORKER_SALARY;
		}
		
		public void addWaiter(RestaurantChungWaiter waiter) {
			waiters.add(waiter);
			host.msgNewWaiter(waiter);
			
		}
		
		public void removeWaiter(RestaurantChungWaiter waiter) {
			waiters.remove(waiter);
			host.msgRemoveWaiter(waiter);		
		}
}
