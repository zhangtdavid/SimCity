package city.agents;

import java.beans.PropertyChangeSupport;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import utilities.MarketOrder;
import city.Application;
import city.Application.BANK_SERVICE;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Application.FOOD_ITEMS;
import city.Application.TRANSACTION_TYPE;
import city.agents.interfaces.Bus;
import city.agents.interfaces.Car;
import city.agents.interfaces.Person;
import city.animations.WalkerAnimation;
import city.animations.interfaces.AnimatedPerson;
import city.animations.interfaces.AnimatedWalker;
import city.bases.Agent;
import city.bases.ResidenceBuilding;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.JobRoleInterface;
import city.bases.interfaces.ResidenceBuildingInterface;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Apt;
import city.buildings.interfaces.Bank;
import city.buildings.interfaces.BusStop;
import city.buildings.interfaces.House;
import city.buildings.interfaces.Market;
import city.roles.BankCustomerRole;
import city.roles.BusPassengerRole;
import city.roles.CarPassengerRole;
import city.roles.MarketCustomerRole;
import city.roles.ResidentRole;
import city.roles.WalkerRole;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.BusPassenger;
import city.roles.interfaces.CarPassenger;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.MarketCustomer;
import city.roles.interfaces.Resident;
import city.roles.interfaces.Walker;

/**
 * The PersonAgent class.
 *
 * Notes for future reference:
 * - Whenever entering a building (other than a residence) remember to addOccupyingRole()
 *   This will usually need to be done in the scheduler. Thanks to processTransportationArrival
 *   you can just use currentLocation.addOccupyingRole(). This should be done along with setActive()
 * - The same applies when leaving a building, use removeOccupyingRole()
 * 
 * To-do:
 * - getBuildingIsOpen() is not implemented
 * 
 */
public class PersonAgent extends Agent implements Person {
	
	//==========//
	// Data     //
	//==========//
	
	private String name;
	private Date date;
	private STATES state; 
	private AnimatedPerson animation; 						// only used inside the person's residence
	private int cash;
	private Car car;
	private JobRoleInterface occupation;
	private ResidenceBuildingInterface home;
	private int roomNumber; 								// homes: 0, apartments: 1-5
	private Date lastAteAtRestaurant;
	private boolean hasEaten;
	private Date lastWentToSleep;
	private List<RoleInterface> roles = Collections.synchronizedList(new ArrayList<RoleInterface>());
	private RoleInterface restaurantCustomerRole; 			// not retained
	private MarketCustomer marketCustomerRole; 				// not retained
	private CarPassenger carPassengerRole; 					// not retained
	private BusPassenger busPassengerRole; 					// not retained
	private Walker walkerRole;								// Not retained
	private BankCustomer bankCustomerRole; 					// retained
	private Resident residentRole; 							// retained
	private Semaphore atDestination = new Semaphore(0, true);
	private PropertyChangeSupport propertyChangeSupport;
	private BuildingInterface currentLocation; 				// the building which the person is currently in
	
	//=============//
	// Constructor //
	//=============//
	
