package city.buildings.interfaces;

import java.util.List;

import city.bases.interfaces.BuildingInterface;
import city.buildings.BankBuilding.Account;
import city.buildings.BankBuilding.Loan;
import city.roles.interfaces.BankManager;

public interface Bank extends BuildingInterface {

	public static final int WORKER_SALARY = 300;
	public static final List<Loan> loans = null;
	public static final List<Account> accounts = null;
	public int waitingCustomers = 0;

	public List<Loan> getLoans();

	public List<Account> getAccounts();

	public BankManager getManager();

	public void setManager(BankManager b);

}