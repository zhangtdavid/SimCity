package city;

import java.util.HashMap;

import city.gui.BuildingCard;
import city.gui.views.CityViewBuilding;
import city.interfaces.Person;

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
	private BuildingCard panel; // The representation of this building's interior
	
	// Constructor

	public Building(String name, BuildingCard panel, CityViewBuilding cityBuilding) {
		this.name = name;
		this.cash = 0;
		this.setPanel(panel);
		this.setCityViewBuilding(cityBuilding);
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

}
