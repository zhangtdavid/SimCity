package city.agents;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TimeZone;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import utilities.MarketOrder;
import city.Agent;
import city.Application;
import city.Application.BANK_SERVICE;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Application.FOOD_ITEMS;
import city.Application.TRANSACTION_TYPE;
import city.BuildingInterface;
import city.RoleInterface;
import city.abstracts.ResidenceBuildingInterface;
import city.animations.interfaces.AnimatedPerson;
import city.interfaces.Bank;
import city.interfaces.BankCustomer;
import city.interfaces.BusPassenger;
import city.interfaces.BusStop;
import city.interfaces.Car;
import city.interfaces.CarPassenger;
import city.interfaces.Market;
import city.interfaces.MarketCustomer;
import city.interfaces.Person;
import city.interfaces.Resident;
import city.roles.BankCustomerRole;
import city.roles.BusPassengerRole;
import city.roles.CarPassengerRole;
import city.roles.MarketCustomerRole;
import city.roles.ResidentRole;

public class PersonAgent extends Agent implements Person {
	
	// Data
	
	private Date date;
	private RoleInterface occupation;
	private ResidenceBuildingInterface home;
	private Car car;
	private CarPassenger carPassengerRole; // not retained
	private BusPassenger busPassengerRole; // not retained
	private BankCustomer bankCustomerRole; // retained
	private Resident residentRole; // retained
	private RoleInterface restaurantCustomerRole; // not retained
	private MarketCustomer marketCustomerRole; // not retained
	private Date lastAteAtRestaurant;
	private Date lastWentToSleep;
	private String name;
	private ArrayList<RoleInterface> roles = new ArrayList<RoleInterface>();
	private Semaphore atDestination = new Semaphore(0, true);
	private AnimatedPerson animation;
	private STATE state; 
	private int cash;
	private boolean hasEaten;
	
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
		this.date = new Date(startDate.getTime());
		this.lastAteAtRestaurant = new Date(startDate.getTime());
		this.lastWentToSleep = new Date(startDate.getTime());
		this.state = STATE.none;
		this.cash = 0;
		this.hasEaten = false;
		
