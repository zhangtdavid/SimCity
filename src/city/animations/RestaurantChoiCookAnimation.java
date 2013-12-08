package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCook;
import city.bases.Animation;
import city.roles.interfaces.RestaurantChoiCook;

public class RestaurantChoiCookAnimation extends Animation implements RestaurantChoiAnimatedCook {

	//Data
	private int xPos, yPos, xDestination, yDestination;

	private boolean currentlyAcquired;
	boolean init;
	private String orderIcon = new String();
	RestaurantChoiCook theCook;

	//Constructor
	public RestaurantChoiCookAnimation(RestaurantChoiCook c) {
		this.theCook = c;
		xPos = 520;
		xDestination = 520;
		yPos = 0;
		yDestination = 0;
	}

	//Abstract
	@Override
	public void updatePosition() {
		if (xPos < xDestination)
			xPos+=1;
		else if (xPos > xDestination)
			xPos-=1;
		if (yPos < yDestination)
			yPos+=1;
		else if (yPos > yDestination)
			yPos-=1;
		if (xPos == xDestination && yPos == yDestination && currentlyAcquired) {
			theCook.msgRelease();
			currentlyAcquired = false;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.lightGray); // shadow of Cook's name, marking where he goes to rest.
		g.drawString(theCook.getPerson().getName(), RESTX, RESTY+10);
		g.setColor(Color.PINK); // the Cook
		g.fillRect(xPos, yPos, WIDTH, WIDTH);
		g.setColor(Color.BLACK); // the label on the Cook
		g.drawString(theCook.getPerson().getName(), xPos, yPos+10);
		g.drawString(orderIcon, xPos, yPos+30);

	}

	// Movement
	public void DoLeave() {
		xDestination = RESTX;
		yDestination = RESTY;
	}

	public void toRef(){
		xDestination = CRX;
		yDestination = CRY+10;
	}

	public void toGrill(){
		xDestination = CGX;
		yDestination = CGY+10;
	}

	public void toPlate(){
		xDestination = CPX;
		yDestination = CPY+10;
	}

	// Getters
	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	// Setters
	/**
	 * For combined usage with the Cook itself
	 */
	public void setAcquired(){
		currentlyAcquired = true;
	}

	/**
	 * Sets the string above the Cook to show what he's holding.
	 * @param choice : what item is being ordered
	 */
	@Override
	public void setOrderIcon(FOOD_ITEMS choice){
		if(choice == FOOD_ITEMS.steak) orderIcon = "St";
		else if(choice == FOOD_ITEMS.chicken) orderIcon = "C";
		else if(choice == FOOD_ITEMS.salad) orderIcon = "Sa";
		else if(choice == FOOD_ITEMS.pizza) orderIcon = "P";
		else orderIcon = "";
	}

	// Utilities
	/**
	 * Returns true if the cook is present at the restaurant, else returns false.
	 * @return
	 */
	public boolean isPresent() {
		return true;
	}

}
