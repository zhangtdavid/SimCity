package city.tests;

import city.buildings.BusStopBuilding;
import city.roles.CarPassengerRole;
import city.tests.mock.MockCar;
import junit.framework.TestCase;

public class CarPassengerTest extends TestCase {
	MockCar car = new MockCar("Mock Car");
	CarPassengerRole passenger = new CarPassengerRole(car);
	BusStopBuilding destination = new BusStopBuilding("BusStop1"); 
	
	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void testOneNormalCarPassengerScenario() {
		// Step 0: Check variables before startup
		assertEquals("CarPassenger should have a car, but doesn't", passenger.myCar, car);
		assertFalse("CarPassenger scheduler should have returned false. It didn't", passenger.runScheduler());
		assertEquals("CarPassenger's destination should be null. It isn't", passenger.destination, null);
		assertEquals("CarPassenger's state should be NOTDRIVING. Instead it's: " + passenger.myState.name(), passenger.myState, CarPassengerRole.CarPassengerState.NOTDRIVING);
		assertEquals("CarPassenger's event should be NONE. Instead it's: " + passenger.myEvent.name(), passenger.myEvent, CarPassengerRole.CarPassengerEvent.NONE);
		passenger.setActive(destination);
	}
	
}
