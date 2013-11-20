package city.animations;

import java.awt.*;

import city.Animation;
import city.animations.interfaces.RestaurantChoiAnimatedCook;
import city.interfaces.RestaurantChoiCook;

public class RestaurantChoiCookAnimation extends Animation implements RestaurantChoiAnimatedCook {

	//Data
	private int xPos, yPos, xDestination, yDestination;

	private boolean currentlyAcquired;
	boolean init;
	private String orderIcon = new String();
	RestaurantChoiCook the_cook;
	
	//Constructor
	public RestaurantChoiCookAnimation(RestaurantChoiCook c) {
		this.the_cook = c;
		xPos = restingCoordX;
		xDestination = restingCoordX;
		yPos = restingCoordY;
		yDestination = restingCoordY;
	}
	
	//Abstract
	@Override
	public void updatePosition() {
		if (xPos < xDestination)
		xPos+=2;
	else if (xPos > xDestination)
		xPos-=2;
	if (yPos < yDestination)
		yPos+=2;
	else if (yPos > yDestination)
		yPos-=2;
	if (xPos == xDestination && yPos == yDestination && currentlyAcquired) {
		//the_cook.msgRelease(); TODO deal with semaphore usage with Cook role, etc.
		currentlyAcquired = false;
	}
		
	}

	@Override
	public void draw(Graphics2D g) {
        g.setColor(Color.lightGray); // shadow of Cook's name, marking where he goes to rest.
        g.drawString(the_cook.getName(), restingCoordX, restingCoordY+20);
		g.setColor(Color.PINK); // the Cook
		g.fillRect(xPos, yPos, WIDTH, WIDTH);
		g.setColor(Color.BLACK); // the label on the Cook
		g.drawString(the_cook.getName(), xPos, yPos+10);
		g.drawString(orderIcon, xPos, yPos+30);
		
	}

	// Movement
	public void DoLeave() {
		xDestination = restingCoordX;
		yDestination = restingCoordY;
	}
	
	public void toRef(){
		xDestination = CRX;
		yDestination = CRY;
	}
	
	public void toGrill(){
		xDestination = CGX;
		yDestination = CGY;
	}
	
	public void toPlate(){
		xDestination = CPX;
		yDestination = CPY;
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
	public void setOrderIcon(int choice){
		switch(choice){
		case 1:
			orderIcon = "St";
			break;
		case 2:
			orderIcon = "C";
			break;
		case 3:
			orderIcon = "Sa";
			break;
		case 4:
			orderIcon = "P";
			break;
		default:
			orderIcon = "";
			break;
		}
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
