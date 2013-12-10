package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import city.Application;
import city.agents.interfaces.Bus;
import city.animations.interfaces.AnimatedBus;
import city.bases.Animation;
import city.bases.Building;
import city.buildings.interfaces.BusStop;
import city.gui.CityRoad;
import city.gui.CityRoadIntersection;
import city.gui.exteriors.CityViewApt;

public class BusAnimation extends Animation implements AnimatedBus {

	private Bus bus = null;

	private int xPos , yPos;//default waiter position
	private int xDestination, yDestination;//default start position

	private boolean atDestination = true;

	private static BufferedImage cityViewBusNorthImage = null;
	private static BufferedImage cityViewBusEastImage = null;
	private static BufferedImage cityViewBusSouthImage = null;
	private static BufferedImage cityViewBusWestImage = null;
	private BufferedImage imageToRender;

	public BusAnimation(Bus b, Building startingBuilding) {
		bus = b;
		xDestination = xPos = Application.CityMap.findClosestRoad(startingBuilding).getX();
		yDestination = yPos = Application.CityMap.findClosestRoad(startingBuilding).getY();
		Application.CityMap.findClosestRoad(startingBuilding).setVehicle(this);
		this.setVisible(true);
		try {
			if(cityViewBusNorthImage == null || cityViewBusEastImage == null || cityViewBusSouthImage == null || cityViewBusWestImage == null) {
				cityViewBusNorthImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewBusNorthImage.png"));
				cityViewBusEastImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewBusEastImage.png"));
				cityViewBusSouthImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewBusSouthImage.png"));
				cityViewBusWestImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewBusWestImage.png"));
				imageToRender = cityViewBusNorthImage;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updatePosition() {
		if(xPos <= xDestination + 1 && xPos >= xDestination - 1
				&& yPos <= yDestination + 1 && yPos >= yDestination - 1 && atDestination == false) {
			atDestination = true;
			bus.msgAtDestination();
		}
	}

	public void draw(Graphics2D g) {
		if(isUgly) {
			g.setColor(Color.CYAN);
			g.fillRect(xPos, yPos, SIZE, SIZE);
			g.setColor(Color.black);
			g.drawString("Bus", xPos, yPos + 10);
		} else {
			if(Application.trafficControl != null) {
				CityRoad currentRoad = Application.trafficControl.getRoadThatVehicleIsOn(this);
				if(currentRoad.getClass() == CityRoadIntersection.class)
					currentRoad = ((CityRoadIntersection)currentRoad).getCurrentNextRoad();
				if(currentRoad == null)
					imageToRender = cityViewBusEastImage;
				else if(currentRoad.getXVelocity() > 0)
					imageToRender = cityViewBusEastImage;
				else if(currentRoad.getXVelocity() < 0)
					imageToRender = cityViewBusWestImage;
				else if(currentRoad.getYVelocity() < 0)
					imageToRender = cityViewBusNorthImage;
				else if(currentRoad.getYVelocity() > 0)
					imageToRender = cityViewBusSouthImage;
			}
			g.drawImage(imageToRender, xPos, yPos, null);
		}
	}

	// Actions

	@Override
	public void goToDestination(Building destination) {
		xDestination = destination.getCityViewBuilding().getX();
		yDestination = destination.getCityViewBuilding().getY();
		atDestination = false;
	}

	@Override
	public void doGoToNextStop(BusStop nextStop) {
		xDestination = Application.CityMap.findClosestRoad(nextStop).getX();
		yDestination = Application.CityMap.findClosestRoad(nextStop).getY();
		atDestination = false;
	}

	// Getters

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public Bus getBus() {
		return bus;
	}

	public boolean isAtDestination() {
		return atDestination;
	}

	// Setters

	public void setXPos(int x) {
		xPos = x;
	}

	public void setYPos(int y) {
		yPos = y;
	}
}
