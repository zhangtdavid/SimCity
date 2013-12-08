package city.tests.bases.mocks;

import java.util.ArrayList;
import java.util.HashMap;

import utilities.EventLog;
import city.bases.interfaces.AnimationInterface;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.RoleInterface;
import city.gui.BuildingCard;
import city.gui.exteriors.CityViewBuilding;

/**
 * The base class for all SimCity201 building mocks.
 * 
 * This class implements most things required by BuildingInterface so that
 * mocks themselves may focus only on their particular behaviors.
 * 
 * This class also implements the event log used by all mocks.
 */
public abstract class MockBuilding implements BuildingInterface {
	
	// Data
	
	private String name; // Name of the building (e.g. "Market" or "RestaurantJP" or "House 1")
	private String customerRoleInterfaceName; // The interface name of the role that interacts with this building as a customer
	private String customerAnimationInterfaceName; // The interface name of the animation that interacts with this building as a customer
	private int cash; // Cash that the building has. Used by restaurants, etc., not houses, bus stops, etc.
	private HashMap<RoleInterface, AnimationInterface> occupyingRoles = new HashMap<RoleInterface, AnimationInterface>(); // Stores all roles currently inside the building, along with their animations
	private CityViewBuilding cityViewBuilding; // The representation of this building in the GUI's map
	private BuildingCard panel; // The representation of this building's interior
	
	public EventLog log = new EventLog();
	
	// Constructor
	
	public MockBuilding(String name) {
		this.name = name;
		this.cash = 0;
	}
	
	// Messages
	
	// Scheduler	
	
	// Actions
	
	// Getters
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getCash() {
		return cash;
	}

	@Override
	public String getCustomerRoleName() {
		return customerRoleInterfaceName;
	}

	@Override
	public String getCustomerAnimationName() {
		return customerAnimationInterfaceName;
	}

	@Override
	public <T extends AnimationInterface> T getOccupyingRoleAnimation(RoleInterface r, Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CityViewBuilding getCityViewBuilding() {
		return cityViewBuilding;
	}

	@Override
	public BuildingCard getPanel() {
		return panel;
	}
	
	@Override
	public HashMap<RoleInterface, AnimationInterface> getOccupyingRoles() {
		return occupyingRoles;
	}
	
	// Setters
	
	@Override
	public void setCash(int c) {
		this.cash = c;
	}

	@Override
	public void setCustomerRoleName(String c) {
		this.customerRoleInterfaceName = c;
	}

	@Override
	public void setCustomerAnimationName(String c) {
		this.customerAnimationInterfaceName = c;
	}

	@Override
	public void setCityViewBuilding(CityViewBuilding b) {
		this.cityViewBuilding = b;
	}

	@Override
	public void setPanel(BuildingCard b) {
		this.panel = b;
	}
	
	// Utilities
	
	@Override
	public void addOccupyingRole(RoleInterface r) {
		// In ordinary buildings this overridden method would actually create the animation
		occupyingRoles.put(r, null);
	}

	@Override
	public void addOccupyingRole(RoleInterface r, AnimationInterface a) {
		occupyingRoles.put(r, a);
	}

	@Override
	public void removeOccupyingRole(RoleInterface r) {
		occupyingRoles.remove(r);
	}

	@Override
	public boolean occupyingRoleExists(RoleInterface r) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public ArrayList<String> getWorkerRoleClassNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBuildingClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBuildingClassName(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWorkerRoleName(String r) {
		// TODO Auto-generated method stub
		
	}

}
