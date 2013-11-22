package city.agents;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.Agent;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Building;
import city.Role;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.HouseBuilding;
import city.buildings.MarketBuilding;
import city.interfaces.Car;
import city.interfaces.Person;
import city.roles.BankCustomerRole;
import city.roles.BusPassengerRole;
import city.roles.CarPassengerRole;
import city.roles.ResidentRole;

public class PersonAgent extends Agent implements Person {
	
	// Data
	
	private Date date;
	private Role occupation;
	private Building workplace;
	private HouseBuilding home;
	private Car car;
	private CarPassengerRole carPassengerRole;
	private BusPassengerRole busPassengerRole;
	private BankCustomerRole bankCustomerRole;
	private ResidentRole residentRole;
	private Role restaurantCustomerRole; // The customer role of whatever restaurant the person is in.
	private Date lastAteAtRestaurant;
	private String name;
	private List<Role> roles = new ArrayList<Role>();
	private Semaphore atDestination = new Semaphore(0, true);
	private city.animations.interfaces.AnimatedPerson animation;
	private State state; 
	private int cash;
	
	// Constructor
	
    /**
     * Create a Person.
     * 
     * @param name the person's name
     * @param startDate the current simulation date
     */
	public PersonAgent(String name, Date startDate) {
		super();
		this.name = name;
		this.date = startDate;
		this.lastAteAtRestaurant = startDate;
		
		// TODO construct ResidentRole
		// TODO construct BankCustomerRole
	}
	
	//==========//
	// Messages //
	//==========//
	
	@Override
	public void guiAtDestination() {
		atDestination.release();
	}
	
	//===========//
	// Scheduler //
	//===========//
	
