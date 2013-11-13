package city;

/**
 * The base class for all SimCity201 Buildings.
 * 
 * Buildings are coded for restaurants, banks, markets, homes, bus stops.
 */
public abstract class Building {
	private String name;
	private Role owner;

	public Building(String name, Role owner) {
		this.name = name;
		this.owner = owner;
	}

	public String getName() {
		return name;
	}
	
	public Role getRole() {
		return owner;
	}
	
	public void setRole(Role newOwner) {
		owner = newOwner;
	}

	public String toString() {
		return this.getClass().getName() + ": " + name;
	}

}
