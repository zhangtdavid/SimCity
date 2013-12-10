package city.gui.exteriors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import city.gui.BuildingCard;

public class CityViewApt extends CityViewBuilding {

	private static BufferedImage cityViewAptImage = null;

	public CityViewApt(int x, int y) {
		super(x, y, Color.WHITE, "Apt 1");
		setRectangle(new Rectangle(x, y, 25, 25));
	}

	public CityViewApt(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		setRectangle(new Rectangle(x, y, 25, 25));
		try {
			if(cityViewAptImage == null)
				cityViewAptImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewAptImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		if(isUgly)
			super.paint(g);
		else
			g.drawImage(cityViewAptImage, x, y, null);
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub

	}

}
