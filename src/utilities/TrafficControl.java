package utilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import city.Application;
import city.bases.interfaces.AnimationInterface;
import city.gui.CityRoad;
import city.gui.CityRoadIntersection;
import city.gui.exteriors.CityViewApt;

public class TrafficControl implements ActionListener {
	List<CityRoad> roads;
	Timer stopLightTimer = new Timer(3000, this);
	
	private static BufferedImage cityViewRoadCrosswalkEastWestImage = null;
	private static BufferedImage cityViewRoadCrosswalkNorthSouthImage = null;
	
	private static BufferedImage cityViewRoadIntersectionAllImage = null;
	private static BufferedImage cityViewRoadIntersectionNorthImage = null;
	private static BufferedImage cityViewRoadIntersectionEastImage = null;
	private static BufferedImage cityViewRoadIntersectionSouthImage = null;
	private static BufferedImage cityViewRoadIntersectionWestImage = null;
	
	private static BufferedImage cityViewRoadHorizontalImage = null;
	private static BufferedImage cityViewRoadVerticalImage = null;
	private static BufferedImage cityViewRoadNorthEastTurnImage = null;
	private static BufferedImage cityViewRoadNorthWestTurnImage = null;
	private static BufferedImage cityViewRoadSouthEastTurnImage = null;
	private static BufferedImage cityViewRoadSouthWestTurnImage = null;

