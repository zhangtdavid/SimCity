package city.gui.exteriors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import city.gui.BuildingCard;

public class CityViewRestaurantZhang extends CityViewBuilding {
	
	private static BufferedImage cityViewRestaurantZhangImage = null;

	public CityViewRestaurantZhang(int x, int y) {
		super(x, y, Color.red, "Restaurant Zhang 1");
		setRectangle(new Rectangle(x, y, 25, 25));
	}
	
	public CityViewRestaurantZhang(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		setRectangle(new Rectangle(x, y, 25, 25));
		try {
			if(cityViewRestaurantZhangImage == null)
				cityViewRestaurantZhangImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRestaurantZhangImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void paint(Graphics g) {
		if(isUgly)
			super.paint(g);
		else
			g.drawImage(cityViewRestaurantZhangImage, x, y, null);
	}
}
