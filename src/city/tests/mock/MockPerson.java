package city.tests.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Semaphore;

import city.RoleInterface;
import city.abstracts.MockAgent;
import city.abstracts.ResidenceBuildingInterface;
import city.animations.interfaces.AnimatedPerson;
import city.interfaces.BankCustomer;
import city.interfaces.BusPassenger;
import city.interfaces.Car;
import city.interfaces.CarPassenger;
import city.interfaces.MarketCustomer;
import city.interfaces.Person;
import city.interfaces.Resident;

public class MockPerson extends MockAgent implements Person {
	
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
	// private Date lastAteAtRestaurant;
	// private Date lastWentToSleep;
	private String name;
	private ArrayList<RoleInterface> roles = new ArrayList<RoleInterface>();
	private Semaphore atDestination = new Semaphore(0, true);
	private AnimatedPerson animation;
	private STATE state; 
	private int cash;
	private boolean hasEaten;
	
	// Constructor
	
    /**
     * Create a MockPerson.
     * 
     * @param name the person's name
     * @param startDate the current simulation date
     */
	public MockPerson(String name, Date startDate) {
		super();
		this.name = name;
		this.date = new Date(startDate.getTime());
		// this.lastAteAtRestaurant = new Date(startDate.getTime());
		// this.lastWentToSleep = new Date(startDate.getTime());
		this.state = STATE.none;
		this.cash = 0;
		this.hasEaten = false;
		
		residentRole = new MockResident(new Date(startDate.getTime()));
		bankCustomerRole = new MockBankCustomer();
		this.addRole(residentRole);
		this.addRole(bankCustomerRole);
	}
	
	/**
	 * For the benefit of tests that aren't using a constructor with startDate
	 */
	public MockPerson(String name) {
		super();
		this.name = name;
		this.date = new Date(0);
		// this.lastAteAtRestaurant = new Date(0);
		// this.lastWentToSleep = new Date(0);
		this.state = STATE.none;
		this.cash = 0;
		this.hasEaten = false;
		
		residentRole = new MockResident(new Date(0));
		bankCustomerRole = new MockBankCustomer();
		this.addRole(residentRole);
		this.addRole(bankCustomerRole);
	}
	
	// Messages
	
	@Override
	public void guiAtDestination() {
		this.atDestination.release();
	}
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	@Override
	public String getName() {
		return name;
	}
	
    @Override
    public Date getDate() {
    	return date;
    }
	
	@Override
	public int getCash() {
		return cash;
	}

	@Override
	public ResidenceBuildingInterface getHome() {
		return home;
	}
	
	@Override
	public RoleInterface getOccupation() {
		return occupation;
	}
	
	@Override
	public ArrayList<RoleInterface> getRoles() {
		return roles;
	}

	@Override
	public Car getCar() {
		return car;
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
	public MarketCustomer getMarketCustomerRole() {
		return marketCustomerRole;
	}
	
	@Override
	public RoleInterface getRestaurantCustomerRole() {
		return restaurantCustomerRole;
	}
	
	@Override
	public STATE getState() {
		return this.state;
	}

	@Override
	public BankCustomer getBankCustomerRole() {
		return this.bankCustomerRole;
	}

	@Override
	public boolean getHasEaten() {
		return this.hasEaten;
	}

	@Override
	public Resident getResidentRole() {
		return this.residentRole;
	}
	
	@Override
	public AnimatedPerson getAnimation() {
		return this.animation;
	}
	
	// Setters
	
	@Override
	public void setAnimation(AnimatedPerson p) {
		this.animation = p;
	}
	
	@Override
	public void setCar(Car c) {
		this.car = c;
	}
	
	@Override
	public void setDate(Date d) {
		this.date = new Date(d.getTime());
	}
	
	@Override
	public void setOccupation(RoleInterface r) {
		this.occupation = r;
	}

	@Override
	public void setCash(int c) {
		this.cash = c;
	}
	
	@Override
	public void setHome(ResidenceBuildingInterface h) {
		this.home = h;		
	}
	
	// Utilities

	@Override
	public void addRole(RoleInterface r) {
		this.roles.add(r);
	}

}
