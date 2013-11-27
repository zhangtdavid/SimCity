package city.gui;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class Vehicle extends Rectangle2D.Double {

	private static final long serialVersionUID = 9171576656515889762L;
	
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
