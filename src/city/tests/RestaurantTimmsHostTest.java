package city.tests;

import junit.framework.TestCase;
import city.roles.RestaurantTimmsHostRole;

public class RestaurantTimmsHostTest extends TestCase {
	
	RestaurantTimmsHostRole host;

	public void setUp() throws Exception {
		super.setUp();		
		this.host = new RestaurantTimmsHostRole();
	}

}
