package city.tests.roles;

import java.awt.Color;

import junit.framework.TestCase;
import city.agents.BusAgent;
import city.animations.interfaces.AnimatedBus;
import city.buildings.BusStopBuilding;
import city.buildings.RestaurantZhangBuilding;
import city.gui.exteriors.CityViewBusStop;
import city.gui.interiors.BusStopPanel;
import city.roles.BusPassengerRole;
import city.tests.agents.mocks.MockPerson;
import city.tests.animations.mocks.MockAnimatedBus;
import city.tests.animations.mocks.MockAnimatedBusPassenger;

public class BusPassengerTest extends TestCase {
	BusStopPanel startPanel = new BusStopPanel(Color.black);
	CityViewBusStop cityViewBusStopStart;
	BusStopBuilding start;
	
	BusStopPanel destinationPanel = new BusStopPanel(Color.black);
	CityViewBusStop cityViewBusStopDestination;
	BusStopBuilding destination;
	
	MockPerson person = new MockPerson("PersonName"); 
	BusPassengerRole passenger;
	
	BusAgent bus;
	
	AnimatedBus anim;
	
	public void setUp() throws Exception {
		super.setUp();
		
		cityViewBusStopStart = new CityViewBusStop(1, 1, "Start Stop", Color.black, startPanel);
		start = new BusStopBuilding("Start", startPanel, cityViewBusStopStart);
		
		cityViewBusStopDestination = new CityViewBusStop(1, 1, "Destination Stop", Color.black, destinationPanel);
		destination = new BusStopBuilding("destination", destinationPanel, cityViewBusStopDestination);
		
		passenger = new BusPassengerRole(destination, start, new RestaurantZhangBuilding(null, null, null));
		passenger.setAnimation(new MockAnimatedBusPassenger(passenger));
		
		bus = new BusAgent(start, destination);
		anim = new MockAnimatedBus(bus);
		bus.setAnimation(anim);
		passenger.setPerson(person);
		start.setNextStop(destination);
		start.setPreviousStop(destination);
		destination.setNextStop(start);
		destination.setPreviousStop(start);
	}

