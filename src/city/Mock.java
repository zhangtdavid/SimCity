package city;

import city.agents.PersonAgent;

/**
 * The base class for all SimCity201 mocks.
 * 
 * Mocks are coded for agents, roles, and animations.
 */
public abstract class Mock {
	private String name;

	public Mock(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.getClass().getName() + ": " + name;
	}
	
	public boolean getActive() {
		// TODO
		return false;
	}
	
	public void setActive() {
		// TODO
	}
	
	public void setInactive() {
		// TODO
	}

}
