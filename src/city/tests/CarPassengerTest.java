package city.tests;

import city.agents.CarAgent;
import city.buildings.BusStopBuilding;
import city.roles.CarPassengerRole;
import city.tests.animations.mock.MockAnimatedCar;
import city.tests.mock.MockPerson;
import junit.framework.TestCase;

public class CarPassengerTest extends TestCase {
	MockPerson person = new MockPerson("PersonName"); 
	BusStopBuilding destination = new BusStopBuilding("BusStop1");
	CarAgent car = new CarAgent(destination);
	CarPassengerRole passenger = new CarPassengerRole(car, destination);
	MockAnimatedCar anim = new MockAnimatedCar(car);
	
	public void setUp() throws Exception {
		super.setUp();
		passenger.setPerson(person);
		car.setAnimation(anim);
	}

	public void testOneNormalCarPassengerScenario() {
		// Step 0: Check variables before startup
		assertEquals("CarPassenger should have a car, but doesn't", passenger.getCar(), car);
		assertFalse("CarPassenger scheduler should have returned false. It didn't", passenger.runScheduler());
		assertEquals("CarPassenger's destination should be set. It isn't", passenger.getDestination(), destination);
		assertEquals("CarPassenger's state should be NOTDRIVING. Instead it's: " + passenger.getState().name(), passenger.getState(), CarPassengerRole.CarPassengerState.NOTDRIVING);
		assertEquals("CarPassenger's event should be NONE. Instead it's: " + passenger.getEvent().name(), passenger.getEvent(), CarPassengerRole.CarPassengerEvent.NONE);
		assertEquals("CarAgent's passenger should be null. It isn't", car.getPassenger(), null);
		assertEquals("CarAgent's destination should be set. It isn't", car.getDestination(), destination);
		assertEquals("CarAgent's state should be NOTDRIVING. Instead it's " + car.getState().name(), car.getState(), CarAgent.CARSTATE.NOTDRIVING);
		assertEquals("CarAgent's event should be NONE. Instead it's; " + car.getEvent().name(), car.getEvent(), CarAgent.CAREVENT.NONE);
		// Step 1: Make the passenger's role active
		passenger.setActive();

		//passenger.msgImGoingToDrive();
		assertEquals("CarPassenger should have a car, but doesn't", passenger.getCar(), car);
		assertEquals("CarPassenger's destination should be a bus stop. It isn't", passenger.getDestination(), destination);
		assertEquals("CarPassenger's state should be NOTDRIVING. Instead it's: " + passenger.getState().name(), passenger.getState(), CarPassengerRole.CarPassengerState.NOTDRIVING);
		assertEquals("CarPassenger's event should be ATCAR. Instead it's: " + passenger.getEvent().name(), passenger.getEvent(), CarPassengerRole.CarPassengerEvent.ATCAR);
		assertEquals("CarAgent's passenger should be null. It isn't", car.getPassenger(), null);
		assertEquals("CarAgent's destination should be set. It isn't", car.getDestination(), destination);
		assertEquals("CarAgent's state should be NOTDRIVING. Instead it's " + car.getState().name(), car.getState(), CarAgent.CARSTATE.NOTDRIVING);
		assertEquals("CarAgent's event should be NONE. Instead it's; " + car.getEvent().name(), car.getEvent(), CarAgent.CAREVENT.NONE);
		// Step 2: Run the passenger's scheduler
		assertTrue("CarPassenger scheduler should have returned true. It didn't", passenger.runScheduler());
		assertEquals("CarPassenger should have a car, but doesn't", passenger.getCar(), car);
		assertEquals("CarPassenger's destination should be a bus stop. It isn't", passenger.getDestination(), destination);
		assertEquals("CarPassenger's state should be DRIVING. Instead it's: " + passenger.getState().name(), passenger.getState(), CarPassengerRole.CarPassengerState.DRIVING);
		assertEquals("CarPassenger's event should be ATCAR. Instead it's: " + passenger.getEvent().name(), passenger.getEvent(), CarPassengerRole.CarPassengerEvent.ATCAR);
		assertEquals("CarAgent's passenger should be set. It isn't", car.getPassenger(), passenger);
		assertEquals("CarAgent's destination should be BusStop1. It isn't", car.getDestination(), destination);
		assertEquals("CarAgent's state should be NOTDRIVING. Instead it's " + car.getState().name(), car.getState(), CarAgent.CARSTATE.NOTDRIVING);
		assertEquals("CarAgent's event should be PASSENGERENTERED. Instead it's " + car.getEvent().name(), car.getEvent(), CarAgent.CAREVENT.PASSENGERENTERED);
		// Step 4: Run the car's scheduler
		assertTrue("CarAgent scheduler should have returned true. It didn't", car.runScheduler());
		assertEquals("CarPassenger should have a car, but doesn't", passenger.getCar(), car);
		assertEquals("CarPassenger's destination should be a bus stop. It isn't", passenger.getDestination(), destination);
		assertEquals("CarPassenger's state should be DRIVING. Instead it's: " + passenger.getState().name(), passenger.getState(), CarPassengerRole.CarPassengerState.DRIVING);
		assertEquals("CarPassenger's event should be ATCAR. Instead it's: " + passenger.getEvent().name(), passenger.getEvent(), CarPassengerRole.CarPassengerEvent.ATCAR);
		assertEquals("CarAgent's passenger should be set. It isn't", car.getPassenger(), passenger);
		assertEquals("CarAgent's destination should be null. It isn't", car.getDestination(), null);
		assertEquals("CarAgent's state should be DRIVING. Instead it's " + car.getState().name(), car.getState(), CarAgent.CARSTATE.DRIVING);
		assertEquals("CarAgent's event should be ATDESTINATION. Instead it's " + car.getEvent().name(), car.getEvent(), CarAgent.CAREVENT.ATDESTINATION);
		// Step 5: Run the car's scheduler
		assertTrue("CarAgent scheduler should have returned true. It didn't", car.runScheduler());
		assertEquals("CarPassenger should have a car, but doesn't", passenger.getCar(), car);
		assertEquals("CarPassenger's destination should be null. It isn't", passenger.getDestination(), null);
		assertEquals("CarPassenger's state should be DRIVING. Instead it's: " + passenger.getState().name(), passenger.getState(), CarPassengerRole.CarPassengerState.DRIVING);
		assertEquals("CarPassenger's event should be ATDESTINATION. Instead it's: " + passenger.getEvent().name(), passenger.getEvent(), CarPassengerRole.CarPassengerEvent.ATDESTINATION);
		assertEquals("CarAgent's passenger should be null. It isn't", car.getPassenger(), null);
		assertEquals("CarAgent's destination should be null. It isn't", car.getDestination(), null);
		assertEquals("CarAgent's state should be NOTDRIVING. Instead it's " + car.getState().name(), car.getState(), CarAgent.CARSTATE.NOTDRIVING);
		assertEquals("CarAgent's event should be NONE. Instead it's " + car.getEvent().name(), car.getEvent(), CarAgent.CAREVENT.NONE);
		// Step 6: Run the passenger's scheduler
		assertTrue("CarPassenger scheduler should have returned true. It didn't", passenger.runScheduler());
		assertEquals("CarPassenger should have a car, but doesn't", passenger.getCar(), car);
		assertEquals("CarPassenger's destination should be null. It isn't", passenger.getDestination(), null);
		assertEquals("CarPassenger's state should be NOTDRIVING. Instead it's: " + passenger.getState().name(), passenger.getState(), CarPassengerRole.CarPassengerState.NOTDRIVING);
		assertEquals("CarPassenger's event should be NONE. Instead it's: " + passenger.getEvent().name(), passenger.getEvent(), CarPassengerRole.CarPassengerEvent.NONE);
		assertEquals("CarAgent's passenger should be null. It isn't", car.getPassenger(), null);
		assertEquals("CarAgent's destination should be null. It isn't", car.getDestination(), null);
		assertEquals("CarAgent's state should be NOTDRIVING. Instead it's " + car.getState().name(), car.getState(), CarAgent.CARSTATE.NOTDRIVING);
		assertEquals("CarAgent's event should be NONE. Instead it's " + car.getEvent().name(), car.getEvent(), CarAgent.CAREVENT.NONE);
		passenger.setActive();
	}

}
