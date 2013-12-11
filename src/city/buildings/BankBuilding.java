package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import city.animations.BankCustomerAnimation;
import city.animations.BankManagerAnimation;
import city.animations.BankTellerAnimation;
import city.bases.Animation;
import city.bases.Building;
import city.bases.Role;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Bank;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.BankPanel;
import city.roles.BankCustomerRole;
import city.roles.BankManagerRole;
import city.roles.BankTellerRole;
import city.roles.interfaces.BankManager;

public class BankBuilding extends Building implements Bank {
	
	// Data

	public BankManager manager;
	public List<Account> accounts = Collections.synchronizedList(new ArrayList<Account>());
	public List<Loan> loans = Collections.synchronizedList(new ArrayList<Loan>());	
	public List<BankTellerRole> employees = new ArrayList<BankTellerRole>();
	public List<BankCustomerAnimation> waitingCustomers = new ArrayList<BankCustomerAnimation>();
	double funds;
	public Date loanLastPaid;
	public HashMap<Role, Animation> allRoles = new HashMap<Role, Animation>();
	
	// Constructor
	
	public BankBuilding(String name, BankPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		this.addWorkerRoleName("city.roles.BankManagerRole");
		this.addWorkerRoleName("city.roles.BankTellerRole");
		this.setBuildingClassName("city.buildings.interfaces.Bank");
	}
	
	// Getters
	
	@Override
	public boolean getBusinessIsOpen() {
		boolean disposition = true;
		if (manager == null) { disposition = false; };
		if (employees.size() == 0) { disposition = false; };
		return disposition;
	}
	
	
	@Override
	public List<Account> getAccounts(){
		return accounts;
	}
	
	@Override
	public BankManager getManager() {
		return manager;
	}

	// Setters
	
	@Override
	public void setManager(BankManager b){
		manager = b;
	}
	
	// Utilities
	public void removeWaitingCustomer(BankCustomerAnimation bc){
		waitingCustomers.remove(bc);
		for(BankCustomerAnimation b : waitingCustomers){
			b.MoveUpInLine();
		}
	}
	@Override
	public void addOccupyingRole(RoleInterface r) {
		if(r instanceof BankManagerRole) {
			BankManagerRole c = (BankManagerRole)r;
			
			if(!allRoles.containsKey(c)) {
				BankManagerAnimation anim = new BankManagerAnimation(c); // no bank animation files 
				c.setGui(anim);	
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				manager = c;
				allRoles.put(c, anim);
			}
			manager = c;
			//c.setActive();
			System.out.println("Bank Manager set");
		}
		if(r instanceof BankTellerRole) {
			BankTellerRole c = (BankTellerRole)r;
			if(!allRoles.containsKey(c)) {
				BankTellerAnimation anim = new BankTellerAnimation(c, employees.size()); 
				c.setGui(anim);	
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				allRoles.put(c, anim);
			}
			employees.add(c);
			//c.setActive();
			System.out.println("Bank Teller set");
		}
		if(r instanceof BankCustomerRole) {
			BankCustomerRole c = (BankCustomerRole)r;
			if(!allRoles.containsKey(c)) {
				BankCustomerAnimation anim = new BankCustomerAnimation(c); 
				c.setGui(anim);	
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				allRoles.put(c, anim);
			}
			c.setActive();
			//c.setActivityBegun();
			System.out.println("Bank Customer set");
			
		}
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
		public int balance;
		public Account(int num, int money){
			balance = money;
			acctNum = num;
		}
	}

	@Override
	public int getWaitingCustomersSize() {
		// TODO Auto-generated method stub
		return waitingCustomers.size();
	}

	@Override
	public void addWaitingCustomer(BankCustomerAnimation gui) {
		// TODO Auto-generated method stub
		waitingCustomers.add(gui);
	}
	
}
