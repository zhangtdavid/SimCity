package city;

/**
 * The base class for all SimCity201 Buildings.
 * 
 * Buildings are coded for restaurants, banks, markets, homes, bus stops.
 */
public abstract class Building {
	
	// Data
	
	private String name;
	private int cash;

	// Constructor

	public Building(String name) {
		this.name = name;
	}
    
    // Messages
    
    // Scheduler
	
	// Actions
	
	// Getters
	
	public String getName() {
		return name;
	}

	public int getCash(){
		return cash;
	}
	
	// Setters
	
	public void setCash(int c){
		this.cash = c;
	}
	
	// Utilities 

}
