package utilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import city.bases.interfaces.AnimationInterface;
import city.gui.CityRoad;

public class TrafficControl implements ActionListener {
	List<CityRoad> roads;
	Timer stopLightTimer = new Timer(3000, this);

	public TrafficControl(List<CityRoad> roads) {
		this.roads = roads;
		stopLightTimer.start();
		for(CityRoad r : roads) {
			if(r.isRedLight() && r.getStopLightType() == CityRoad.STOPLIGHTTYPE.HORIZONTALOFF) {
				r.setStopLightType(CityRoad.STOPLIGHTTYPE.HORIZONTALON);
			}
			if(r.isRedLight() && r.getStopLightType() == CityRoad.STOPLIGHTTYPE.VERTICALON) {
				r.setStopLightType(CityRoad.STOPLIGHTTYPE.VERTICALOFF);
			}
		}
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
