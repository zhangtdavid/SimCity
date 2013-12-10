package city.gui.exteriors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import city.gui.BuildingCard;

public class CityViewMarket extends CityViewBuilding{

	private static BufferedImage cityViewMarketImage = null;

	public CityViewMarket(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		setRectangle(new Rectangle(x, y, 25, 25));
		try {
			if(cityViewMarketImage == null)
				cityViewMarketImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewMarketImage.png"));
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
			g.drawImage(cityViewMarketImage, x, y, null);
	}

}
