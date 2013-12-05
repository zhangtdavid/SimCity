package utilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Timer;

import city.gui.CityRoad;

public class TrafficControl implements ActionListener {
	List<CityRoad> roads;
	Timer stopLightTimer = new Timer(10000, this);
	
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
		// TODO Auto-generated method stub
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
}
