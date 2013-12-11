package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.animations.interfaces.RestaurantChungAnimatedCook;
import city.bases.Animation;
import city.gui.interiors.RestaurantChungPanel;
import city.roles.interfaces.RestaurantChungCook;

public class RestaurantChungCookAnimation extends Animation implements RestaurantChungAnimatedCook {
	private RestaurantChungCook agent = null;
	
	private boolean cooking = false;
	private String cookingItem;
	
	private boolean plating = false;
	private String platingItem;
	
//	Location Information
//	=====================================================================	
	private int xPos = RestaurantChungPanel.COOKHOMEX, yPos = RestaurantChungPanel.COOKHOMEY;
	private int xDestination = xPos, yDestination = yPos;
	private enum Command {noCommand, GoToCookHome, GoToGrill, GoToPlating};
	private Command command = Command.noCommand;
	
	
	public RestaurantChungCookAnimation(RestaurantChungCook c) {
		agent = c;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.GoToCookHome) {
				agent.msgAnimationAtCookHome();
				cookingItem = null;
				platingItem = null;
				cooking = false;
				plating = false;
			}
			else if (command == Command.GoToGrill) {
				agent.msgAnimationAtGrill();
				platingItem = null;
				plating = false;
			}
			else if (command == Command.GoToPlating) {
				agent.msgAnimationAtPlating();
				cookingItem = null;
				cooking = false;
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, RestaurantChungPanel.RECTDIM, RestaurantChungPanel.RECTDIM);
		g.setColor(Color.BLACK);
		if (cooking) {
			g.drawString(cookingItem, xPos+3, yPos+16);
		}
		else if (plating) {
			g.drawString(platingItem, xPos-25, yPos+13);
		}
	}

	public boolean isPresent() {
		return true;
	}
	
	@Override
    public void DoReturnToCookHome() {
        xDestination = RestaurantChungPanel.COOKHOMEX;
        yDestination = RestaurantChungPanel.COOKHOMEY;
		command = Command.GoToCookHome;
    }
    
	@Override
	public void DoGoToGrill(String item) {
        cookingItem = item;
        cooking = true;
        xDestination = RestaurantChungPanel.COOKGRILLX;
        yDestination = RestaurantChungPanel.COOKGRILLY;
		command = Command.GoToGrill;
	}
	
	@Override
	public void DoGoToPlating(String item) {
        platingItem = item;
        plating = true;
        xDestination = RestaurantChungPanel.PLATINGX;
        yDestination = RestaurantChungPanel.PLATINGY;
		command = Command.GoToPlating;
	}
}
