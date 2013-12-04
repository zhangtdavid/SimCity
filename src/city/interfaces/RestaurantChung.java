package city.interfaces;

import city.abstracts.RestaurantBuildingInterface;
import city.animations.RestaurantChungWaiterAnimation;

public interface RestaurantChung extends RestaurantBuildingInterface {

	public void addWaiter(RestaurantChungWaiter w,
			RestaurantChungWaiterAnimation anim);

	public void removeWaiter(RestaurantChungWaiter waiter);

}