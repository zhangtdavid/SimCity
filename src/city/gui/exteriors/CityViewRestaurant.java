package city.gui.exteriors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import city.gui.BuildingCard;

public class CityViewRestaurant extends CityViewBuilding {
	
	private static BufferedImage cityViewRestaurantImage = null;
	private static BufferedImage cityViewRestaurantZhangImage = null;
	private static BufferedImage cityViewRestaurantChoiImage = null;
	private static BufferedImage cityViewRestaurantChungImage = null;
	private static BufferedImage cityViewRestaurantJPImage = null;
	private static BufferedImage cityViewRestaurantTimmsImage = null;

	public CityViewRestaurant(int x, int y) {
		super(x, y, Color.red, "Restaurant 1");
		setRectangle(new Rectangle(x, y, 25, 25));
	}
	
	public CityViewRestaurant(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		setRectangle(new Rectangle(x, y, 25, 25));
		try {
			if(cityViewRestaurantImage == null)
				cityViewRestaurantImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRestaurantZhangImage.png"));
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
		//		super.paint(g);
		g.drawImage(cityViewRestaurantImage, x, y, null);
	}
}
