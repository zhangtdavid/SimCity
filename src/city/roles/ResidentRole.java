package city.roles;

import java.util.Calendar;
import java.util.Date;

import city.Application;
import city.Role;
import city.agents.PersonAgent;
import city.buildings.AptBuilding;
import city.buildings.ResidenceBaseBuilding;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class ResidentRole extends Role implements Resident {

	// Data

	private STATE rstate = STATE.none;
	private Landlord landlord;
	private double maintenance;
	private double upcomingRent;
	private Date rentLastPaid;
	private ResidenceBaseBuilding house;

	// Constructor

	public ResidentRole(){
		super();
		maintenance = 0;
		upcomingRent = 5; // low number for normative situations; i wish my rent was ($)
	}

	// Messages

	// Scheduler
	@Override
	public boolean runScheduler() {
		if(rstate == STATE.needToPayRent){
			System.out.println("need to pay rent!");
			payRent();
			return true;
		}
		return false;
	}

	// Actions

	public void payRent() {
		landlord.msgHeresRent(house.rent); // pay rent
		this.getPerson().setCash((int)(this.getPerson().getCash()-house.rent)); // lose $ for rent
		if(house.total_current_maintenance != 0) // pay maintenance if needed
			this.getPerson().setCash((int)(this.getPerson().getCash()-house.total_current_maintenance/house.residents.size())); // lose $ for maintenance;
		System.out.println(rentLastPaid.getTime());
		rentLastPaid = this.getPerson().getDate();
		this.setInactive();
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
		System.out.println("within getRentDueDate: " + dueDate);
		return dueDate;
	}
	@Override
	public boolean isLandlord() {
		return (landlord != null);
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
		long day = c.getTimeInMillis();// this is the time and date RIGHT NOW
		
		Calendar d = Calendar.getInstance();
		d.setTime(getRentDueDate());
		long due = d.getTimeInMillis();
		System.out.println("Time now: " + day);
		System.out.println("Time due: " + due);
		
		return (day >= due);
	}

	@Override
	public void setResidence(ResidenceBaseBuilding b) {
		house = b;
	}

	@Override
	public void setLandlord(Landlord l) {
		landlord = l;
		
	}
	public void setActive(){
		rentLastPaid = this.getPerson().getDate();
	}

	// Classes
}
