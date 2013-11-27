package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import city.Building;
import city.Role;
import city.interfaces.BankManager;
import city.roles.BankTellerRole;

public class BankBuilding extends Building {
	
	// Data

	public BankManager manager;
	public List<Account> accounts = Collections.synchronizedList(new ArrayList<Account>());
	public List<Loan> loans = Collections.synchronizedList(new ArrayList<Loan>());	
	public List<BankTellerRole> employees = new ArrayList<BankTellerRole>();
	double funds;
	public Date loanLastPaid;
	
	// Constructor
	
	public BankBuilding(String name) {
		super(name);
	}
	
	// Getters
	
	public List<Loan> getLoans(){
		return loans;
	}
	
	public List<Account> getAccounts(){
		return accounts;
	}
	
	public BankManager getManager() {
		return manager;
	}

	// Setters
	
	public void setManager(BankManager b){
		manager = b;
	}
	
	// Utilities
	
	@Override
	public void addRole(Role r) {
		// TODO
		return;
	}
	
	// Classes

	public static class Loan {
		public double monthlyPayment;
		public double remaining;
		public int acctNum;
		public Loan(double amt, double monthly, int acct){
			monthlyPayment = monthly;
			remaining = amt;
			acctNum = acct;
		}
	}

	public static class Account {
		public int acctNum;
		public double balance;
		public Account(int num, double money){
			balance = money;
			acctNum = num;
		}
	}
	
}
