package city.tests.buildings.mocks;

import city.tests.bases.mocks.MockRestaurantBuilding;

/**
 * Though there are 5 different restaurants, this mock is just for the use of PersonTest.
 */
public class MockRestaurant extends MockRestaurantBuilding {

	public MockRestaurant(String name) {
		super(name);
		this.setCustomerRoleName("city.tests.roles.mocks.MockRestaurantCustomer");
		this.setCustomerAnimationName("city.tests.animations.mocks.MockRestaurantAnimatedCustomer");
	}

}
