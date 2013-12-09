package city.gui.exteriors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import city.gui.BuildingCard;

public class CityViewBank extends CityViewBuilding {
	
	private static BufferedImage cityViewBankImage = null;

	public CityViewBank(int x, int y) {
		super(x, y, Color.red, "Bank 1");
		setRectangle(new Rectangle(x, y, 25, 25));
	}

	public CityViewBank(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		setRectangle(new Rectangle(x, y, 25, 25));
		try {
			if(cityViewBankImage == null)
				cityViewBankImage = ImageIO.read(CityViewBank.class.getResource("/icons/cityView/CityViewBankImage.png"));
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
		g.drawImage(cityViewBankImage, x, y, null);
	}
}