	public TrafficControl(List<CityRoad> roads) {
		// Images for turns and straights
		try {
			if(cityViewRoadHorizontalImage == null || cityViewRoadVerticalImage == null ||
					cityViewRoadNorthEastTurnImage == null || cityViewRoadNorthWestTurnImage == null ||
					cityViewRoadSouthEastTurnImage == null || cityViewRoadSouthWestTurnImage == null ||
					cityViewRoadCrosswalkEastWestImage == null || cityViewRoadCrosswalkNorthSouthImage == null) {
				cityViewRoadCrosswalkEastWestImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadCrosswalkEastWestImage.png"));
				cityViewRoadCrosswalkNorthSouthImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadCrosswalkNorthSouthImage.png"));
				cityViewRoadHorizontalImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadHorizontalImage.png"));
				cityViewRoadVerticalImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadVerticalImage.png"));
				cityViewRoadNorthEastTurnImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadNorthEastTurnImage.png"));
				cityViewRoadNorthWestTurnImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadNorthWestTurnImage.png"));
				cityViewRoadSouthEastTurnImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadSouthEastTurnImage.png"));
				cityViewRoadSouthWestTurnImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadSouthWestTurnImage.png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Images for intersections
		try {
			if(cityViewRoadIntersectionAllImage == null || cityViewRoadIntersectionNorthImage == null ||
					cityViewRoadIntersectionEastImage == null || cityViewRoadIntersectionSouthImage == null ||
							cityViewRoadIntersectionWestImage == null)
				cityViewRoadIntersectionAllImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadIntersectionAllImage.png"));
			cityViewRoadIntersectionNorthImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadIntersectionNorthImage.png"));
			cityViewRoadIntersectionEastImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadIntersectionEastImage.png"));
			cityViewRoadIntersectionSouthImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadIntersectionSouthImage.png"));
			cityViewRoadIntersectionWestImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewRoadIntersectionWestImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Setup roads
		this.roads = roads;
		for(CityRoad r : roads) {
			// Images
			if(getRoadAt(r.getX(), r.getY() - 25) != null && getRoadAt(r.getX() + 25, r.getY()) != null
					&& getRoadAt(r.getX(), r.getY() + 25) != null && getRoadAt(r.getX() - 25, r.getY()) != null)
				r.setImageToRender(cityViewRoadIntersectionAllImage);
			else if(getRoadAt(r.getX(), r.getY() - 25) != null && getRoadAt(r.getX() + 25, r.getY()) != null
					&& getRoadAt(r.getX(), r.getY() + 25) != null)
				r.setImageToRender(cityViewRoadIntersectionWestImage);
			else if(getRoadAt(r.getX(), r.getY() - 25) != null && getRoadAt(r.getX() + 25, r.getY()) != null
					&& getRoadAt(r.getX() - 25, r.getY()) != null)
				r.setImageToRender(cityViewRoadIntersectionSouthImage);
			else if(getRoadAt(r.getX(), r.getY() - 25) != null && getRoadAt(r.getX(), r.getY() + 25) != null
					&& getRoadAt(r.getX() - 25, r.getY()) != null)
				r.setImageToRender(cityViewRoadIntersectionEastImage);
			else if(getRoadAt(r.getX() + 25, r.getY()) != null && getRoadAt(r.getX(), r.getY() + 25) != null
					&& getRoadAt(r.getX() - 25, r.getY()) != null)
				r.setImageToRender(cityViewRoadIntersectionNorthImage);
			else if(getRoadAt(r.getX(), r.getY() - 25) != null && getRoadAt(r.getX() + 25, r.getY()) != null)
				r.setImageToRender(cityViewRoadNorthEastTurnImage);
			else if(getRoadAt(r.getX(), r.getY() - 25) != null && getRoadAt(r.getX() - 25, r.getY()) != null)
				r.setImageToRender(cityViewRoadNorthWestTurnImage);
			else if(getRoadAt(r.getX(), r.getY() + 25) != null && getRoadAt(r.getX() + 25, r.getY()) != null)
				r.setImageToRender(cityViewRoadSouthEastTurnImage);
			else if(getRoadAt(r.getX(), r.getY() + 25) != null && getRoadAt(r.getX() - 25, r.getY()) != null)
				r.setImageToRender(cityViewRoadSouthWestTurnImage);
			else if(r.getHorizontal())
				r.setImageToRender(cityViewRoadHorizontalImage);
			else if(!r.getHorizontal())
				r.setImageToRender(cityViewRoadVerticalImage);
			// Crosswalks
			CityRoad northRoad = getRoadAt(r.getX(), r.getY() - 25);
			CityRoad eastRoad = getRoadAt(r.getX() + 25, r.getY());
			CityRoad southRoad = getRoadAt(r.getX(), r.getY() + 25);
			CityRoad westRoad = getRoadAt(r.getX() - 25, r.getY());
			if(northRoad != null && southRoad != null) {
				if((northRoad.isRedLight() || southRoad.isRedLight()) &&
						(northRoad.getClass() == CityRoadIntersection.class || southRoad.getClass() == CityRoadIntersection.class))
					r.setImageToRender(cityViewRoadCrosswalkEastWestImage);
			}
			if(eastRoad != null && westRoad != null) {
				if((eastRoad.isRedLight() || westRoad.isRedLight()) &&
						(eastRoad.getClass() == CityRoadIntersection.class || westRoad.getClass() == CityRoadIntersection.class))
					r.setImageToRender(cityViewRoadCrosswalkNorthSouthImage);
			}
			// Stop light
			if(r.isRedLight() && r.getStopLightType() == CityRoad.STOPLIGHTTYPE.HORIZONTALOFF) {
				r.setStopLightType(CityRoad.STOPLIGHTTYPE.HORIZONTALON);
			}
			if(r.isRedLight() && r.getStopLightType() == CityRoad.STOPLIGHTTYPE.VERTICALON) {
				r.setStopLightType(CityRoad.STOPLIGHTTYPE.VERTICALOFF);
			}
			Application.getMainFrame().cityView.addMoving(r);
		}
		stopLightTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(CityRoad r : roads) {
			if(r.isRedLight() && r.getStopLightType() == CityRoad.STOPLIGHTTYPE.HORIZONTALOFF) {
				r.setStopLightType(CityRoad.STOPLIGHTTYPE.HORIZONTALON);
			} else if(r.isRedLight() && r.getStopLightType() == CityRoad.STOPLIGHTTYPE.HORIZONTALON) {
				r.setStopLightType(CityRoad.STOPLIGHTTYPE.HORIZONTALOFF);
			}
			if(r.isRedLight() && r.getStopLightType() == CityRoad.STOPLIGHTTYPE.VERTICALON) {
				r.setStopLightType(CityRoad.STOPLIGHTTYPE.VERTICALOFF);
			} else if(r.isRedLight() && r.getStopLightType() == CityRoad.STOPLIGHTTYPE.VERTICALOFF) {
				r.setStopLightType(CityRoad.STOPLIGHTTYPE.VERTICALON);
			}
		}
		stopLightTimer.restart();
	}

	public CityRoad getRoadAt(int x, int y) {
		for(CityRoad r : roads) {
			if(r.contains(x, y))
				return r;
		}
		return null;
	}

	public CityRoad getClosestRoad(int x, int y) {
		double closestDistance = 10000000;
		CityRoad closestRoad = null;
		for(CityRoad r : roads) {
			double distance = Math.sqrt((double)(Math.pow(r.getX() - x, 2) + Math.pow(r.getY() - y, 2)));
			if( distance < closestDistance) {
				closestDistance = distance;
				closestRoad = r;
			}
		}
		return closestRoad;
	}
	
	public List<AnimationInterface> getAllVehicles() {
		List<AnimationInterface> listToReturn = new ArrayList<AnimationInterface>();
		for(CityRoad r : roads) {
			if(r.getVehicle() != null)
				listToReturn.add(r.getVehicle());
		}
		return listToReturn;
	}
}
