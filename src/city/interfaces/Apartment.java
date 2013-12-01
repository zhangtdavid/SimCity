package city.interfaces;

import city.BuildingInterface;
import city.RoleInterface;

public interface Apartment extends BuildingInterface {

	public final static int NUMBER_OF_BEDS = 5;

	public void addResident(Resident r);

	public void addOccupyingRole(RoleInterface r);

}