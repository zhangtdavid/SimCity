package city.tests.mock;

import java.util.Calendar;
import java.util.Date;

import utilities.LoggedEvent;
import city.Application;
import city.MockRole;
import city.buildings.HouseBuilding;
import city.interfaces.Landlord;
import city.interfaces.Person;
import city.interfaces.Resident;
import city.interfaces.Resident.STATE;

public class MockResident extends MockRole implements Resident{

	private STATE rstate = STATE.none;
	private Landlord landlord;
	private Date rentLastPaid;
	private HouseBuilding house;
	
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
		landlord.msgHeresRent(house.rent); // pay rent
		log.add(new LoggedEvent("Before paying rent, money is " + this.getPerson().getCash()));
		 this.getPerson().setCash( this.getPerson().getCash()-house.rent); // lose $ for rent
		log.add(new LoggedEvent("After paying rent, money is " + this.getPerson().getCash()));
		if(house.total_current_maintenance != 0) // pay maintenance if needed
			this.getPerson().setCash( this.getPerson().getCash()-house.total_current_maintenance/house.residents.size()); 
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
	public void setHouse(HouseBuilding b) {
		house = b;
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
}
