package city.agents;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import utilities.MarketOrder;
import city.Agent;
import city.Animation;
import city.Application;
import city.Application.BANK_SERVICE;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Application.FOOD_ITEMS;
import city.Application.TRANSACTION_TYPE;
import city.Building;
import city.Role;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.MarketBuilding;
import city.buildings.ResidenceBaseBuilding;
import city.buildings.RestaurantTimmsBuilding;
import city.interfaces.Car;
import city.interfaces.Person;
import city.roles.BankCustomerRole;
import city.roles.BusPassengerRole;
import city.roles.CarPassengerRole;
import city.roles.MarketCustomerRole;
import city.roles.ResidentRole;

public class PersonAgent extends Agent implements Person {
	
	// Data
	
	private Date date;
	private Role occupation;
	private ResidenceBaseBuilding home;
	private Car car;
	private CarPassengerRole carPassengerRole; // not retained
	private BusPassengerRole busPassengerRole; // not retained
	private BankCustomerRole bankCustomerRole; // retained
	private ResidentRole residentRole; // retained
	private Role restaurantCustomerRole; // not retained
	private MarketCustomerRole marketCustomerRole; // not retained
	private Date lastAteAtRestaurant;
	private Date lastWentToSleep;
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
		this.lastWentToSleep = startDate;
		this.state = State.none;
		
