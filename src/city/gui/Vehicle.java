package city.gui;

import java.awt.*;
import java.awt.geom.*;


public class Vehicle extends Rectangle2D.Double {
	Color vehicleColor;
	
	public Vehicle( int x, int y, int width, int height ) {
		super( x, y, width, height );
	}
	public void setLocation( int x, int y ) {
		setRect( x, y, getWidth(), getHeight() );
	}
	
	public Color getColor() {
		return vehicleColor;
	}
	
	public void move( int xv, int yv ) {
		setRect( x+xv, y+yv, getWidth(), getHeight() );
	}
}
