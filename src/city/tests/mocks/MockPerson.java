package city.tests.mocks;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Semaphore;

import city.agents.interfaces.Car;
import city.agents.interfaces.Person;
import city.animations.interfaces.AnimatedPerson;
import city.animations.interfaces.AnimatedPersonAtHome;
import city.bases.interfaces.JobRoleInterface;
import city.bases.interfaces.ResidenceBuildingInterface;
import city.bases.interfaces.RoleInterface;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.BusPassenger;
import city.roles.interfaces.CarPassenger;
import city.roles.interfaces.MarketCustomer;
import city.roles.interfaces.Resident;
import city.tests.bases.mocks.MockAgent;

public class MockPerson extends MockAgent implements Person {
	
	// Data
	
	private Date date;
	private JobRoleInterface occupation;
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
	private STATES state; 
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
		this.state = STATES.none;
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
		this.state = STATES.none;
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
	public JobRoleInterface getOccupation() {
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
	public STATES getState() {
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
	public void setCar(Car c) {
		this.car = c;
	}
	
	@Override
	public void setDate(Date d) {
		this.date = new Date(d.getTime());
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

	@Override
	public int getRoomNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRoomNumber(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHomeAnimation(AnimatedPersonAtHome anim) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AnimatedPersonAtHome getAnimationAtHome() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void acquireSemaphoreFromAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void releaseSemaphoreFromAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOccupation(JobRoleInterface r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResidentRole(Resident r) {
		// TODO Auto-generated method stub
		
	}
}
