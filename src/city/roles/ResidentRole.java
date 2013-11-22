package city.roles;

import java.util.Calendar;
import java.util.Date;

import city.Role;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class ResidentRole extends Role implements Resident {
	
	// Data
	
	private STATE rstate = STATE.none;
	private Landlord landlord;
	private Date rentLastPaid;
	
	// Constructor
	
	public ResidentRole(Date rentLastPaid){
		super();
		this.rentLastPaid = rentLastPaid;
	}
	
	// Messages
	
	@Override
	public void msgPayForMaintenance(double d) {
		
	}
	
	// Scheduler
	@Override
	public boolean runScheduler() {
		if(rstate == STATE.needToPayMaintenance){
			System.out.println("need to pay maintenance!");
			payMaintenance();
			return true;
		}
		if(rstate == STATE.needToPayRent){
			System.out.println("need to pay rent!");
			payRent();
			return true;
		}
		return false;
	}

	// Actions
	
	public void payMaintenance() {
		
	}
	
	public void payRent() {
		// TODO update rentLastPaid here
		// Example: rentLastPaid = this.getPerson().getDate();
	}
	
	// Getters
	
	/**
	 * Takes the Date of the last rent payment and adds the set interval for
	 * the next payment to it, returning a Date of the exact time rent is due.
	 * 
	 * @return
	 */
	@Override
	public Date getRentDueDate() {
		Date dueDate = new Date(0);
		dueDate.setTime(rentLastPaid.getTime() + RENT_DUE_INTERVAL);
		return dueDate;
	}
	
	// Setters

	// Utilities
	
	/**
	 * Returns true if today is the day that rent is due or rent is overdue 
	 * 
	 * @return
	 */
	@Override
	public boolean rentIsDue() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.getPerson().getDate());
		int day = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(getRentDueDate());
		int due = c.get(Calendar.DAY_OF_YEAR);
		return (day >= due);
	}

	// Classes
}