		residentRole = new ResidentRole(new Date(startDate.getTime()));
		bankCustomerRole = new BankCustomerRole((Bank)(Application.CityMap.findRandomBuilding(BUILDING.bank))); // TODO Get replace with correct constructor
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
	public boolean runScheduler() throws InterruptedException {
		//-------------------/
		// Central Scheduler /
		//-------------------/
		
		// Go to work	
		if (state == STATE.goingToWork) {
			if (processTransportationArrival()) {
				occupation.setActive();
				state = STATE.atWork;
				return true;
			}
		} else if (shouldGoToWork()) {
			actGoToWork();
			return true;
		}
		
		// Leave work and go to daily tasks
		if (state == STATE.leavingWork) {
			if (!occupation.getActive()) {
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		} else if (state == STATE.atWork && shouldLeaveWork()) {
			// Must go to intermediary leavingWork state to give setInactive() time to finish working
			state = STATE.leavingWork;
			occupation.setInactive();
			occupation.setActivityBegun();
			return true;
		}
		
		// All the daily tasks are beneath here
		
		if (state == STATE.goingToBank) {
			if (processTransportationArrival()) {
				// Calculate which service to use
				BANK_SERVICE choice = BANK_SERVICE.none;
				int money = 0;
				if (cash >= BANK_DEPOSIT_THRESHOLD) { 
					choice = BANK_SERVICE.atmDeposit; 
					money = BANK_DEPOSIT_SUM;
				} 
				if (residentRole.rentIsDue()) { 
					choice = BANK_SERVICE.moneyWithdraw; 
					money = RENT_MIN_THRESHOLD;
				}
				
				// Start the role
				bankCustomerRole.setActive(choice, money, TRANSACTION_TYPE.personal);
				state = STATE.atBank;
				return true;
			}
		}
		if (state == STATE.atBank) {
			if (!bankCustomerRole.getActive()) {
				// The role persists, it's already inactive, so don't change or remove it
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		}
		if (state == STATE.goingToPayRent) {
			if (processTransportationArrival()) {
				residentRole.setActive();
				state = STATE.atRentPayment;
				return true;
			}
		}
		if (state == STATE.atRentPayment) {
			if (!residentRole.getActive()) {
				// The role persists, it's already inactive, so don't change or remove it
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		}
		if (state == STATE.goingToRestaurant) {
			if (processTransportationArrival()) {
				restaurantCustomerRole.setActive();
				state = STATE.atRestaurant;
				return true;
			}
		}
		if (state == STATE.atRestaurant) {
			if (!restaurantCustomerRole.getActive()) {
				roles.remove(restaurantCustomerRole);
				restaurantCustomerRole = null;
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		}
		if (state == STATE.goingToMarket) {
			if (processTransportationArrival()) {
				marketCustomerRole.setActive();
				state = STATE.atMarket;
				return true;
			}
		}
		if (state == STATE.atMarket) {
			if (!marketCustomerRole.getActive()) {
				roles.remove(marketCustomerRole);
				marketCustomerRole = null;
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		}
		if (state == STATE.goingToCook) {
			if (processTransportationArrival()) {
				state = STATE.atCooking;
				actCookAndEatFood();
				return true;
			}
		}
		if (state == STATE.atCooking) {
			atDestination.acquire();
			state = pickDailyTask();
			performDailyTaskAction();
			return true;
		}
		if (state == STATE.goingToSleep) {
			if (processTransportationArrival()) {
				animation.goToSleep();
				state = STATE.atSleep;
				return false;
			}
		}
		if (state == STATE.atSleep) {
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
		if (state == STATE.none) {
			// If state == STATE.none (the condition at PersonAgent creation) we don't want anything special to 
			// happen. The person either has a first-shift job and will go to work, or the person has a
			// second-shift job and will begin daily tasks.	
			state = pickDailyTask();
			performDailyTaskAction();
			return true;
		}
		
		//----------------/
		// Role Scheduler /
		//----------------/
		
		boolean blocking = false;
		for (RoleInterface r : roles) if (r.getActive() && r.getActivity()) {
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
		processTransportationDeparture(occupation.getWorkplace(BuildingInterface.class));
		state = STATE.goingToWork;
	}
	
	/**
	 * Sends the person by car or bus to the bank closest to them.
	 * 
	 * @throws InterruptedException
	 */
	private void actGoToBank() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		Bank b = (Bank) Application.CityMap.findClosestBuilding(BUILDING.bank, this);
		processTransportationDeparture(b);
		state = STATE.goingToBank;
	}
	
	/**
	 * Sends the person home where they'll pay their rent.
	 * 
	 * @throws InterruptedException 
	 */
	private void actGoToPayRent() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		processTransportationDeparture((ResidenceBuildingInterface) home);
		state = STATE.goingToPayRent;
	}
	
	/**
	 * Chooses a random restaurant and sends the person to it by car or bus
	 * 
	 * @throws InterruptedException
	 */
	private void actGoToRestaurant() throws InterruptedException { 
		// Thread.sleep(9000); // TODO testing - Delay the customer entering
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		BuildingInterface building = Application.CityMap.findRandomBuilding(BUILDING.restaurant);
		this.lastAteAtRestaurant = this.date;
		this.hasEaten = true;
		
		// Use reflection to get a Restaurant<name>CustomerRole to use when dining at the restaurant
		try {
			Class<?> c0 = Class.forName(building.getCustomerRoleName());
			Constructor<?> r0 = c0.getConstructor();
			restaurantCustomerRole = (RoleInterface) r0.newInstance();
			building.addOccupyingRole(restaurantCustomerRole);
			this.addRole(restaurantCustomerRole);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		processTransportationDeparture(building);
		state = STATE.goingToRestaurant;
	}
	
	/**
	 * Sends the person by car or bus to the market closest to them.
	 * 
	 * @throws InterruptedException
	 */
	private void actGoToMarket() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		Market m = (Market) Application.CityMap.findClosestBuilding(BUILDING.market, this);
		processTransportationDeparture(m);
		state = STATE.goingToMarket;
		
		// Orders one of each of four random food items
		HashMap<FOOD_ITEMS, Integer> items = new HashMap<FOOD_ITEMS, Integer>();
		LinkedList<FOOD_ITEMS> list = new LinkedList<FOOD_ITEMS>(Arrays.asList(FOOD_ITEMS.values()));
		Collections.shuffle(list);
		int i = 0;
		do {
			FOOD_ITEMS f = list.get(0);
			list.remove(0);
			items.put(f, 1);
			i += 1;
		} while (i < 4);
		MarketOrder order = new MarketOrder(items);
		marketCustomerRole = new MarketCustomerRole(order);
		m.addOccupyingRole(marketCustomerRole);
		this.addRole(marketCustomerRole);
	}
	
	/**
	 * Takes the person home so that they can cook and eat a meal.
	 */
	private void actGoToCook() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		processTransportationDeparture((BuildingInterface) home);
		state = STATE.goingToCook;
	}
	
	/**
	 * Has the person cook and eat a meal at home.
	 * 
	 * @throws InterruptedException 
	 */
	private void actCookAndEatFood() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.hasEaten = true;
		animation.cookAndEatFood();
		// The scheduler will pick up the atDestination and continue the program
	}
	
	/**
	 * Takes the person home and allows them to "sleep" until awakened for work.
	 * 
	 * Sets hasEaten to false, since it's effectively the beginning of a new day.
	 * 
	 * @throws InterruptedException 
	 */
	private void actGoToSleep() throws InterruptedException {
		// TODO this shouldn't transport them home if they're already at home
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		this.hasEaten = false;
		processTransportationDeparture((BuildingInterface) home);
		lastWentToSleep = this.getDate();
		state = STATE.goingToSleep;
	}
	
	//=========//
	// Getters //
	//=========//
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public STATE getState() {
		return this.state;
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
	public ResidenceBuildingInterface getHome() {
		return home;
	}
	
	@Override
	public Car getCar() {
		return car;
	}
	
	@Override
	public RoleInterface getOccupation() {
		return occupation;
	}
	
	@Override
	public CarPassenger getCarPassengerRole() {
		return carPassengerRole;
	}

	@Override
	public BusPassenger getBusPassengerRole() {
		return busPassengerRole;
	}

	@Override
	public BankCustomer getBankCustomerRole() {
		return bankCustomerRole;
	}

	@Override
	public MarketCustomer getMarketCustomerRole() {
		return marketCustomerRole;
	}

	@Override
	public RoleInterface getRestaurantCustomerRole() {
		return restaurantCustomerRole;
	}

	@Override
	public ArrayList<RoleInterface> getRoles() {
		return roles;
	}
	
	@Override
	public boolean getHasEaten() {
		return hasEaten;
	}
	
	@Override
	public Resident getResidentRole() {
		return residentRole;
	}
	
	@Override
	public AnimatedPerson getAnimation() {
		return animation;
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
	public void setOccupation(RoleInterface r) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		occupation = r;
		if (r != null) { addRole(r); }
	}
	
	@Override
	public void setAnimation(AnimatedPerson p) {
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
	public void setHome(ResidenceBuildingInterface h) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		home = h;
	}
	
	//===========//
	// Utilities //
	//===========//
	
	@Override
	public void addRole(RoleInterface r) {
		r.setPerson(this); // Order is important here. Many roles expect to have a person set.
		roles.add(r);
	}
	
	/**
	 * Sends the person by car or bus to the selected destination
	 * 
	 * @throws InterruptedException 
	 * @param destination the building to travel to
	 */
	private void processTransportationDeparture(BuildingInterface destination) throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		if (car != null) {
			carPassengerRole = new CarPassengerRole(car, destination);
			carPassengerRole.setActive();
			carPassengerRole.setPerson(this);
			this.addRole(carPassengerRole);
		} else {
			BusStop b = (BusStop) CityMap.findClosestBuilding(BUILDING.busStop, this);
			BusStop d = (BusStop) CityMap.findClosestBuilding(BUILDING.busStop, destination);
// TODO
//			animation.goToBusStop(b);
//			atDestination.acquire();
			busPassengerRole = new BusPassengerRole(d, b);
			busPassengerRole.setPerson(this);
			busPassengerRole.setActive();
			this.addRole(busPassengerRole);
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
		if (car != null && carPassengerRole != null) {
			if(!carPassengerRole.getActive()) {
				roles.remove(carPassengerRole);
				carPassengerRole = null;
				return true;
			}
		} else if (busPassengerRole != null && busPassengerRole != null) {
			if(!busPassengerRole.getActive()) {
				roles.remove(busPassengerRole);
				busPassengerRole = null;
				return true;
			}
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
	private STATE pickDailyTask() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		STATE disposition = STATE.none;
		if (shouldGoToBank()) {
			disposition = STATE.goingToBank;
		} else if (shouldPayLandlord()) {
			disposition = STATE.goingToPayRent;
		} else if (shouldGoToRestaurant()) {
			disposition = STATE.goingToRestaurant;
		} else if (shouldGoToMarket()) {
			disposition = STATE.goingToMarket;
		} else if (shouldGoToCook()) {
			disposition = STATE.goingToCook;
		} else {
			disposition = STATE.goingToSleep;
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
//		if (this.name != "Landlord") {
//			disposition = false;
//		}
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
		if (residentRole.rentIsDue() && cash < RENT_MIN_THRESHOLD) { disposition = true; }
		if (residentRole.rentIsDue() && cash >= RENT_MIN_THRESHOLD) { disposition = false; }
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
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.setTime(date);
		int today = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(thresholdDate);
		int threshold = c.get(Calendar.DAY_OF_YEAR);
		
		// Decision
		boolean disposition = false;
		if (cash >= RESTAURANT_DINING_THRESHOLD) { disposition = true; }
		if (today >= threshold) { disposition = true; }
		if (this.hasEaten) { disposition = false; }
		
		return disposition;
	}
	
	/**
	 * Returns true if the person needs to visit the market to buy food for cooking at home.
	 * 
	 * The person will visit the market if they have 3 or fewer items in their home. 
	 */
	private boolean shouldGoToMarket() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean disposition = true;
		int items = 0;
		for (FOOD_ITEMS i : home.getFoodItems().keySet()) {
			items = items + home.getFoodItems().get(i);
			if (items > 3) { 
				disposition = false;
				break;
			}
		}
		return disposition;
	}
	
	/**
	 * Returns true if the person should go home to cook their food.
	 * 
	 * Since eating at home is lower in priority in the task picker than going 
	 * to a restaurant, the person will automatically go home to cook if 
	 * shouldGoToCook() is called and the person has not eaten.
	 * 
	 * Though the person can go to the market after going to a restaurant, the 
	 * person will not go home to cook.
	 */
	private boolean shouldGoToCook() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean disposition = true;
		if (hasEaten) { disposition = false; }
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
		if (date.getTime() >= thresholdDate.getTime()) { disposition = true; }
		return disposition;
	}
	
	/**
	 * Tests whether the current time is at or within the occupation's working hours
	 * 
	 * @param r the role object representing the person's occupation
	 * @return true if the current time is at or within their working hours
	 */
	private boolean inShiftRange() {
		// TODO do we need to give the person enough time to get to work?
		
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.setTime(date);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		boolean disposition = false;
		// Covers any range starting at 0 or above and going to 23
		if (hour >= occupation.getShiftStart() && hour <= occupation.getShiftEnd()) {
			disposition = true;
		}
		// Covers any range starting at 0 or above and ending at 0
		if (hour >= occupation.getShiftStart() && occupation.getShiftEnd() == 0 && hour != 0) {
			disposition = true;
		}
		// Shifts end at exactly shiftEnd
		if (hour == occupation.getShiftEnd() && minute >= 0) {
			disposition = false;
		}
		return disposition;
	}
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.PERSON, "PersonAgent " + this.name, msg);
    }
}
