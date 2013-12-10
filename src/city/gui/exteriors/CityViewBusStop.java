package city.gui.exteriors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import city.gui.BuildingCard;

public class CityViewBusStop extends CityViewBuilding {

	private static BufferedImage cityViewBusStopImage = null;

	public CityViewBusStop(int x, int y) {
		super(x, y, Color.white, "Bus Stop 1");
		setRectangle(new Rectangle(x, y, 25, 25));
	}

	public CityViewBusStop(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		setRectangle(new Rectangle(x, y, 25, 25));
		try {
			if(cityViewBusStopImage == null)
				cityViewBusStopImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewBusStopImage.png"));
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
			g.drawImage(cityViewBusStopImage, x, y, null);
	}
}
