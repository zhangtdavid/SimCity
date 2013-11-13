package city.roles;

import city.Application;
import city.Role;
import city.interfaces.BankCustomer;

public class BankCustomerRole extends Role implements BankCustomer {

	// Data
	
	Application.BANK_SERVICES desiredService;
	
	// Constructor
	
	public BankCustomerRole(Application.BANK_SERVICES s) {
		desiredService = s;
	}
	
	// Messages
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		// TODO Auto-generated method stub
		return false;
	}
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
