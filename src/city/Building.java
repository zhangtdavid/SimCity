package city;

import java.util.HashMap;

import city.gui.BuildingCard;
import city.gui.views.CityViewBuilding;

/**
 * The base class for all SimCity201 Buildings.
 * 
 * Buildings are coded for restaurants, banks, markets, homes, bus stops.
 */
public abstract class Building implements BuildingInterface {
	
	// Data
	
	private String name; // Name of the building (e.g. "Market" or "RestaurantJP" or "House 1")
	private String customerRoleInterfaceName; // The interface name of the role that interacts with this building as a customer
	private String customerAnimationInterfaceName; // The interface name of the animation that interacts with this building as a customer
	private int cash; // Cash that the building has. Used by restaurants, etc., not houses, bus stops, etc.
	private HashMap<Role, Animation> occupyingRoles = new HashMap<Role, Animation>(); // Stores all roles currently inside the building, along with their animations
	private CityViewBuilding cityViewBuilding;
	private BuildingCard panel;
	
	// Constructor

	public Building(String name) {
		this.name = name;
		cash = 0;
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
	public <T extends AnimationInterface> T getOccupyingRoleAnimation(Role r, Class<T> type) {
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
	public abstract void addRole(Role r);
	
	@Override
	public void addRole(Role r, Animation a) {
		occupyingRoles.put(r, a);
	}
	
	@Override
	public void removeRole(Role r) {
		// TODO
	}
	
	@Override
	public boolean roleExists(Role r) {
		return occupyingRoles.containsKey(r);
	}

}
