package city.bases.interfaces;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

import city.gui.BuildingCard;
import city.gui.exteriors.CityViewBuilding;

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
	public <T extends AnimationInterface> T getOccupyingRoleAnimation(RoleInterface r, Class<T> type);
	public CityViewBuilding getCityViewBuilding();
	public BuildingCard getPanel();
	public HashMap<RoleInterface, AnimationInterface> getOccupyingRoles();
	public ArrayList<String> getWorkerRoleClassNames();
	public String getBuildingClassName();
	public PropertyChangeSupport getPropertyChangeSupport();
	public boolean getBusinessIsOpen();
	
	// Setters
	
	public void setCash(int c);
	public void setCustomerRoleName(String c);
	public void setCustomerAnimationName(String c);
	public void setCityViewBuilding(CityViewBuilding b);
	public void setPanel(BuildingCard b);
	public void setBuildingClassName(String s);
	
	// Utilities 
	
	public void addOccupyingRole(RoleInterface r);
	public void addOccupyingRole(RoleInterface r, AnimationInterface a);
	public void addWorkerRoleName(String r);
	public void removeOccupyingRole(RoleInterface r);
	public boolean occupyingRoleExists(RoleInterface r);
}
