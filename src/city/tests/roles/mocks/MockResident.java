package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import utilities.LoggedEvent;
import city.bases.ResidenceBuilding;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;
import city.tests.bases.mocks.MockRole;

public class MockResident extends MockRole implements Resident {

	// Data
	
	private STATE rstate = STATE.none;
	private Landlord landlord;
	private Date rentLastPaid;
	private ResidenceBuilding residence;
	
	// Constructor
	
	public MockResident(Date rentLastPaid) {
		super();
		this.rentLastPaid = rentLastPaid;
	}
	
	/**
	 * For the benefit of tests that don't use rentLastPaid
	 */
	public MockResident() {
		super();
		this.rentLastPaid = new Date(0);
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(rstate == STATE.needToPayRent){
			log.add(new LoggedEvent("Inside scheduler, state = " + rstate));
			System.out.println("need to pay rent!");
			payRent();
			return true;
		}
		return false;
	}
	
	// Actions
	
	@Override
	public void payRent() {
		rentLastPaid = this.getPerson().getDate();
		log.add(new LoggedEvent("Rent Last Paid is: " + rentLastPaid));
		landlord.msgHeresRent(residence.getRent()); // pay rent
		log.add(new LoggedEvent("Before paying rent, money is " + this.getPerson().getCash()));
		 this.getPerson().setCash( this.getPerson().getCash()-residence.getRent()); // lose $ for rent
		log.add(new LoggedEvent("After paying rent, money is " + this.getPerson().getCash()));
		if(residence.getTotalCurrentMaintenance() != 0) // pay maintenance if needed
			this.getPerson().setCash( this.getPerson().getCash()-residence.getTotalCurrentMaintenance()/residence.getResidents().size()); 
		// lose $ for maintenance;
		
		this.setInactive();		
	}
	
	
	
	
	
	
	
	
	@Override
	public Date getRentDueDate() {
		Date dueDate = new Date(0);
		dueDate.setTime(rentLastPaid.getTime() + RENT_DUE_INTERVAL);
		return dueDate;
	}
	
	@Override
	public boolean rentIsDue() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.setTime(this.getPerson().getDate());
		int day = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(getRentDueDate());
		int due = c.get(Calendar.DAY_OF_YEAR);
		return (day >= due);
	}
	
	@Override
	public void setResidence(ResidenceBuilding b) {
		residence = b;		
	}
	
	@Override
	public void setRentLastPaid(Date d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}
}
