package city.roles;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.Building;
import city.Role;
import city.animations.RestaurantJPWaiterAnimation;
import city.buildings.RestaurantJPBuilding;
import city.interfaces.RestaurantJPWaiter;
import city.interfaces.RestaurantJPCustomer;
import utilities.RestaurantJPMenuClass;
import utilities.RestaurantJPTableClass;
import utilities.RestaurantJPWaiterBase;

public class RestaurantJPWaiterRole extends RestaurantJPWaiterBase {
	
	public RestaurantJPWaiterRole(RestaurantJPBuilding b) {
		super(b);
	}
		// TODO Auto-generated constructor stub
}

