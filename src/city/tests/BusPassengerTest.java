package city.tests;

import city.agents.BusAgent;
import city.buildings.BusStopBuilding;
import city.roles.BusPassengerRole;
import city.tests.mock.MockPerson;
import junit.framework.TestCase;

public class BusPassengerTest extends TestCase {
	BusStopBuilding start = new BusStopBuilding("Start");
	BusStopBuilding destination = new BusStopBuilding("destination");
	
	MockPerson person = new MockPerson("PersonName"); 
	BusPassengerRole passenger = new BusPassengerRole(destination, start);
	
	BusAgent bus = new BusAgent(start, destination);
	
	public void setUp() throws Exception {
		super.setUp();
		passenger.setPerson(person);
		start.setNextStop(destination);
		start.setPreviousStop(destination);
		destination.setNextStop(start);
		destination.setPreviousStop(start);
	}

	public void testOneNormalBusPassengerScenario() {
		// Check preconditions
		assertEquals("Passenger state should be NOTBUSSING. Instead it's " + passenger.myState.name(), BusPassengerRole.BusPassengerState.NOTBUSSING, passenger.myState);
		assertEquals("Passenger event should be NONE. Instead it's " + passenger.myEvent.name(), BusPassengerRole.BusPassengerEvent.NONE, passenger.myEvent);
		assertEquals("Passenger should have no bus. It doesn't.", null, passenger.myBus);
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.busStopToWaitAt);
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.destination);
		assertEquals("Bus state should be DRIVING, but instead it's " + bus.myState.name(), BusAgent.BusState.DRIVING, bus.myState);
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.myEvent.name(), BusAgent.BusEvent.ATSTOP, bus.myEvent);
		assertTrue("Bus should have no passengers. It doesn't", bus.passengerList.isEmpty());
		assertEquals("Bus has the wrong current stop. It is " + bus.currentStop.getName(), start, bus.currentStop);
		assertEquals("Bus has the wrong next stop. It is " + bus.nextStop.getName(), destination, bus.nextStop);
		assertEquals("Bus should have 1.50 for bus fare. It instead is " + BusAgent.busFare, 1.50, BusAgent.busFare);
		assertEquals("Bus should have 0.00 for earned money. It instead is " + bus.earnedMoney, 0.00, bus.earnedMoney);
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertTrue("Starting bus stop should have no waiting passengers, but it does.", start.waitingList.isEmpty());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		
		// Step 1: activate the passenger
		passenger.setActive();
		assertEquals("Passenger state should be NOTBUSSING. Instead it's " + passenger.myState.name(), BusPassengerRole.BusPassengerState.NOTBUSSING, passenger.myState);
		assertEquals("Passenger event should be ATSTOP. Instead it's " + passenger.myEvent.name(), BusPassengerRole.BusPassengerEvent.ATSTOP, passenger.myEvent);
		assertEquals("Passenger should have no bus. It doesn't.", null, passenger.myBus);
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.busStopToWaitAt);
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.destination);
		assertEquals("Bus state should be DRIVING, but instead it's " + bus.myState.name(), BusAgent.BusState.DRIVING, bus.myState);
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.myEvent.name(), BusAgent.BusEvent.ATSTOP, bus.myEvent);
		assertTrue("Bus should have no passengers. It doesn't", bus.passengerList.isEmpty());
		assertEquals("Bus has the wrong current stop. It is " + bus.currentStop.getName(), start, bus.currentStop);
		assertEquals("Bus has the wrong next stop. It is " + bus.nextStop.getName(), destination, bus.nextStop);
		assertEquals("Bus should have 1.50 for bus fare. It instead is " + BusAgent.busFare, 1.50, BusAgent.busFare);
		assertEquals("Bus should have 0.00 for earned money. It instead is " + bus.earnedMoney, 0.00, bus.earnedMoney);
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 1 waiting passenger, but it doesn't.", 1, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		
		// Step 2: run the passenger's scheduler
		assertTrue("Passenger's scheduler should have returned true, but didn't", passenger.runScheduler());
		assertEquals("Passenger state should be NOTBUSSING. Instead it's " + passenger.myState.name(), BusPassengerRole.BusPassengerState.WAITINGFORBUS, passenger.myState);
		assertEquals("Passenger event should be ATSTOP. Instead it's " + passenger.myEvent.name(), BusPassengerRole.BusPassengerEvent.ATSTOP, passenger.myEvent);
		assertEquals("Passenger should have no bus. It doesn't.", null, passenger.myBus);
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.busStopToWaitAt);
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.destination);
		assertEquals("Bus state should be DRIVING, but instead it's " + bus.myState.name(), BusAgent.BusState.DRIVING, bus.myState);
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.myEvent.name(), BusAgent.BusEvent.ATSTOP, bus.myEvent);
		assertTrue("Bus should have no passengers. It doesn't", bus.passengerList.isEmpty());
		assertEquals("Bus has the wrong current stop. It is " + bus.currentStop.getName(), start, bus.currentStop);
		assertEquals("Bus has the wrong next stop. It is " + bus.nextStop.getName(), destination, bus.nextStop);
		assertEquals("Bus should have 1.50 for bus fare. It instead is " + BusAgent.busFare, 1.50, BusAgent.busFare);
		assertEquals("Bus should have 0.00 for earned money. It instead is " + bus.earnedMoney, 0.00, bus.earnedMoney);
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 1 waiting passenger, but it doesn't.", 1, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		assertFalse("Passenger's scheduler should have returned false, but didn't", passenger.runScheduler());
		
		// Step 3: run bus scheduler
		assertTrue("Bus scheduler did not return true.", bus.runScheduler());
		assertEquals("Passenger state should be NOTBUSSING. Instead it's " + passenger.myState.name(), BusPassengerRole.BusPassengerState.WAITINGFORBUS, passenger.myState);
		assertEquals("Passenger event should be BUSISHERE. Instead it's " + passenger.myEvent.name(), BusPassengerRole.BusPassengerEvent.BUSISHERE, passenger.myEvent);
		assertEquals("Passenger should have a bus. It doesn't.", bus, passenger.myBus);
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.busStopToWaitAt);
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.destination);
		assertEquals("Bus state should be PICKINGPEOPLEUP, but instead it's " + bus.myState.name(), BusAgent.BusState.PICKINGPEOPLEUP, bus.myState);
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.myEvent.name(), BusAgent.BusEvent.ATSTOP, bus.myEvent);
		assertEquals("Bus should have 1 passenger. It doesn't", 1, bus.passengerList.size());
		assertEquals("Bus has the wrong current stop. It is " + bus.currentStop.getName(), start, bus.currentStop);
		assertEquals("Bus has the wrong next stop. It is " + bus.nextStop.getName(), destination, bus.nextStop);
		assertEquals("Bus should have 1.50 for bus fare. It instead is " + BusAgent.busFare, 1.50, BusAgent.busFare);
		assertEquals("Bus should have 0.00 for earned money. It instead is " + bus.earnedMoney, 0.00, bus.earnedMoney);
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 1 waiting passenger, but it doesn't.", 1, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		assertFalse("Bus's scheduler should have returned false, but didn't", bus.runScheduler());
		
		// Step 4: run the passenger's scheduler
		assertTrue("Passenger scheduler did not return true.", passenger.runScheduler());
		assertEquals("Passenger state should be ONBUS. Instead it's " + passenger.myState.name(), BusPassengerRole.BusPassengerState.ONBUS, passenger.myState);
		assertEquals("Passenger event should be BUSISHERE. Instead it's " + passenger.myEvent.name(), BusPassengerRole.BusPassengerEvent.BUSISHERE, passenger.myEvent);
		assertEquals("Passenger should have a bus. It doesn't.", bus, passenger.myBus);
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.busStopToWaitAt);
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.destination);
		assertEquals("Bus state should be PICKINGPEOPLEUP, but instead it's " + bus.myState.name(), BusAgent.BusState.PICKINGPEOPLEUP, bus.myState);
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.myEvent.name(), BusAgent.BusEvent.ATSTOP, bus.myEvent);
		assertEquals("Bus should have 1 passenger. It doesn't", 1, bus.passengerList.size());
		assertEquals("Bus has the wrong current stop. It is " + bus.currentStop.getName(), start, bus.currentStop);
		assertEquals("Bus has the wrong next stop. It is " + bus.nextStop.getName(), destination, bus.nextStop);
		assertEquals("Bus should have 1.50 for bus fare. It instead is " + BusAgent.busFare, 1.50, BusAgent.busFare);
		assertEquals("Bus should have 1.35 for earned money. It instead is " + bus.earnedMoney, 1.5, bus.earnedMoney);
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 0 waiting passengers, but it doesn't.", 0, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		assertFalse("Passenger's scheduler should have returned false, but didn't", passenger.runScheduler());
		
		// Step 5 run the bus's scheduler
		assertTrue("Bus scheduler did not return true.", bus.runScheduler());
		assertEquals("Passenger state should be ONBUS. Instead it's " + passenger.myState.name(), BusPassengerRole.BusPassengerState.ONBUS, passenger.myState);
		assertEquals("Passenger event should be BUSISHERE. Instead it's " + passenger.myEvent.name(), BusPassengerRole.BusPassengerEvent.BUSISHERE, passenger.myEvent);
		assertEquals("Passenger should have a bus. It doesn't.", bus, passenger.myBus);
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.busStopToWaitAt);
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.destination);
		assertEquals("Bus state should be DRIVING, but instead it's " + bus.myState.name(), BusAgent.BusState.DRIVING, bus.myState);
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.myEvent.name(), BusAgent.BusEvent.ATSTOP, bus.myEvent);
		assertEquals("Bus should have 1 passenger. It doesn't", 1, bus.passengerList.size());
		assertEquals("Bus has the wrong current stop. It is " + bus.currentStop.getName(), destination, bus.currentStop);
		assertEquals("Bus has the wrong next stop. It is " + bus.nextStop.getName(), start, bus.nextStop);
		assertEquals("Bus should have 1.50 for bus fare. It instead is " + BusAgent.busFare, 1.50, BusAgent.busFare);
		assertEquals("Bus should have 1.50 for earned money. It instead is " + bus.earnedMoney, 1.5, bus.earnedMoney);
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 0 waiting passengers, but it doesn't.", 0, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		
		// Step 6 run the bus's scheduler
		assertTrue("Bus's scheduler should have returned true, but didn't", bus.runScheduler());
		assertEquals("Passenger state should be ONBUS. Instead it's " + passenger.myState.name(), BusPassengerRole.BusPassengerState.ONBUS, passenger.myState);
		assertEquals("Passenger event should be ATDESTINATION. Instead it's " + passenger.myEvent.name(), BusPassengerRole.BusPassengerEvent.ATDESTINATION, passenger.myEvent);
		assertEquals("Passenger should have a bus. It doesn't.", bus, passenger.myBus);
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.busStopToWaitAt);
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.destination);
		assertEquals("Bus state should be DROPPINGPASSENGERSOFF, but instead it's " + bus.myState.name(), BusAgent.BusState.DROPPINGPASSENGERSOFF, bus.myState);
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.myEvent.name(), BusAgent.BusEvent.ATSTOP, bus.myEvent);
		assertEquals("Bus should have 1 passenger. It doesn't", 1, bus.passengerList.size());
		assertEquals("Bus has the wrong current stop. It is " + bus.currentStop.getName(), destination, bus.currentStop);
		assertEquals("Bus has the wrong next stop. It is " + bus.nextStop.getName(), start, bus.nextStop);
		assertEquals("Bus should have 1.50 for bus fare. It instead is " + BusAgent.busFare, 1.50, BusAgent.busFare);
		assertEquals("Bus should have 1.50 for earned money. It instead is " + bus.earnedMoney, 1.5, bus.earnedMoney);
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 0 waiting passengers, but it doesn't.", 0, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		assertFalse("Bus's scheduler should have returned false, but didn't", bus.runScheduler());
		
		// Step 7 run the person's scheduler
		assertTrue("Passenger scheduler did not return true.", passenger.runScheduler());
		assertEquals("Passenger state should be NOTBUSSING. Instead it's " + passenger.myState.name(), BusPassengerRole.BusPassengerState.NOTBUSSING, passenger.myState);
		assertEquals("Passenger event should be NONE. Instead it's " + passenger.myEvent.name(), BusPassengerRole.BusPassengerEvent.NONE, passenger.myEvent);
		assertEquals("Passenger should have a bus. It doesn't.", bus, passenger.myBus);
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.busStopToWaitAt);
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.destination);
		assertEquals("Bus state should be DROPPINGPASSENGERSOFF, but instead it's " + bus.myState.name(), BusAgent.BusState.DROPPINGPASSENGERSOFF, bus.myState);
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.myEvent.name(), BusAgent.BusEvent.ATSTOP, bus.myEvent);
		assertEquals("Bus should have 1 passenger. It doesn't", 1, bus.passengerList.size());
		assertEquals("Bus has the wrong current stop. It is " + bus.currentStop.getName(), destination, bus.currentStop);
		assertEquals("Bus has the wrong next stop. It is " + bus.nextStop.getName(), start, bus.nextStop);
		assertEquals("Bus should have 1.50 for bus fare. It instead is " + BusAgent.busFare, 1.50, BusAgent.busFare);
		assertEquals("Bus should have 1.50 for earned money. It instead is " + bus.earnedMoney, 1.5, bus.earnedMoney);
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 0 waiting passengers, but it doesn't.", 0, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		assertFalse("Passenger's scheduler should have returned false, but didn't", passenger.runScheduler());
		assertFalse("Passenger should be inactive. It isn't", passenger.getActive());
		
		// Step 8 run the bus's scheduler
		assertTrue("Bus's scheduler should have returned true, but didn't", bus.runScheduler());
		assertEquals("Passenger state should be NOTBUSSING. Instead it's " + passenger.myState.name(), BusPassengerRole.BusPassengerState.NOTBUSSING, passenger.myState);
		assertEquals("Passenger event should be NONE. Instead it's " + passenger.myEvent.name(), BusPassengerRole.BusPassengerEvent.NONE, passenger.myEvent);
		assertEquals("Passenger should have a bus. It doesn't.", bus, passenger.myBus);
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.busStopToWaitAt);
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.destination);
		assertEquals("Bus state should be DRIVING, but instead it's " + bus.myState.name(), BusAgent.BusState.DRIVING, bus.myState);
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.myEvent.name(), BusAgent.BusEvent.ATSTOP, bus.myEvent);
		assertEquals("Bus should have 0 passengers. It doesn't", 0, bus.passengerList.size());
		assertEquals("Bus has the wrong current stop. It is " + bus.currentStop.getName(), start, bus.currentStop);
		assertEquals("Bus has the wrong next stop. It is " + bus.nextStop.getName(), destination, bus.nextStop);
		assertEquals("Bus should have 1.50 for bus fare. It instead is " + BusAgent.busFare, 1.50, BusAgent.busFare);
		assertEquals("Bus should have 1.50 for earned money. It instead is " + bus.earnedMoney, 1.5, bus.earnedMoney);
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 0 waiting passengers, but it doesn't.", 0, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
	}

}
