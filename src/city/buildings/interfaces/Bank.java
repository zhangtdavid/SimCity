package city.buildings.interfaces;

import java.util.List;

import city.animations.BankCustomerAnimation;
import city.bases.interfaces.BuildingInterface;
import city.buildings.BankBuilding.Account;
import city.roles.interfaces.BankManager;

public interface Bank extends BuildingInterface {

	int WORKER_SALARY = 2000;

	public List<Account> getAccounts();

	public BankManager getManager();

	public void setManager(BankManager b);

	public void removeWaitingCustomer(BankCustomerAnimation gui);

	public int getWaitingCustomersSize();

	public void addWaitingCustomer(BankCustomerAnimation gui);

}