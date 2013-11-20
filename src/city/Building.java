package city;

/**
 * The base class for all SimCity201 Buildings.
 * 
 * Buildings are coded for restaurants, banks, markets, homes, bus stops.
 */
public abstract class Building {
	private String name;

	public Building(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.getClass().getName() + ": " + name;
	}

}
