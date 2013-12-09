package city.bases;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

import city.bases.interfaces.AnimationInterface;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.RoleInterface;
import city.gui.BuildingCard;
import city.gui.exteriors.CityViewBuilding;

/**
 * The base class for all SimCity201 Buildings.
 * 
 * Buildings are coded for restaurants, banks, markets, bus stops.
 * For homes, see ResidenceBuildingBase, which extends Building.
 */
public abstract class Building implements BuildingInterface {
	
	// Data
	
	private String name; // Name of the building (e.g. "Market" or "RestaurantJP" or "House 1")
	private String customerRoleInterfaceName; // The interface name of the role that interacts with this building as a customer
	private String customerAnimationInterfaceName; // The interface name of the animation that interacts with this building as a customer
	private int cash; // Cash that the building has. Used by restaurants, etc., not houses, bus stops, etc.
	private HashMap<RoleInterface, AnimationInterface> occupyingRoles = new HashMap<RoleInterface, AnimationInterface>(); // Stores all roles currently inside the building, along with their animations
	private CityViewBuilding cityViewBuilding; // The representation of this building in the GUI's map
	protected BuildingCard panel; // The representation of this building's interior
	private ArrayList<String> workerRoleClasses = new ArrayList<String>(); // The classes which work in this type of building
	private String buildingClassName;
	private PropertyChangeSupport propertyChangeSupport;

	
	// Constructor

	public Building(String name, BuildingCard panel, CityViewBuilding cityBuilding) {
		propertyChangeSupport = new PropertyChangeSupport(this);
		this.name = name;
		this.cash = 0;
		this.setPanel(panel);
		this.setCityViewBuilding(cityBuilding);
		this.setBuildingClassName("city.bases.interfaces.BuildingInterface");
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
	public int getCash(){
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
	public ArrayList<String> getWorkerRoleClassNames() {
		return workerRoleClasses;
	}
	
	@Override
	public <T extends AnimationInterface> T getOccupyingRoleAnimation(RoleInterface r, Class<T> type) {
		return type.cast(occupyingRoles.get(r));
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
	
	@Override
	public String getBuildingClassName() {
		return buildingClassName;
	}
	
	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
    	return propertyChangeSupport;
	}
	
	// Setters
	
	@Override
	public void setCash(int c){
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
	
	@Override
	public void setBuildingClassName(String s) {
		this.buildingClassName = s;
	}
	
	// Utilities 
	
	@Override
	public abstract void addOccupyingRole(RoleInterface r);
	
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
		return occupyingRoles.containsKey(r);
	}
	
	@Override
	public void addWorkerRoleName(String r) {
		workerRoleClasses.add(r);
	}



}
