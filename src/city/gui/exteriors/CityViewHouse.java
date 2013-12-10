package city.gui.exteriors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import city.gui.BuildingCard;

public class CityViewHouse extends CityViewBuilding {

	private static BufferedImage cityViewHouseImage = null;

	public CityViewHouse(int x, int y) {
		super(x, y, Color.WHITE, "House 1");
		setRectangle(new Rectangle(x, y, 25, 25));
	}

	public CityViewHouse(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		setRectangle(new Rectangle(x, y, 25, 25));
		try {
			if(cityViewHouseImage == null)
				cityViewHouseImage = ImageIO.read(CityViewHouse.class.getResource("/icons/cityView/CityViewHouseImage.png"));
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
			g.drawImage(cityViewHouseImage, x, y, null);
	}

}
