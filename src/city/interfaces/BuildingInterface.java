package city.interfaces;

import city.Animation;
import city.Role;
import city.gui.BuildingCard;
import city.gui.CityViewBuilding;

public interface BuildingInterface {
	
	// Data
	
	// Constructor
    
    // Messages
    
    // Scheduler
	
	// Actions
	
	// Getters
	
	public String getName();
	public int getCash();
	public String getCustomerRoleName();
	public String getCustomerAnimationName();
	public <T extends AnimationInterface> T getRoleAnimation(Role r, Class<T> type);
	public CityViewBuilding getCityViewBuilding();
	public BuildingCard getPanel();
	
	// Setters
	
	public void setCash(int c);
	public void setCustomerRoleName(String c);
	public void setCustomerAnimationName(String c);
	public void setCityViewBuilding(CityViewBuilding b);
	public void setPanel(BuildingCard b);
	
	// Utilities 
	
	public abstract void addRole(Role r);
	public void addRole(Role r, Animation a);
	public void removeRole(Role r);
	public boolean roleExists(Role r);

}