    /**
     * Create a Person.
     * 
     * The person's starting cash is 0. The person is created inside their home.
     * The person creates their own Resident and BankCustomer roles.
     * 
     * @param name The person's name
     * @param startDate The current simulation date
     * @param animation The animation object for the person inside their home
     * @param residence The building where the person lives
     */
	public PersonAgent(String name, Date startDate, AnimatedPerson animation, ResidenceBuildingInterface residence) {
		super();
		this.name = name;
		this.cash = 0;
		this.hasEaten = false;
		this.home = residence;
		this.currentLocation = residence;
		this.animation = animation;
		animation.setPerson(this);
		this.lastAteAtRestaurant = new Date(startDate.getTime());
		this.lastWentToSleep = new Date(startDate.getTime());
		this.date = new Date(startDate.getTime());
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		this.setState(STATES.none);

		residentRole = new ResidentRole(new Date(startDate.getTime()));
		bankCustomerRole = new BankCustomerRole((Bank)(Application.CityMap.findRandomBuilding(BUILDING.bank)));
		this.addRole(residentRole);
		this.addRole(bankCustomerRole);
		
		residence.addResident(residentRole);
		this.setHome(residence);
		this.animation.setPerson(this);
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
		if (state == STATES.goingToWork) {
			if (processTransportationArrival()) {
				// The occupation could have been removed while going to work
				if (occupation != null) {
					currentLocation.addOccupyingRole(occupation);
					occupation.setActive();
					setState(STATES.atWork);
				} else {
					setState(STATES.leavingWork);
				}
				return true;
			}
		} else if (shouldGoToWork()) {
			actGoToWork();
			return true;
		}

		// Leave work and go to daily tasks
		if (state == STATES.leavingWork) {
			if (occupation == null || !occupation.getActive()) {
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		} else if (state == STATES.atWork && shouldLeaveWork()) {
			// Must go to intermediary leavingWork state to give setInactive() time to finish working
			setState(STATES.leavingWork);
			currentLocation.removeOccupyingRole(occupation);
			occupation.setInactive();
			occupation.setActivityBegun();
			return true;
		}

		// All the daily tasks are beneath here

		if (state == STATES.goingToBank) {
			if (processTransportationArrival()) {
				// Calculate which service to use
				BANK_SERVICE choice = BANK_SERVICE.none;
				int money = 0;
				if (cash >= BANK_DEPOSIT_THRESHOLD) { 
					choice = BANK_SERVICE.deposit; 
					money = cash - BANK_DEPOSIT_THRESHOLD + 1;
				} 
				if (residentRole.rentIsDue()) { 
					choice = BANK_SERVICE.moneyWithdraw; 
					money = RENT_MIN_THRESHOLD;
				}

				// Start the role
				currentLocation.addOccupyingRole(bankCustomerRole);
				bankCustomerRole.setActive(choice, money, TRANSACTION_TYPE.personal);
				setState(STATES.atBank);
				return true;
			}
		}
		if (state == STATES.atBank) {
			if (!bankCustomerRole.getActive()) {
				// The role persists, it's already inactive, so don't change or remove it
				currentLocation.removeOccupyingRole(bankCustomerRole);
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		}
		if (state == STATES.goingToPayRent) {
			if (processTransportationArrival()) {
				// Do not addOccupyingRole since this action takes place
				// at home, where the standard PersonAnimation is used
				residentRole.setActive();
				setState(STATES.atRentPayment);
				return true;
			}
		}
		if (state == STATES.atRentPayment) {
			if (!residentRole.getActive()) {
				// The role persists, it's already inactive, so don't change or remove it
				// No need to removeOccupyingRole since we didn't add it above
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		}
		if (state == STATES.goingToRestaurant) {
			if (processTransportationArrival()) {
				currentLocation.addOccupyingRole(restaurantCustomerRole);
				restaurantCustomerRole.setActive();
				setState(STATES.atRestaurant);
				return true;
			}
		}
		if (state == STATES.atRestaurant) {
			if (!restaurantCustomerRole.getActive()) {
				currentLocation.removeOccupyingRole(restaurantCustomerRole);
				removeRole(restaurantCustomerRole);
				restaurantCustomerRole = null;
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		}
		if (state == STATES.goingToMarket) {
			if (processTransportationArrival()) {
				currentLocation.addOccupyingRole(marketCustomerRole);
				marketCustomerRole.setActive();
				setState(STATES.atMarket);
				return true;
			}
		}
		if (state == STATES.atMarket) {
			if (!marketCustomerRole.getActive()) {
				currentLocation.removeOccupyingRole(marketCustomerRole);
				removeRole(marketCustomerRole);
				marketCustomerRole = null;
				state = pickDailyTask();
				performDailyTaskAction();
				return true;
			}
		}
		if (state == STATES.goingToCook) {
			if (processTransportationArrival()) {
				actCookAndEatFood();
				return true;
			}
		}
		if (state == STATES.atCooking) {
			state = pickDailyTask();
			performDailyTaskAction();
			return true;
		}
		if (state == STATES.goingToSleep) {
			if (processTransportationArrival()) {
				actGoToBed();
				return false;
			}
		}
		if (state == STATES.atSleep) {
			// Some people don't have jobs. This will ensure that they eventually wake up and do daily tasks.
			// This will also ensure that no roles can run while the person is sleeping.
			if (occupation == null) {
				if (shouldWakeUp()) {
					actWakeUp();
					state = pickDailyTask();
					performDailyTaskAction();
					return true;
				}
			}
			return false;
		}
		if (state == STATES.none) {
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
		synchronized(roles) {
			for (RoleInterface r : roles) if (r.getActive() && r.getActivity()) {
				blocking  = true;
				boolean activity = r.runScheduler();
				if (!activity) {
					r.setActivityFinished();
				}
				break;
			}
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
		setState(STATES.goingToWork);
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
		setState(STATES.goingToBank);
	}

	/**
	 * Sends the person home where they'll pay their rent.
	 * 
	 * @throws InterruptedException 
	 */
	private void actGoToPayRent() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		processTransportationDeparture((ResidenceBuildingInterface) home);
		setState(STATES.goingToPayRent);
	}

	/**
	 * Chooses a random restaurant and sends the person to it by car or bus
	 * 
	 * @throws InterruptedException
	 */
	private void actGoToRestaurant() throws InterruptedException { 
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		BuildingInterface building = Application.CityMap.findRandomBuilding(BUILDING.restaurant);
		this.lastAteAtRestaurant = new Date(date.getTime());
		this.hasEaten = true;

		// Use reflection to get a Restaurant<name>CustomerRole to use when dining at the restaurant
		try {
			Class<?> c0 = Class.forName(building.getCustomerRoleName());
			Constructor<?> r0 = c0.getConstructor();
			restaurantCustomerRole = (RoleInterface) r0.newInstance();
			this.addRole(restaurantCustomerRole);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		processTransportationDeparture(building);
		setState(STATES.goingToRestaurant);
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
		setState(STATES.goingToMarket);

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
		marketCustomerRole = new MarketCustomerRole(m, order);
		this.addRole(marketCustomerRole);
	}

	/**
	 * Takes the person home so that they can cook and eat a meal.
	 */
	private void actGoToCook() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		processTransportationDeparture((BuildingInterface) home);
		setState(STATES.goingToCook);
	}

	/**
	 * Has the person cook and eat a meal at home.
	 * Assume they are already at home when this is called
	 * IMPORTANT: also assumes there is food in the refrigerator.
	 * @throws InterruptedException 
	 */
	private void actCookAndEatFood() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		setState(STATES.atCooking);
		// Pick a food item to eat from the refrigerator
		List<FOOD_ITEMS> list = new ArrayList<FOOD_ITEMS>();
		list.addAll(this.home.getFoodItems(this).keySet());
		Collections.shuffle(list);
		FOOD_ITEMS toEat = null;
		for (FOOD_ITEMS i : list) {
			if (this.home.getFoodItems(this).get(i) > 0) {
				toEat = i;
				break;
			}
		}
		if(this.home.removeFood(this, toEat, 1)){
			// Cooks the food and eats it
			animation.cookAndEatFood(toEat.toString());
			atDestination.acquire();
			this.hasEaten = true;			
		}else{
			this.hasEaten = false;
		}
	}

	/**
	 * Takes the person home w/ intent to sleep.
	 */
	private void actGoToSleep() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		setState(STATES.goingToSleep);
		processTransportationDeparture((BuildingInterface) home);
	}

	/**
	 * Once the person is home, moves to the bed before sleeping. 
	 * From actGoToSleep (after arrival at home)
	 * Sets hasEaten to false, since it's effectively the beginning of a new day.
	 * @throws InterruptedException
	 */
	private void actGoToBed() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		animation.goToSleep();
		atDestination.acquire();
		this.hasEaten = false;
		this.lastWentToSleep = new Date(date.getTime()); 
		setState(STATES.atSleep);
	}
	
	private void actWakeUp() throws InterruptedException {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		animation.goOutside();
		atDestination.acquire();
	}

	//=========//
	// Getters //
	//=========//

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public STATES getState() {
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
	public JobRoleInterface getOccupation() {
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
	public List<RoleInterface> getRoles() {
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
	public int getRoomNumber(){
		return roomNumber;
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}

	@Override
	public AnimatedPerson getAnimation(){
		return animation;
	}
	
	@Override
	public BuildingInterface getCurrentLocation(){
		return currentLocation;
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
	public void setOccupation(JobRoleInterface r) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		if (r != null) {
			// Giving an occupation
			this.occupation = r;
			this.addRole(r);
			r.setPerson(this);
		} else if (this.occupation != null) {
			// Taking away an occupation
			if (this.occupation.getActive()) {
				// The occupation should not be working when it is removed
				throw new IllegalStateException();
			} else {
				removeRole(occupation);
				occupation = null;
			}
		}
	}

	@Override
	public void setAnimation(AnimatedPerson a){
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		animation = a;
	}

	@Override
	public void setCar(Car c) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		getPropertyChangeSupport().firePropertyChange(CAR, this.car, c);
		car = c;
	}

	@Override
	public void setCash(int c) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		getPropertyChangeSupport().firePropertyChange(CASH, this.cash, c);
		this.cash = c;
	}

	/**
	 * This method sets Home for the PersonAgent.
	 * 
	 * @param h is a ResidenceBuildingInterface; that is, it can be a Home or an Apartment.
	 */
	@Override
	public void setHome(ResidenceBuildingInterface h) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		if (this.home != null) {
			this.home.removeResident(this.getResidentRole()); // remove from past home
		}
		this.home = h;
		this.home.addResident(this.getResidentRole());
		animation.setVisible(true); // see below
		if(animation != null && this.home.getPanel() != null)
		this.home.getPanel().addVisualizationElement(animation); // this and the above line "animates" the person at home, but doesn't DRAW it.
		else
			System.out.println("You're probably testing, because this house or apartment panel is null, or the animation is null.");

		
	}

	@Override
	public void setRoomNumber(int i){
		roomNumber = i;
	}

	@Override
	public void setName(String n) {
		getPropertyChangeSupport().firePropertyChange(NAME, this.name, n);
		this.name = n;
	}

	@Override
	public void setResidentRole(Resident r) {
		this.residentRole = r;
	}
	
	@Override
	public void setCurrentLocation(BuildingInterface b) {
		this.currentLocation = b;
	}

	/**
	 * When the state changes, let the GUI know
	 */
	private void setState(STATES s) {
		getPropertyChangeSupport().firePropertyChange(STATE, this.state, s);
		this.state = s;
	}

	//===========//
	// Utilities //
	//===========//

	@Override
	public void addRole(RoleInterface r) {
		boolean safeToContinue = true;
		for (RoleInterface i : roles) {
			if (i.getClass().equals(r.getClass())) {
				safeToContinue = false;
				throw new IllegalStateException("A role was added that duplicates another role.");
			}
		}
		
		if (safeToContinue) {
			r.setPerson(this); // Order is important here. Many roles expect to have a person set.
			roles.add(r);
			getPropertyChangeSupport().firePropertyChange(ROLES, null, r);
		}
	}

//	@Override
//	public void forceSleep() {
//		synchronized(roles) {
//			for (RoleInterface r : roles) {
//				r.setInactive();
//			}
//		}
//		if (carPassengerRole != null) {
//			roles.remove(carPassengerRole);
//			carPassengerRole = null;
//		}
//		if (busPassengerRole != null) {
//			roles.remove(busPassengerRole);
//			busPassengerRole = null;
//		}
//		if (restaurantCustomerRole != null) {
//			roles.remove(restaurantCustomerRole);
//			restaurantCustomerRole = null;
//		}
//		if (marketCustomerRole != null) {
//			roles.remove(marketCustomerRole);
//			marketCustomerRole = null;
//		}
//		try {
//			actGoToSleep();
//		} catch (InterruptedException e) {}
//	}
	
	@Override
	public void terminateWithExtremePrejudice() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// Get rid of the person's car
		if (car != null) {
			car.stopThread();
			car = null;
		}
		
		// If the person is a landlord... CHECK
		//     If the person is a landlord of their own home... CHECK
		//         If it's a house, just remove the house's landlord. CHECK
		//         If it's an apartment, set anyone else (who isn't this person) to be the landlord. CHECK
		//     If the person is a landlord of a different residence. CHECK
		//         Set anyone else to be the landlord CHECK; and if he's the only person who lives in an apt, landlord = null. ~rchoi
		for (RoleInterface i : roles) {
			if (i instanceof Landlord) {
				for(ResidenceBuilding j : ((Landlord) i).getResidences()){ // check all residences managed by person
					if(this.getHome() == j)	{ // Does landlord live in this residencebuilding?
						//is it an apartment?
						if(this.getHome() instanceof Apt){
						int toDel=0;
						for(int k = 0; k < j.getResidents().size(); k++){ // find index of person
							if(j.getResidents().get(k).equals(this.residentRole)){ // found index
								toDel = k;
							}
						}
						for(int k = toDel; k < j.getResidents().size()-1; k++){ // starting from the index to the rest of the list
							//shift everyone down a room, and change their room#s.
							j.getResidents().get(k+1).getPerson().setRoomNumber(k); // decrement next person's room # 
							//refrigerators are key-valued to Person, so no need to fiddle with it in the loop.
							//bed and stove navigation are done in person, based off Room Number. so, done.
							//basically we shift everyone below him up a room.
						}
						//re-assign landlord to index 0 resident. Recycle the current landlord and just repoint it
						//the building already knows the landlord's pointer. the landlord role and person need to know now, because of $ flow.
						((Landlord)i).setResident(j.getResidents().get(0)); // this method takes it deeper, to the person.
						
						//is it a house?
						}else if(this.getHome() instanceof House){ 
							// if he landlords his own house, house points to a landlord that's null. simple
							j.setLandlord(null);
						}
					}else{
						//if he landlords houses he doesn't live in? We've already deleted the one he does live in, if he lives in it.
						//This time, set getResidents(0) to be landlord. EZ
						if(j.getResidents().size() > 1) // if he's not the only person who lives in it,
							((Landlord)i).setResident(j.getResidents().get(0));
						else j.setLandlord(null); // if he's the only person who lives there, set landlord to null.
					}
					
				}
			}
		}
		
		// Remove the person from their residence
		this.getHome().removeResident(residentRole);
		this.roles.remove(residentRole);
		this.getHome().setFood(this, null);
		
		// Notify all the person's roles that the person is dead
		for (RoleInterface i : roles) {
			i.definitelyDead();
		}
		
		// Final steps
		roles.clear();
		Application.getModel().removePerson(this);
		this.stopThread();
		print("Goodbye, cruel world! (git blame: CSCI 201)");
	}

	private void removeRole(RoleInterface r) {
		roles.remove(r);
		getPropertyChangeSupport().firePropertyChange(ROLES, r, null);
	}

	/**
	 * Sends the person by car or bus to the selected destination.
	 * 
	 * If the person is already at the selected destination, it does nothing.
	 * 
	 * @throws InterruptedException 
	 * @param destination the building to travel to
	 */
	private void processTransportationDeparture(BuildingInterface destination) throws InterruptedException {
		if(currentLocation == this.getHome()){ // if you're at home and want to leave, tell animation to leave
			this.animation.goOutside();
			atDestination.acquire();
		}
		if (currentLocation != destination) {
			if (car != null) {
				setCurrentLocation(null);
				carPassengerRole = new CarPassengerRole(car, destination);
				carPassengerRole.setActive();
				carPassengerRole.setPerson(this);
				this.addRole(carPassengerRole);
			} else if(this.cash >= (Bus.BUS_FARE * 25)) { // Only goes to bus if he has 10 times the amount of bus fare in cash
				BusStop b = (BusStop) CityMap.findClosestBuilding(BUILDING.busStop, this);
				BusStop d = (BusStop) CityMap.findClosestBuilding(BUILDING.busStop, destination);
				walkerRole = new WalkerRole(b);
				AnimatedWalker walkerAnimation = new WalkerAnimation(walkerRole, currentLocation, Application.sidewalks);
				walkerAnimation.setVisible(true);
				if(Application.getMainFrame() != null)
					Application.getMainFrame().cityView.addAnimation(walkerAnimation);
				walkerRole.setAnimation(walkerAnimation);
				walkerRole.setPerson(this);
				this.addRole(walkerRole);
				walkerRole.setActive();
				if(Application.sidewalks != null)
					atDestination.acquire();
				this.removeRole(walkerRole);
				setCurrentLocation(b);
				busPassengerRole = new BusPassengerRole(d, b, destination);
				busPassengerRole.setPerson(this);
				busPassengerRole.setActive();
				this.addRole(busPassengerRole);
			} else { // Let's go for a walk
				walkerRole = new WalkerRole(destination);
				AnimatedWalker walkerAnimation = new WalkerAnimation(walkerRole, currentLocation, Application.sidewalks);
				walkerAnimation.setVisible(true);
				if(Application.getMainFrame() != null)
					Application.getMainFrame().cityView.addAnimation(walkerAnimation);
				walkerRole.setAnimation(walkerAnimation);
				walkerRole.setPerson(this);
				this.addRole(walkerRole);
				walkerRole.setActive();
				if(Application.sidewalks != null)
					atDestination.acquire();
			}
		} else {
			// Don't do anything, you're already where you should be
			return;
		}
	}

	/**
	 * Checks whether a car or bus is finished transporting for the scheduler.
	 * 
	 * Determines whether a person has arrived by car or bus. If they have,
	 * returns true so that the scheduler can activate the new role and state.
	 * 
	 * If the person was already at their destination when processTransportationDeparture
	 * was called, this will return true.
	 * 
	 */
	private boolean processTransportationArrival() {
		if (carPassengerRole == null && busPassengerRole == null && walkerRole == null) {
			return true;
		} else {
			if(walkerRole != null) {
				if(!walkerRole.getActive()) {
					setCurrentLocation(walkerRole.getDestination());
					removeRole(walkerRole);
					walkerRole = null;
					return true;
				}
			}
			if (car != null && carPassengerRole != null) {
				if(!carPassengerRole.getActive()) {
					setCurrentLocation(carPassengerRole.getDestination());
					removeRole(carPassengerRole);
					carPassengerRole = null;
					return true;
				}
			} else if (busPassengerRole != null && busPassengerRole != null) {
				if(!busPassengerRole.getActive()) {
					BuildingInterface destination = busPassengerRole.getDestination();
					setCurrentLocation(busPassengerRole.getBusStopDestination());
					removeRole(busPassengerRole);
					busPassengerRole = null;
					// Walk to the place you want to go
					walkerRole = new WalkerRole(destination);
					AnimatedWalker walkerAnimation = new WalkerAnimation(walkerRole, currentLocation, Application.sidewalks);
					walkerAnimation.setVisible(true);
					if(Application.getMainFrame() != null)
						Application.getMainFrame().cityView.addAnimation(walkerAnimation);
					walkerRole.setAnimation(walkerAnimation);
					walkerRole.setPerson(this);
					this.addRole(walkerRole);
					walkerRole.setActive();
					try {
						if(Application.sidewalks != null)
						atDestination.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.removeRole(walkerRole);
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * After a user is done working for the day, this decides what they'll do next.
	 * 
	 * Not all daily tasks will need to be performed. Daily tasks go in order: bank, 
	 * paying rent/maintenance, eating (goes to restaurant or market as needed), sleep
	 * 
	 * @return a State to indicate which daily task to perform
	 */
	private STATES pickDailyTask() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		STATES disposition = STATES.none;
		if (shouldGoToBank()) {
			disposition = STATES.goingToBank;
		} else if (shouldPayLandlord()) {
			disposition = STATES.goingToPayRent;
		} else if (shouldGoToRestaurant()) {
			disposition = STATES.goingToRestaurant;
		} else if (shouldGoToMarket()) {
			disposition = STATES.goingToMarket;
		} else if (shouldGoToCook()) {
			disposition = STATES.goingToCook;
		} else {
			disposition = STATES.goingToSleep;
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
		
		for (FOOD_ITEMS i : home.getFoodItems(this).keySet()) {
			items = items + home.getFoodItems(this).get(i);
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
		AlertLog.getInstance().logMessage(Integer.toString(this.hashCode()), this.name, msg);
		AlertLog.getInstance().logMessage(AlertTag.PERSON, "PersonAgent " + this.name, msg);
	}

	@Override
	public void printViaRole(String role, String msg) {
		AlertLog.getInstance().logMessage(Integer.toString(this.hashCode()), role, msg);
	}

}
