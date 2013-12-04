package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import city.Animation;
import city.Building;
import city.Role;
import city.RoleInterface;
import city.gui.buildings.BankPanel;
import city.gui.views.CityViewBuilding;
import city.interfaces.Bank;
import city.interfaces.BankManager;
import city.roles.BankManagerRole;
import city.roles.BankTellerRole;

public class BankBuilding extends Building implements Bank {
	
	// Data

	public BankManager manager;
	public List<Account> accounts = Collections.synchronizedList(new ArrayList<Account>());
	public List<Loan> loans = Collections.synchronizedList(new ArrayList<Loan>());	
	public List<BankTellerRole> employees = new ArrayList<BankTellerRole>();
	double funds;
	public Date loanLastPaid;
	public HashMap<Role, Animation> allRoles = new HashMap<Role, Animation>();
	

	
	// Constructor
	
	public BankBuilding(String name, BankPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
	}
	
	// Getters
	
	@Override
	public List<Loan> getLoans(){
		return loans;
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
	
	@Override
	public void addOccupyingRole(RoleInterface r) {
		if(r instanceof BankManagerRole) {
			BankManagerRole c = (BankManagerRole)r;
			
			/*if(!allRoles.containsKey(c)) {
				BankManagerAnimation anim = new BankManagerAnimation(c); // no bank animation files 
				c.setGui(anim);	
//				c.setAnimation(anim);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				manager = c;
				allRoles.put(c, anim);
			}*/
			manager = c;
			c.setActive();
			//c.setActivityBegun();
			System.out.println("Bank Manager set");
		}
		if(r instanceof BankTellerRole) {
			BankTellerRole c = (BankTellerRole)r;
			/*if(!allRoles.containsKey(c)) {
				RestaurantChoiCustomerAnimation anim = new RestaurantChoiCustomerAnimation(c); 
				c.setGui(anim);	
//				c.setAnimation(anim);
				anim.setVisible(true);
				panel.addVisualizationElement(anim);
				employees.add(c);
				allRoles.put(c, anim);
			}*/
			employees.add(c);
			c.setActive();
			//c.setActivityBegun();
			System.out.println("Bank Teller set");
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
		public double balance;
		public Account(int num, double money){
			balance = money;
			acctNum = num;
		}
	}
	
}