	public void testOneNormalBusPassengerScenario() {
		// Check preconditions
		assertEquals("Passenger state should be NOTBUSSING. Instead it's " + passenger.getState().name(), BusPassengerRole.BUSPASSENGERSTATE.NOTBUSSING, passenger.getState());
		assertEquals("Passenger event should be NONE. Instead it's " + passenger.getEvent().name(), BusPassengerRole.BUSPASSENGEREVENT.NONE, passenger.getEvent());
		assertEquals("Passenger should have no bus. It doesn't.", null, passenger.getBus());
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.getBusStopToWaitAt());
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.getBusStopDestination());
		assertEquals("Bus state should be DRIVING, but instead it's " + bus.getState().name(), BusAgent.BUSSTATE.DRIVING, bus.getState());
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.getEvent().name(), BusAgent.BUSEVENT.ATSTOP, bus.getEvent());
		assertTrue("Bus should have no passengers. It doesn't", bus.getPassengerList().isEmpty());
		assertEquals("Bus has the wrong current stop. It is " + bus.getCurrentStop().getName(), start, bus.getCurrentStop());
		assertEquals("Bus has the wrong next stop. It is " + bus.getNextStop().getName(), destination, bus.getNextStop());
		assertEquals("Bus should have 0 for earned money. It instead is " + bus.getEarnedMoney(), 0, bus.getEarnedMoney());
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertTrue("Starting bus stop should have no waiting passengers, but it does.", start.waitingList.isEmpty());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		
		// Step 1: activate the passenger
		passenger.setActive();
		assertEquals("Passenger state should be NOTBUSSING. Instead it's " + passenger.getState().name(), BusPassengerRole.BUSPASSENGERSTATE.NOTBUSSING, passenger.getState());
		assertEquals("Passenger event should be ATSTOP. Instead it's " + passenger.getEvent().name(), BusPassengerRole.BUSPASSENGEREVENT.ATSTOP, passenger.getEvent());
		assertEquals("Passenger should have no bus. It doesn't.", null, passenger.getBus());
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.getBusStopToWaitAt());
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.getBusStopDestination());
		assertEquals("Bus state should be DRIVING, but instead it's " + bus.getState().name(), BusAgent.BUSSTATE.DRIVING, bus.getState());
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.getEvent().name(), BusAgent.BUSEVENT.ATSTOP, bus.getEvent());
		assertTrue("Bus should have no passengers. It doesn't", bus.getPassengerList().isEmpty());
		assertEquals("Bus has the wrong current stop. It is " + bus.getCurrentStop().getName(), start, bus.getCurrentStop());
		assertEquals("Bus has the wrong next stop. It is " + bus.getNextStop().getName(), destination, bus.getNextStop());
		assertEquals("Bus should have 0 for earned money. It instead is " + bus.getEarnedMoney(), 0, bus.getEarnedMoney());
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 1 waiting passenger, but it doesn't.", 1, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		
		// Step 2: run the passenger's scheduler
		assertTrue("Passenger's scheduler should have returned true, but didn't", passenger.runScheduler());
		assertEquals("Passenger state should be NOTBUSSING. Instead it's " + passenger.getState().name(), BusPassengerRole.BUSPASSENGERSTATE.WAITINGFORBUS, passenger.getState());
		assertEquals("Passenger event should be ATSTOP. Instead it's " + passenger.getEvent().name(), BusPassengerRole.BUSPASSENGEREVENT.ATSTOP, passenger.getEvent());
		assertEquals("Passenger should have no bus. It doesn't.", null, passenger.getBus());
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.getBusStopToWaitAt());
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.getBusStopDestination());
		assertEquals("Bus state should be DRIVING, but instead it's " + bus.getState().name(), BusAgent.BUSSTATE.DRIVING, bus.getState());
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.getEvent().name(), BusAgent.BUSEVENT.ATSTOP, bus.getEvent());
		assertTrue("Bus should have no passengers. It doesn't", bus.getPassengerList().isEmpty());
		assertEquals("Bus has the wrong current stop. It is " + bus.getCurrentStop().getName(), start, bus.getCurrentStop());
		assertEquals("Bus has the wrong next stop. It is " + bus.getNextStop().getName(), destination, bus.getNextStop());
		assertEquals("Bus should have 0 for earned money. It instead is " + bus.getEarnedMoney(), 0, bus.getEarnedMoney());
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 1 waiting passenger, but it doesn't.", 1, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		assertFalse("Passenger's scheduler should have returned false, but didn't", passenger.runScheduler());
		
		// Step 3: run bus scheduler
		assertTrue("Bus scheduler did not return true.", bus.runScheduler());
		assertEquals("Passenger state should be NOTBUSSING. Instead it's " + passenger.getState().name(), BusPassengerRole.BUSPASSENGERSTATE.WAITINGFORBUS, passenger.getState());
		assertEquals("Passenger event should be BUSISHERE. Instead it's " + passenger.getEvent().name(), BusPassengerRole.BUSPASSENGEREVENT.BUSISHERE, passenger.getEvent());
		assertEquals("Passenger should have a bus. It doesn't.", bus, passenger.getBus());
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.getBusStopToWaitAt());
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.getBusStopDestination());
		assertEquals("Bus state should be PICKINGPEOPLEUP, but instead it's " + bus.getState().name(), BusAgent.BUSSTATE.PICKINGPEOPLEUP, bus.getState());
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.getEvent().name(), BusAgent.BUSEVENT.ATSTOP, bus.getEvent());
		assertEquals("Bus should have 1 passenger. It doesn't", 1, bus.getPassengerList().size());
		assertEquals("Bus has the wrong current stop. It is " + bus.getCurrentStop().getName(), start, bus.getCurrentStop());
		assertEquals("Bus has the wrong next stop. It is " + bus.getNextStop().getName(), destination, bus.getNextStop());
		assertEquals("Bus should have 0 for earned money. It instead is " + bus.getEarnedMoney(), 0, bus.getEarnedMoney());
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 1 waiting passenger, but it doesn't.", 1, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		assertFalse("Bus's scheduler should have returned false, but didn't", bus.runScheduler());
		
		// Step 4: run the passenger's scheduler
		passenger.msgImAtDestination();
		assertTrue("Passenger scheduler did not return true.", passenger.runScheduler());
		assertEquals("Passenger state should be ONBUS. Instead it's " + passenger.getState().name(), BusPassengerRole.BUSPASSENGERSTATE.ONBUS, passenger.getState());
		assertEquals("Passenger event should be BUSISHERE. Instead it's " + passenger.getEvent().name(), BusPassengerRole.BUSPASSENGEREVENT.BUSISHERE, passenger.getEvent());
		assertEquals("Passenger should have a bus. It doesn't.", bus, passenger.getBus());
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.getBusStopToWaitAt());
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.getBusStopDestination());
		assertEquals("Bus state should be PICKINGPEOPLEUP, but instead it's " + bus.getState().name(), BusAgent.BUSSTATE.PICKINGPEOPLEUP, bus.getState());
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.getEvent().name(), BusAgent.BUSEVENT.ATSTOP, bus.getEvent());
		assertEquals("Bus should have 1 passenger. It doesn't", 1, bus.getPassengerList().size());
		assertEquals("Bus has the wrong current stop. It is " + bus.getCurrentStop().getName(), start, bus.getCurrentStop());
		assertEquals("Bus has the wrong next stop. It is " + bus.getNextStop().getName(), destination, bus.getNextStop());
		assertEquals("Bus should have 1.35 for earned money. It instead is " + bus.getEarnedMoney(), 2, bus.getEarnedMoney());
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 0 waiting passengers, but it doesn't.", 0, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		assertFalse("Passenger's scheduler should have returned false, but didn't", passenger.runScheduler());
		
		// Step 5 run the bus's scheduler
		assertTrue("Bus scheduler did not return true.", bus.runScheduler());
		assertEquals("Passenger state should be ONBUS. Instead it's " + passenger.getState().name(), BusPassengerRole.BUSPASSENGERSTATE.ONBUS, passenger.getState());
		assertEquals("Passenger event should be BUSISHERE. Instead it's " + passenger.getEvent().name(), BusPassengerRole.BUSPASSENGEREVENT.BUSISHERE, passenger.getEvent());
		assertEquals("Passenger should have a bus. It doesn't.", bus, passenger.getBus());
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.getBusStopToWaitAt());
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.getBusStopDestination());
		assertEquals("Bus state should be DRIVING, but instead it's " + bus.getState().name(), BusAgent.BUSSTATE.DRIVING, bus.getState());
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.getEvent().name(), BusAgent.BUSEVENT.ATSTOP, bus.getEvent());
		assertEquals("Bus should have 1 passenger. It doesn't", 1, bus.getPassengerList().size());
		assertEquals("Bus has the wrong current stop. It is " + bus.getCurrentStop().getName(), destination, bus.getCurrentStop());
		assertEquals("Bus has the wrong next stop. It is " + bus.getNextStop().getName(), start, bus.getNextStop());
		assertEquals("Bus should have 2 for earned money. It instead is " + bus.getEarnedMoney(), 2, bus.getEarnedMoney());
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 0 waiting passengers, but it doesn't.", 0, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		
		// Step 6 run the bus's scheduler
		assertTrue("Bus's scheduler should have returned true, but didn't", bus.runScheduler());
		assertEquals("Passenger state should be ONBUS. Instead it's " + passenger.getState().name(), BusPassengerRole.BUSPASSENGERSTATE.ONBUS, passenger.getState());
		assertEquals("Passenger event should be ATDESTINATION. Instead it's " + passenger.getEvent().name(), BusPassengerRole.BUSPASSENGEREVENT.ATDESTINATION, passenger.getEvent());
		assertEquals("Passenger should have a bus. It doesn't.", bus, passenger.getBus());
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.getBusStopToWaitAt());
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.getBusStopDestination());
		assertEquals("Bus state should be DROPPINGPASSENGERSOFF, but instead it's " + bus.getState().name(), BusAgent.BUSSTATE.DROPPINGPASSENGERSOFF, bus.getState());
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.getEvent().name(), BusAgent.BUSEVENT.ATSTOP, bus.getEvent());
		assertEquals("Bus should have 1 passenger. It doesn't", 1, bus.getPassengerList().size());
		assertEquals("Bus has the wrong current stop. It is " + bus.getCurrentStop().getName(), destination, bus.getCurrentStop());
		assertEquals("Bus has the wrong next stop. It is " + bus.getNextStop().getName(), start, bus.getNextStop());
		assertEquals("Bus should have 2 for earned money. It instead is " + bus.getEarnedMoney(), 2, bus.getEarnedMoney());
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 0 waiting passengers, but it doesn't.", 0, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
		assertFalse("Bus's scheduler should have returned false, but didn't", bus.runScheduler());
		
		// Step 7 run the person's scheduler
		passenger.msgImAtDestination();
		assertTrue("Passenger scheduler did not return true.", passenger.runScheduler());
		assertEquals("Passenger state should be NOTBUSSING. Instead it's " + passenger.getState().name(), BusPassengerRole.BUSPASSENGERSTATE.NOTBUSSING, passenger.getState());
		assertEquals("Passenger event should be NONE. Instead it's " + passenger.getEvent().name(), BusPassengerRole.BUSPASSENGEREVENT.NONE, passenger.getEvent());
		assertEquals("Passenger should have a bus. It doesn't.", bus, passenger.getBus());
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.getBusStopToWaitAt());
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.getBusStopDestination());
		assertEquals("Bus state should be DROPPINGPASSENGERSOFF, but instead it's " + bus.getState().name(), BusAgent.BUSSTATE.DROPPINGPASSENGERSOFF, bus.getState());
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.getEvent().name(), BusAgent.BUSEVENT.ATSTOP, bus.getEvent());
		assertEquals("Bus should have 1 passenger. It doesn't", 1, bus.getPassengerList().size());
		assertEquals("Bus has the wrong current stop. It is " + bus.getCurrentStop().getName(), destination, bus.getCurrentStop());
		assertEquals("Bus has the wrong next stop. It is " + bus.getNextStop().getName(), start, bus.getNextStop());
		assertEquals("Bus should have 2 for earned money. It instead is " + bus.getEarnedMoney(), 2, bus.getEarnedMoney());
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
		assertEquals("Passenger state should be NOTBUSSING. Instead it's " + passenger.getState().name(), BusPassengerRole.BUSPASSENGERSTATE.NOTBUSSING, passenger.getState());
		assertEquals("Passenger event should be NONE. Instead it's " + passenger.getEvent().name(), BusPassengerRole.BUSPASSENGEREVENT.NONE, passenger.getEvent());
		assertEquals("Passenger should have a bus. It doesn't.", bus, passenger.getBus());
		assertEquals("Passenger has the wrong starting bus stop.", start, passenger.getBusStopToWaitAt());
		assertEquals("Passenger has the wrong destination bus stop.", destination, passenger.getBusStopDestination());
		assertEquals("Bus state should be DRIVING, but instead it's " + bus.getState().name(), BusAgent.BUSSTATE.DRIVING, bus.getState());
		assertEquals("Bus event should be ATSTOP, but instead it's " + bus.getEvent().name(), BusAgent.BUSEVENT.ATSTOP, bus.getEvent());
		assertEquals("Bus should have 0 passengers. It doesn't", 0, bus.getPassengerList().size());
		assertEquals("Bus has the wrong current stop. It is " + bus.getCurrentStop().getName(), start, bus.getCurrentStop());
		assertEquals("Bus has the wrong next stop. It is " + bus.getNextStop().getName(), destination, bus.getNextStop());
		assertEquals("Bus should have 2 for earned money. It instead is " + bus.getEarnedMoney(), 2, bus.getEarnedMoney());
		assertEquals("Starting bus stop has the wrong next bus stop " + start.nextStop.getName(), destination, start.nextStop);
		assertEquals("Starting bus stop has the wrong prev bus stop " + start.previousStop.getName(), destination, start.previousStop);
		assertEquals("Starting bus stop should have 0 waiting passengers, but it doesn't.", 0, start.waitingList.size());
		assertEquals("Destination bus stop has the wrong next bus stop " + destination.nextStop.getName(), start, destination.nextStop);
		assertEquals("Destination bus stop has the wrong prev bus stop " + destination.previousStop.getName(), start, destination.previousStop);
		assertTrue("Destination bus stop should have no waiting passengers, but it does.", destination.waitingList.isEmpty());
	}

}
