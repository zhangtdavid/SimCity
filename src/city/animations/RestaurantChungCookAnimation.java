package city.animations;

import java.awt.*;

import city.Animation;
import city.animations.interfaces.RestaurantChungAnimatedCashier;
import city.interfaces.RestaurantChungCook;

public class RestaurantChungCookAnimation extends Animation implements RestaurantChungAnimatedCook {
	private RestaurantChungCook agent = null;
	private RestaurantGui gui;
	
	private boolean cooking = false;
	private String cookingItem;
	
	private boolean plating = false;
	private String platingItem;
	

//	Fixed Numbers
//	=====================================================================
	private static final int CRECTDIM = 20;
    private static final int xCookHome = 500, yCookHome = 60;	
	private static final int xGrill = 500, yGrill = 80;
	private static final int xPlating = 480, yPlating = 60;
	
//	Location Information
//	=====================================================================	
	private int xPos = 500, yPos = 60;
	private int xDestination = xPos, yDestination = yPos;
	private enum Command {noCommand, GoToCookHome, GoToGrill, GoToPlating};
	private Command command = Command.noCommand;
	
	
	public RestaurantChungCookAnimation(RestaurantChungCook c, RestaurantGui gui) {
		agent = c;
		this.gui = gui;
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
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, CRECTDIM, CRECTDIM);
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
	
    public void DoReturnToCookHome() {
        xDestination = xCookHome;
        yDestination = yCookHome;
		command = Command.GoToCookHome;
    }
    
	public void DoGoToGrill(String item) {
        cookingItem = item;
        cooking = true;
        xDestination = xGrill;
        yDestination = yGrill;
		command = Command.GoToGrill;
	}
	
	public void DoGoToPlating(String item) {
        platingItem = item;
        plating = true;
        xDestination = xPlating;
        yDestination = yPlating;
		command = Command.GoToPlating;
	}
}
