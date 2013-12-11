package city.buildings;

import city.bases.ResidenceBuilding;
import city.buildings.interfaces.Apt;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.AptPanel;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;

public class AptBuilding extends ResidenceBuilding implements Apt {

	// Data
	
	public final static int NUMBER_OF_BEDS = 5;

	// Constructor

	public AptBuilding(String name, Landlord l, AptPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		this.setLandlord(l); // WHO YOU PAY RENT TO. MIGHT NOT LIVE HERE // TODO Perhaps I should eliminate the landlord requirement, and have that be added separately?
		this.panel = panel;
		this.setCityViewBuilding(cityBuilding); 
	}

	// Getters
	
	@Override
	public boolean getIsFull() {
		return residents.size() == 5;
	}
	
	// Setters

	// Utilities

	@Override
	public void addResident(Resident r) {
		if (!residents.contains(r)) {
			if (residents.size() <= AptBuilding.NUMBER_OF_BEDS) {
				residents.add(r);
				super.addResident(r);
				r.getPerson().getAnimation().setVisible(true);
			} else {
				throw new IllegalStateException("Only five people at a time may live in an apartment.");
			}
		}
	}

}
