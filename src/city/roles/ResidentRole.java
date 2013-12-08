package city.roles;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import trace.AlertLog;
import trace.AlertTag;
import city.bases.ResidenceBuilding;
import city.bases.Role;
import city.roles.interfaces.Resident;

public class ResidentRole extends Role implements Resident {

	// Data

	private STATE rstate = STATE.none;
	private Date rentLastPaid;
	private ResidenceBuilding house;

	// Constructor

	public ResidentRole(Date rentLastPaid){
		super();
		this.rentLastPaid = rentLastPaid;
	}

	// Messages
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(rstate == STATE.needToPayRent){
			print("need to pay rent!");
			payRent();
			return true;
		}
		return false;
	}

	// Actions

	@Override
	public void payRent() {
		this.getPerson().getHome().getLandlord().msgHeresRent(house.getRent()); // pay rent to your house's landlord
		this.getPerson().setCash((int)(this.getPerson().getCash()-house.getRent())); // lose $ for rent
		if(house.getTotalCurrentMaintenance() != 0) {
			// pay maintenance if needed
			this.getPerson().setCash( (this.getPerson().getCash() - (house.getTotalCurrentMaintenance() / house.getResidents().size())) ); // lose $ for maintenance;
		}
		//print(""+rentLastPaid.getTime()); // for debugging
		rentLastPaid = this.getPerson().getDate();
		this.setInactive(); // step out of resident role
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
	
	@Override
	public void setRentLastPaid(Date d) {
		this.rentLastPaid = new Date(d.getTime());
	}
	
	// Utilities

	/**
	 * Returns true if today is the day that rent is due or rent is overdue 
	 */
	@Override
	public boolean rentIsDue() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.setTime(this.getPerson().getDate()); 
		long day = c.getTimeInMillis();// this is the time and date RIGHT NOW
		
		Calendar d = Calendar.getInstance();
		d.setTime(getRentDueDate());
		long due = d.getTimeInMillis();
		
		return (day >= due);
	}

	@Override
	public void setResidence(ResidenceBuilding b) {
		house = b;
	}
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("Resident", msg);
        AlertLog.getInstance().logMessage(AlertTag.HOUSE, "ResidentRole " + this.getPerson().getName(), msg);
    }

	// Classes
}
