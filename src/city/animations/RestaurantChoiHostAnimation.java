package city.animations;

import java.awt.Graphics2D;

import city.animations.interfaces.RestaurantChoiAnimatedHost;
import city.bases.Animation;

public class RestaurantChoiHostAnimation extends Animation implements RestaurantChoiAnimatedHost {

	//Data
	
	//Constructor
	
	//Abstract
	@Override
	public void updatePosition() {} // host does not move

	@Override
	public void draw(Graphics2D g) {} // host is not visible; is a behind the scenes role.
	//when a person takes on the role of the host he is not visible inside the restaurant, but is still present.
	
	//Movement
	
	//Getters
	
	//Setters

	//Utilities
}