	@Override
	protected boolean runScheduler() throws InterruptedException {
		//-------------------/
		// Central Scheduler /
		//-------------------/
		
		// Go to work

		if (state == State.goingToWork) {
			processTransporationArrival();
		}		
		if (state == State.goingToWork) {
			if (processTransporationArrival()) {
				occupation.setActive();
				state = State.atWork;
				return true;
			}
		} else if (shouldGoToWork()) {
			actGoToWork();
			return true;
		}
		
		// Leave work and go to daily tasks
		if (state == State.leavingWork) {
			if (!occupation.getActive()) {
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		} else if (shouldLeaveWork()) {
			// Must go to intermediary leavingWork state to give setInactive() time to finish working
			state = State.leavingWork;
			occupation.setInactive();
			return true;
		}
		
		// All the daily tasks are beneath here
		
		if (state == State.goingToBank) {
			if (processTransporationArrival()) {
				// TODO tell role how much to deposit/borrow/loan
				bankCustomerRole.setActive();
				state = State.atBank;
				return true;
			}
		}
		if (state == State.atBank) {
			if (!bankCustomerRole.getActive()) {
				// The role persists, it's already inactive, so don't change or remove it
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		}
		if (state == State.goingToPayRent) {
			if (processTransporationArrival()) {
				residentRole.setActive();
				state = State.atRentPayment;
				return true;
			}
		}
		if (state == State.atRentPayment) {
			if (!residentRole.getActive()) {
				// The role persists, it's already inactive, so don't change or remove it
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		}
		if (state == State.goingToRestaurant) {
			if (processTransporationArrival()) {
				restaurantCustomerRole.setActive();
				state = State.atRestaurant;
				return true;
			}
		}
		if (state == State.atRestaurant) {
			if (!restaurantCustomerRole.getActive()) {
				roles.remove(restaurantCustomerRole);
				restaurantCustomerRole = null;
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		}
		
		// Otherwise sleep
		
		//----------------/
		// Role Scheduler /
		//----------------/

		boolean blocking = false;
		for (Role r : roles) if (r.getActive() && r.getActivity()) {
			blocking  = true;
			boolean activity = r.runScheduler();
			if (!activity) {
				r.setActivityFinished();
			}
			break;
		}
		
		// Scheduler disposition
		return blocking;
	}
	
	//=========//
	// Actions //
	//=========//
	
	/**
	 * Sends the person by car or bus to their workplace.
	 * 
	 * @throws InterruptedException 
	 */
	private void actGoToWork() throws InterruptedException {
		processTransportationDeparture(workplace);
		state = State.goingToWork;
	}
	
	/**
	 * Sends the person by car or bus to the bank closest to them.
	 * 
	 * @throws InterruptedException
	 */
	private void actGoToBank() throws InterruptedException {
		BankBuilding b = (BankBuilding) CityMap.findClosestBuilding(BUILDING.bank, this);
		processTransportationDeparture(b);
		state = State.goingToBank;
	}
	
	/**
	 * Sends the person home where they'll pay their rent.
	 * 
	 * @throws InterruptedException 
	 */
	private void actGoToPayRent() throws InterruptedException {
		processTransportationDeparture(home);
		state = State.goingToPayRent;
	}
	
	/**
	 * Sends the person by car or bus to the market closest to them.
	 * 
	 * @throws InterruptedException
	 */
	private void actGoToMarket() throws InterruptedException {
		MarketBuilding b = (MarketBuilding) CityMap.findClosestBuilding(BUILDING.market, this);
		processTransportationDeparture(b);
		state = State.goingToMarket;
	}
	
	/**
	 * Chooses a random restaurant and sends the person to it by car or bus
	 * 
	 * @throws InterruptedException
	 */
	private void actGoToRestaurant() throws InterruptedException { 
		Building b = CityMap.findRandomBuilding(BUILDING.restaurant);
		
		// Use reflection to get a Restaurant<name>CustomerRole to use when dining at the restaurant
		try {
			Class<?> c = Class.forName(b.getCustomerRole());
			Constructor<?> r = c.getConstructor();
			restaurantCustomerRole = (Role) r.newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		processTransportationDeparture(b);
		state = State.goingToRestaurant;
	}
	
	/**
	 * Sends the person by car or bus to their house.
	 * 
	 * @throws InterruptedException 
	 */
	private void actGoHome() throws InterruptedException {
		processTransportationDeparture(home);
		state = State.goingHome;
	}
	
	//=========//
	// Getters //
	//=========//
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public Date getDate() {
		return this.date;
	}
	
	@Override
	public int getSalary() {
		return occupation.getSalary();
	}
	
	public int getCash(){
		return cash;
	}
	
	//=========//
	// Setters //
	//=========//
	
	@Override
	public void setDate(Date d) {
		date = d;
		stateChanged();
	}
	
	@Override
	public void setOccupation(Role r) {
		occupation = r;
		addRole(r);
		r.setActive(); // TODO testing only - remove!!
		r.setActivityBegun(); // TODO testing only - remove!!
	}
	
	@Override
	public void setAnimation(city.animations.interfaces.AnimatedPerson p) {
		animation = p;
	}

	@Override
	public void setCar(Car c) {
		car = c;
	}
	
	@Override
	public void setWorkplace(Building b) {
		workplace = b;
	}
	
	@Override
	public void setCash(int c) {
		cash = c;
	}
	
	//===========//
	// Utilities //
	//===========//
	
	@Override
	public void addRole(Role r) {
		roles.add(r);
		r.setPerson(this);
	}
	
	/**
	 * Sends the person by car or bus to the selected destination
	 * 
	 * @throws InterruptedException 
	 * @param destination the building to travel to
	 */
	private void processTransportationDeparture(Building destination) throws InterruptedException {
		if (car != null) {
			carPassengerRole = new CarPassengerRole(car, destination);
			carPassengerRole.setActive();
			carPassengerRole.setPerson(this);
			roles.add(carPassengerRole);
		} else {
			BusStopBuilding b = (BusStopBuilding) CityMap.findClosestBuilding(BUILDING.busStop, this);
			BusStopBuilding d = (BusStopBuilding) CityMap.findClosestBuilding(BUILDING.busStop, destination);
			animation.goToBusStop(b);
			atDestination.acquire();
			busPassengerRole = new BusPassengerRole(d, b);
			busPassengerRole.setActive();
			busPassengerRole.setPerson(this);
			roles.add(busPassengerRole);
		}
	}
	
	/**
	 * Checks whether a car or bus is finished transporting for the scheduler.
	 * 
	 * Determines whether a person has arrived by car or bus. If they have,
	 * returns true so that the scheduler can activate the new role and state.
	 * 
	 * @param r the role to make active on arrival
	 * @param s the state to select on arrival
	 */
	private boolean processTransporationArrival() {
		if (car != null && !carPassengerRole.getActive()) {
			roles.remove(carPassengerRole);
			carPassengerRole = null;
			return true;
		} else if (!busPassengerRole.getActive()) {
			roles.remove(busPassengerRole);
			busPassengerRole = null;
			return true;
		}
		return false;
	}
	
	/**
	 * After a user is done working for the day, this decides what they'll do next.
	 * 
	 * Not all daily tasks will need to be performed. Daily tasks go in order: bank, 
	 * paying rent/maintenance, eating (goes to restaurant or market as needed), sleep
	 * 
	 * @return a State to indicate which daily task to perform
	 */
	private State pickDailyTask() {
		State disposition = State.none;
		if (shouldGoToBank()) {
			disposition = State.goingToBank;
		} else if (shouldPayLandlord()) {
			disposition = State.goingToPayRent;
		} else if (shouldGoToRestaurant()) {
			disposition = State.goingToRestaurant;
		} else if (shouldGoToMarket()) {
			disposition = State.goingToMarket; // TODO Does the person need to go home first to check the fridge?
		}
		return disposition;
	}
	
	/**
	 * Takes the current state and performs an action based on that state.
	 * 
	 * @throws InterruptedException 
	 */
	private void performDailyTaskAction() throws InterruptedException {
		switch(state) {
			case goingToBank:
				actGoToBank();
				break;
			case goingToPayRent:
				actGoToPayRent();
				break;
			case goingToRestaurant:
				actGoToRestaurant();
				break;
			case goingToMarket:
				actGoToMarket();
				break;
			case goingHome:
				actGoHome();
				break;
			default:
				break;
		}
	}
	
	/**
	 * If the person is not at work and the current time is at or within
	 * their working hours, then the person should go to work.
	 * 
	 * @return true if the person should go to work
	 */
	private boolean shouldGoToWork() {
		boolean disposition = false;
		if (!occupation.getActive() && inShiftRange()) {
			disposition = true;
		}
		return disposition;
	}
	
	/**
	 * Returns true if the person should go to the bank.
	 * 
	 * If the person has a lot of cash and rent is not due, it will
	 * go and deposit the cash. If rent is due and it does not have
	 * enough cash, it will go and take a withdrawal or a loan.
	 */
	private boolean shouldGoToBank() {
		boolean disposition = false;
		if (cash >= BANK_DEPOSIT_THRESHOLD) { disposition = true; }
		if (residentRole.rentIsDue() && cash < RENT_MAX_THRESHOLD) { disposition = true; }
		if (residentRole.rentIsDue() && cash > RENT_MIN_THRESHOLD) { disposition = false; }
		return disposition;
	}
	
	/**
	 * Returns true if the person should pay the landlord their rent/maintenance.
	 */
	private boolean shouldPayLandlord() {
		return residentRole.rentIsDue();
	}
	
	/**
	 * Returns true if the person chooses to eat at a restaurant.
	 * 
	 * The decision is made based on the cash the person has on hand and/or
	 * the time since the person last ate at a restaurant.
	 */
	private boolean shouldGoToRestaurant() {
		// Calculations
		Date thresholdDate = new Date(0);
		thresholdDate.setTime(lastAteAtRestaurant.getTime() + RESTAURANT_DINING_INTERVAL);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int today = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(thresholdDate);
		int threshold = c.get(Calendar.DAY_OF_YEAR);
		
		// Decision
		boolean disposition = false;
		if (cash >= RESTAURANT_DINING_THRESHOLD) { disposition = true; }
		if (today >= threshold) { disposition = true; }
		
		return disposition;
	}
	
	/**
	 * Returns true if the person needs to visit the market to buy food for cooking at home.
	 */
	private boolean shouldGoToMarket() { // TODO
		return false;
	}
	
	/**
	 * If the person is at work and the current time is outside their
	 * working hours, then the person should leave work.
	 * 
	 * @return true if 
	 */
	private boolean shouldLeaveWork() {
		boolean disposition = false;
		if (occupation.getActive() && !inShiftRange()) {
			disposition = true;
		}
		return disposition;
	}
	
	/**
	 * Tests whether the current time is at or within the occupation's working hours
	 * 
	 * @param r the role object representing the person's occupation
	 * @return true if the current time is at or within their working hours
	 */
	private boolean inShiftRange() {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int time = c.get(Calendar.HOUR_OF_DAY);
		
		boolean disposition = false;
		// TODO do we need to give the person enough time to get to work?
		if (time >= occupation.getShiftStart() && time <= occupation.getShiftEnd()) {
			disposition = true;
		}
		return disposition;
	}
}
