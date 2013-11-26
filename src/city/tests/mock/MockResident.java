package city.tests.mock;

import java.util.Calendar;
import java.util.Date;

import utilities.LoggedEvent;
import city.MockRole;
import city.buildings.ResidenceBaseBuilding;
import city.interfaces.Landlord;
import city.interfaces.Resident;


public class MockResident extends MockRole implements Resident{

	private STATE rstate = STATE.none;
	private Landlord landlord;
	private Date rentLastPaid;
	private ResidenceBaseBuilding residence;
	
	public MockResident(){
		super();
	}
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
	@Override
	public void payRent() {
		rentLastPaid = this.getPerson().getDate();
		log.add(new LoggedEvent("Rent Last Paid is: " + rentLastPaid));
		landlord.msgHeresRent(residence.rent); // pay rent
		log.add(new LoggedEvent("Before paying rent, money is " + this.getPerson().getCash()));
		 this.getPerson().setCash( this.getPerson().getCash()-residence.rent); // lose $ for rent
		log.add(new LoggedEvent("After paying rent, money is " + this.getPerson().getCash()));
		if(residence.total_current_maintenance != 0) // pay maintenance if needed
			this.getPerson().setCash( this.getPerson().getCash()-residence.total_current_maintenance/residence.residents.size()); 
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
	public boolean isLandlord() {
		return (landlord != null);
	}
	@Override
	public boolean rentIsDue() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.getPerson().getDate());
		int day = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(getRentDueDate());
		int due = c.get(Calendar.DAY_OF_YEAR);
		return (day >= due);
	}
	@Override
	public void setLandlord(Landlord l) {
		landlord = l;
		
	}
	@Override
	public void setResidence(ResidenceBaseBuilding b) {
		residence = b;		
	}
}
