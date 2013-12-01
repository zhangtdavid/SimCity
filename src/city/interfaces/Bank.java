package city.interfaces;

import java.util.List;

import city.BuildingInterface;
import city.buildings.BankBuilding.Account;
import city.buildings.BankBuilding.Loan;

public interface Bank extends BuildingInterface {

	public static final int WORKER_SALARY = 300;

	public List<Loan> getLoans();

	public List<Account> getAccounts();

	public BankManager getManager();

	public void setManager(BankManager b);

}