		residentRole = new ResidentRole(startDate);
		bankCustomerRole = new BankCustomerRole();
		this.addRole(residentRole);
		this.addRole(bankCustomerRole);
	}
	
	//==========//
	// Messages //
	//==========//
	
	@Override
	public void guiAtDestination() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
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
			if (processTransportationArrival()) {
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
		} else if (state == State.atWork && shouldLeaveWork()) {
			// Must go to intermediary leavingWork state to give setInactive() time to finish working
			state = State.leavingWork;
			occupation.setInactive();
			return true;
		}
		
		// All the daily tasks are beneath here
		
		if (state == State.goingToBank) {
			if (processTransportationArrival()) {
				// Calculate which service to use
				BANK_SERVICE choice = BANK_SERVICE.none;
				int money = 0;
				if (cash >= BANK_DEPOSIT_THRESHOLD) { 
					choice = BANK_SERVICE.atmDeposit; 
					money = BANK_DEPOSIT_SUM;
				} else if (residentRole.rentIsDue() && cash < RENT_MAX_THRESHOLD) { 
					choice = BANK_SERVICE.moneyWithdraw; 
					money = RENT_MIN_THRESHOLD;
				}
				
				// Start the role
				bankCustomerRole.setActive(choice, money, TRANSACTION_TYPE.personal);
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
			if (processTransportationArrival()) {
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
			if (processTransportationArrival()) {
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
		if (state == State.goingToMarket) {
			if (processTransportationArrival()) {
				marketCustomerRole.setActive();
				state = State.atMarket;
				return true;
			}
		}
		if (state == State.atMarket) {
			if (!marketCustomerRole.getActive()) {
				roles.remove(marketCustomerRole);
				marketCustomerRole = null;
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		}
		if (state == State.goingToCook) {
			if (processTransportationArrival()) {
				// actCookAndEatFood() will run the daily task scheduler
				state = State.atCooking;
				actCookAndEatFood();
				return true;
			}
		}
		if (state == State.goingToSleep) {
			if (processTransportationArrival()) {
				animation.goToSleep();
				state = State.atSleep;
				return false;
			}
		}
		if (state == State.atSleep || state == State.none) { // TODO needs testing. 
			// Some people don't have jobs. This will ensure that they eventually wake up and do daily tasks.
			// This will also ensure that no roles can run while the person is sleeping.
			if (occupation == null) {
				if (shouldWakeUp()) {
					state = pickDailyTask();
					performDailyTaskAction();
					return true;
				}
			}
			return false;
		}
		
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
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		processTransportationDeparture(occupation.getWorkplace(Building.class));
		state = State.goingToWork;
	}
	
	/**
	 * Sends the person by car or bus to the bank closest to them.
	 * 
	 * @throws InterruptedException
	 */
	private void actGoToBank() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
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
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		processTransportationDeparture(home);
		state = State.goingToPayRent;
	}
	
	/**
	 * Chooses a random restaurant and sends the person to it by car or bus
	 * 
	 * @throws InterruptedException
	 */
	private void actGoToRestaurant() throws InterruptedException { 
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		Building b = CityMap.findRandomBuilding(BUILDING.restaurant);
		
		// Use reflection to get a Restaurant<name>CustomerRole to use when dining at the restaurant
		try {
			Class<?> c0 = Class.forName(b.getCustomerRole());
			Constructor<?> r0 = c0.getConstructor();
			restaurantCustomerRole = (Role) r0.newInstance();
			b.addRole(restaurantCustomerRole);
			addRole(restaurantCustomerRole);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		processTransportationDeparture(b);
		state = State.goingToRestaurant;
	}
	
	/**
	 * Sends the person by car or bus to the market closest to them.
	 * 
	 * @throws InterruptedException
	 */
	private void actGoToMarket() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		MarketBuilding b = (MarketBuilding) CityMap.findClosestBuilding(BUILDING.market, this);
		processTransportationDeparture(b);
		state = State.goingToMarket;
		
		// TODO construct an actual order
		HashMap<FOOD_ITEMS, Integer> items = null;
		MarketOrder order = new MarketOrder(items);
		marketCustomerRole = new MarketCustomerRole(order);
	}
	
	/**
	 * Takes the person home so that they can cook and eat a meal.
	 */
	private void actGoToCook() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		processTransportationDeparture(home);
		state = State.goingToCook;
	}
	
	/**
	 * Has the person cook and eat a meal at home.
	 * 
	 * @throws InterruptedException 
	 */
	private void actCookAndEatFood() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		animation.cookAndEatFood();
		atDestination.acquire();
		state = pickDailyTask();
		performDailyTaskAction();
		// Expected to go to sleep next
	}
	
	/**
	 * Takes the person home and allows them to "sleep" until awakened for work.
	 * 
	 * @throws InterruptedException 
	 */
	private void actGoToSleep() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		processTransportationDeparture(home);
		state = State.goingToSleep;
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
	public int getCash(){
		return cash;
	}
	
	@Override
	public ResidenceBaseBuilding getHome() {
		return home;
	}
	
	@Override
	public Role getOccupation() {
		return occupation;
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
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		occupation = r;
		addRole(r);
	}
	
	@Override
	public void setAnimation(city.animations.interfaces.AnimatedPerson p) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		animation = p;
	}

	@Override
	public void setCar(Car c) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		car = c;
	}
	
	@Override
	public void setCash(int c) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		cash = c;
	}
	
	@Override
	public void setHome(ResidenceBaseBuilding h) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		home = h;
	}
	
	//===========//
	// Utilities //
	//===========//
	
	@Override
	public void addRole(Role r) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		r.setPerson(this); // Order is important here. Many roles expect to have a person set.
		roles.add(r);
	}
	
	/**
	 * Sends the person by car or bus to the selected destination
	 * 
	 * @throws InterruptedException 
	 * @param destination the building to travel to
	 */
	private void processTransportationDeparture(Building destination) throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
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
	private boolean processTransportationArrival() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		if (car != null && !carPassengerRole.getActive()) {
			roles.remove(carPassengerRole);
			carPassengerRole = null;
			return true;
		} else if (busPassengerRole != null && !busPassengerRole.getActive()) {
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
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		State disposition = State.none;
		if (shouldGoToBank()) {
			disposition = State.goingToBank;
		} else if (shouldPayLandlord()) {
			disposition = State.goingToPayRent;
		} else if (shouldGoToRestaurant()) {
			disposition = State.goingToRestaurant;
		} else if (shouldGoToMarket()) {
			disposition = State.goingToMarket;
		} else if (shouldGoToCook()) {
			disposition = State.goingToCook;
		} else {
			disposition = State.goingToSleep;
		}
		return disposition;
	}
	
	/**
	 * Takes the current state and performs an action based on that state.
	 * 
	 * @throws InterruptedException 
	 */
	private void performDailyTaskAction() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
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
			case goingToCook:
				actGoToCook();
				break;
			case goingToSleep:
				actGoToSleep();
				break;
			default:
				break;
		}
	}
	
	/**
	 * If the person is not at work and the current time is at or within
	 * their working hours, then the person should go to work.
	 * 
	 * Supports people not having jobs, in which case
	 * 
	 * @return true if the person should go to work
	 */
	private boolean shouldGoToWork() {
		boolean disposition = false;
		if (occupation != null && !occupation.getActive() && inShiftRange()) {
			disposition = true;
		}
		return disposition;
	}
	
	/**
	 * If the person is at work and the current time is outside their
	 * working hours, then the person should leave work.
	 * 
	 * @return true if 
	 */
	private boolean shouldLeaveWork() {
		boolean disposition = false;
		if (occupation != null && occupation.getActive() && !inShiftRange()) {
			disposition = true;
		}
		// TODO testing
		if (this.name != "Landlord") {
			disposition = false;
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
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
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
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		return residentRole.rentIsDue();
	}
	
	/**
	 * Returns true if the person chooses to eat at a restaurant.
	 * 
	 * The decision is made based on the cash the person has on hand and/or
	 * the time since the person last ate at a restaurant.
	 */
	private boolean shouldGoToRestaurant() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
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
	 * 
	 * The person will visit the market if they have 3 or fewer items in their home. 
	 */
	private boolean shouldGoToMarket() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean disposition = false;
		int items = 0;
		for (FOOD_ITEMS i : home.foodItems.keySet()) {
			items = items + home.foodItems.get(i);
		}
		if (items <= 3) { disposition = true; }
		return disposition;
	}
	
	/**
	 * Returns true if the person should go home to cook their food.
	 * 
	 * Should be called only after visiting a market, and will only go to cook
	 * if a market has just been visited.
	 */
	private boolean shouldGoToCook() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean disposition = false;
		if (state == State.atMarket) { disposition = true; }
		return disposition;
	}
	
	/**
	 * Returns true if the person should wake up. Only called for persons who don't have jobs.
	 */
	private boolean shouldWakeUp() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		// Calculations
		Date thresholdDate = new Date(0);
		thresholdDate.setTime(lastWentToSleep.getTime() + WAKE_UP_THRESHOLD);
		
		// Decision
		boolean disposition = false;
		if (thresholdDate.getTime() >= date.getTime()) { disposition = true; }
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
