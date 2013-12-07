//package city.animations.interfaces;
//
//
//public interface AnimatedPersonAtHome {
//	// Data
//	public static enum Command {noCommand, AtDoor, InBed, ToBed, ToRef, ToStove, ToTable, ToDoor, StationaryAtRef, StationaryAtStove, StationaryAtTable};
//	public static enum OrderIcon {Steak, Chicken, Pizza, Salad}; // could use strings or something on Person's side instead
//    public static final int WIDTH = 20; // Person is a 20x20 rect, standard.
//
//	// Constructor (n/a interface)
//	
//	// Abstract
//	
//	// Movement
//    
//	/**
//	 * Moves the person to bed before sleeping. 
//	 */
//	public abstract void goToSleep(); // to person's bed
//	
//	/**
//	 * Moves the person to refrigerator to check for food stocks.
//	 * Person always knows how much food there is in the refrigerator. Or does he?
//	 * What if his room mate ate all the food in there without person knowing? Too real. Thus, we check.
//	 * This is before actually checking the food; it moves the Person to the refrigerator.
//	 */
//	public abstract void verifyFood(); // to person's refrig
//	
//	/**
//	 * Moves the person to stove to cook food.
//	 * (To be called after verifyFood().)
//	 * This animation takes Person to the stove with item, (then timer - cook)
//	 */
//	public abstract void cookAndEatFood(String in); // to person's stove and table
//	
//	/**
//	 * Moves the person to the door so he can go outside (and leave the house)
//	 */
//	public abstract void goOutside(); // to person's door
//
//	
//    // Getters
//    public int[] getDestination();
//
//	public boolean getBeingTested();
//    // Setters
//	/**
//	 * Notifies the animation that it's OK to release the semaphore one time after animation is complete.
//	 */
//	public void setAcquired();
//	
//	public void setCoords(int x, int y);
//
//	public void setGraphicStatus(String in);
//
//	public abstract void setAtHome();
//
//    // Utilities
//
//	
//
//}
