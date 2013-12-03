package city.animations;

import java.awt.Graphics2D;

import city.Animation;
import city.animations.interfaces.AnimatedPerson;
import city.interfaces.BusStop;

public class PersonAnimation extends Animation implements AnimatedPerson {
	
	//Data
	
	//Update position (Drawing)
	@Override
	public void updatePosition() {

	}

	/**
	 * Draws person and a string symbolizing the food he is making/eating at the time. 
	 */
	@Override
	public void draw(Graphics2D g) {
	} 

	
	//Msg (from agent)
	
	/**
	 * Moves the person to a bus stop (already determined which is closest) 
	 */
	@Override
	public void goToBusStop(BusStop b) {
	}

	@Override
	/**
	 * Moves the person to bed before sleeping. 
	 */
	public void goToSleep() {
	}

	/**
	 * Moves the person to refrigerator to check for food stocks.
	 * Person always knows how much food there is in the refrigerator. Or does he?
	 * What if his room mate ate all the food in there without person knowing? Too real. Thus, we check.
	 * This is before actually checking the food; it moves the Person to the refrigerator.
	 */
	@Override
	public void verifyFood(){
	}
	
	/**
	 * Moves the person to stove to cook food.
	 * After verifyFood().
	 * This animation takes Person to the stove with item, (then timer - cook)
	 */
	@Override
	public void cookAndEatFood() {
	}
	
	/**
	 * Moves the person to table to eat food.
	 * After cookAndEatFood().
	 * This animation takes Person to the table with item (then timer - eat).
	 */
	/*
	@Override
	public void cookAndEatFoodPart2(){
	}*/
}
