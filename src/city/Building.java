package city;

/**
 * The base class for all SimCity201 Buildings.
 * 
 * Buildings are coded for restaurants, banks, markets, homes, bus stops.
 */
public abstract class Building {
	private String name;
	private Role manager;

	public Building(String name, Role manager) {
		this.name = name;
		this.manager = manager;
	}

	public String getName() {
		return name;
	}
	
	public Role getManager() {
		return manager;
	}
	
	public void setManager(Role manager) {
		this.manager = manager;
	}

	public String toString() {
		return this.getClass().getName() + ": " + name;
	}

}
