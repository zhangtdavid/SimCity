package city.agents;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.Agent;
import city.Application;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Building;
import city.Role;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.HouseBuilding;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantTimmsBuilding;
import city.interfaces.Car;
import city.interfaces.Person;
import city.roles.BankCustomerRole;
import city.roles.BusPassengerRole;
import city.roles.CarPassengerRole;
import city.roles.ResidentRole;

public class PersonAgent extends Agent implements Person {
	
	// Data
	
	private Date date;
	private Date rentLastPaid;
	private Role occupation;
	private Building workplace;
	private HouseBuilding home;
	private Car car;
	private CarPassengerRole carPassengerRole;
	private BusPassengerRole busPassengerRole;
	private BankCustomerRole bankCustomerRole;
	private ResidentRole residentRole;
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
		// TODO work with Ryan to ensure that the rent last paid date is updated, etc.
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
	 * Chooses a restaurant (based on some criteria) and sends the person to it by car or bus
	 * 
	 * @throws InterruptedException
	 */
	private void actGoToRestaurant() throws InterruptedException { 
		Building b = new RestaurantTimmsBuilding("placeholder"); // TODO make it choose a restaurant on some criteria
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
		if (rentIsDue() && cash < RENT_MAX_THRESHOLD) { disposition = true; }
		if (rentIsDue() && cash > RENT_MIN_THRESHOLD) { disposition = false; }
		return disposition;
	}
	
	/**
	 * Returns true if the person should pay the landlord their rent/maintenance.
	 */
	private boolean shouldPayLandlord() {
		return rentIsDue();
	}
	
	/**
	 * Returns true if the person chooses to eat at a restaurant.
	 * 
	 * The decision is made based on the cash the person has on hand and/or
	 * the time since the person last ate at a restaurant.
	 */
	private boolean shouldGoToRestaurant() { // TODO
		return false;
	}
	
	/**
	 * 
	 */
	private boolean shouldGoToMarket() { // TODO
		return false;
	}
	
	/**
	 * Returns true if today is the day that rent is due
	 * 
	 * @return
	 */
	private boolean rentIsDue() {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(rentDueDate());
		int due = c.get(Calendar.DAY_OF_YEAR);
		return (day == due);
	}
	
	// TODO move elsewhere
	/**
	 * Takes the Date of the last rent payment and adds the set interval for
	 * the next payment to it, returning a Date of the exact time rent is due.
	 * 
	 * The interval (336) should be gotten from somewhere else.
	 * 
	 * @return
	 */
	private Date rentDueDate() {
		long interval = (Application.INTERVAL * 336);
		Date dueDate = new Date(0);
		dueDate.setTime(rentLastPaid.getTime() + interval);
		return dueDate;
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
