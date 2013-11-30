package city.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import trace.AlertTag;

public class CityControlPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 9166425422374406573L;

	MainFrame mainframe;
	public static final int CP_WIDTH = 200, CP_HEIGHT = 700;
	JButton addRestaurantZhang, addRestaurantChoi, addRestaurantJP, addRestaurantTimms, addRestaurantChung, addBank;

	//For managing traces
	JRadioButton toggleBank;
	JRadioButton togglePerson;
	JRadioButton toggleBus;
	JRadioButton toggleCar;
	JRadioButton toggleHouse;
	JRadioButton toggleMarket;
	JRadioButton toggleRestaurantZhang;
	JRadioButton toggleRestaurantChoi;
	JRadioButton toggleRestaurantChung;
	JRadioButton toggleRestaurantJP;
	JRadioButton toggleRestaurantTimms;


	String name = "Control Panel";

	public CityControlPanel(MainFrame mf) {
		this.mainframe = mf;
		this.setPreferredSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setMaximumSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setMinimumSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setVisible(true);

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		int gridBagYPos = 0;

		//Trace panel buttons
		toggleBank = new JRadioButton("Show Tag: BANK", true);
		toggleBank.addActionListener(this);
		toggleBank.setSelected(false);
		mainframe.tracePanel.hideAlertsWithTag(AlertTag.BANK);
		togglePerson = new JRadioButton("Show Tag: Person", true);
		togglePerson.addActionListener(this);
		togglePerson.setSelected(false);
		mainframe.tracePanel.hideAlertsWithTag(AlertTag.PERSON);
		toggleBus = new JRadioButton("Show Tag: Bus", true);
		toggleBus.addActionListener(this);
		toggleBus.setSelected(false);
		mainframe.tracePanel.hideAlertsWithTag(AlertTag.BUS);
		toggleCar = new JRadioButton("Show Tag: Car", true);
		toggleCar.addActionListener(this);
		toggleCar.setSelected(false);
		mainframe.tracePanel.hideAlertsWithTag(AlertTag.CAR);
		toggleHouse = new JRadioButton("Show Tag: House", true);
		toggleHouse.addActionListener(this);
		toggleHouse.setSelected(false);
		mainframe.tracePanel.hideAlertsWithTag(AlertTag.HOUSE);
		toggleMarket = new JRadioButton("Show Tag: Market", true);
		toggleMarket.addActionListener(this);
		toggleMarket.setSelected(false);
		mainframe.tracePanel.hideAlertsWithTag(AlertTag.MARKET);
		toggleRestaurantZhang = new JRadioButton("Show Tag: Restaurant Zhang", true);
		toggleRestaurantZhang.addActionListener(this);
		toggleRestaurantZhang.setSelected(false);
		mainframe.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTZHANG);
		toggleRestaurantChoi = new JRadioButton("Show Tag: Restaurant Choi", true);
		toggleRestaurantChoi.addActionListener(this);
		toggleRestaurantChoi.setSelected(false);
		mainframe.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTCHOI);
		toggleRestaurantChung = new JRadioButton("Show Tag: Restaurant Chung", true);
		toggleRestaurantChung.addActionListener(this);
		toggleRestaurantChung.setSelected(false);
		mainframe.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTCHUNG);
		toggleRestaurantJP = new JRadioButton("Show Tag: Restaurant JP", true);
		toggleRestaurantJP.addActionListener(this);
		toggleRestaurantJP.setSelected(false);
		toggleRestaurantTimms = new JRadioButton("Show Tag: Restaurant Timms", true);
		mainframe.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTTIMMS);
		toggleRestaurantTimms.addActionListener(this);
		toggleRestaurantTimms.setSelected(false);

		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleBank, c);
		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(togglePerson, c);
		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleBus, c);
		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleCar, c);
		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleHouse, c);
		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleMarket, c);
		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleRestaurantZhang, c);
		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleRestaurantChoi, c);
		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleRestaurantChung, c);
		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleRestaurantJP, c);
		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleRestaurantTimms, c);

//		addRestaurantZhang = new JButton("Add RestaurantZhang");
//		addRestaurantZhang.addActionListener(this);
//		c.gridx = 0; c.gridy = gridBagYPos++;
//		//		add(addRestaurantZhang, c);
//
//		addRestaurantChoi = new JButton("Add RestaurantChoi");
//		addRestaurantChoi.addActionListener(this);
//		c.gridx = 0; c.gridy = gridBagYPos++;
//		//		add(addRestaurantChoi, c);
//
//		addRestaurantJP = new JButton("Add RestaurantJP");
//		addRestaurantJP .addActionListener(this);
//		c.gridx = 0; c.gridy = gridBagYPos++;
//		//		add(addRestaurantJP, c);
//
//		addRestaurantTimms = new JButton("Add RestaurantTimms");
//		addRestaurantTimms.addActionListener(this);
//		c.gridx = 0; c.gridy = gridBagYPos++;
//		//		add(addRestaurantTimms, c);
//
//		addRestaurantChung = new JButton("Add RestaurantChung");
//		addRestaurantChung.addActionListener(this);
//		c.gridx = 0; c.gridy = gridBagYPos++;
//		//		add(addRestaurantChung, c);
//
//		addBank = new JButton("Add Bank");
//		addBank.addActionListener(this);
//		c.gridx = 0; c.gridy = gridBagYPos++;
//		//		add(addBank, c);
	}

	public void actionPerformed(ActionEvent e) {
//		if (e.getSource().equals(addRestaurantZhang)) {
//			mainframe.cityView.addObject(CityViewBuilding.BuildingType.RESTAURANTZHANG);
//			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New RestaurantZhang");
//		} else if (e.getSource().equals(addRestaurantChoi)) {
//			mainframe.cityView.addObject(CityViewBuilding.BuildingType.RESTAURANTCHOI);
//			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New RestaurantChoi");
//		} else if (e.getSource().equals(addRestaurantJP)) {
//			mainframe.cityView.addObject(CityViewBuilding.BuildingType.RESTAURANTJP);
//			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New RestaurantJP");
//		} else if (e.getSource().equals(addRestaurantTimms)) {
//			mainframe.cityView.addObject(CityViewBuilding.BuildingType.RESTAURANTTIMMS);
//			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New RestaurantTimms");
//		} else if (e.getSource().equals(addRestaurantChung)) {
//			mainframe.cityView.addObject(CityViewBuilding.BuildingType.RESTAURANTCHUNG);
//			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New RestaurantChung");
//		} else if (e.getSource().equals(addBank)) {
//			AlertLog.getInstance().logInfo(AlertTag.BANK, this.name, "Adding New Bank");
//			mainframe.cityView.addObject(CityViewBuilding.BuildingType.BANK);
//		}
		// TOGGLES
		if(e.getSource().equals(toggleBank)) {
			if(toggleBank.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.BANK);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.BANK);
		}
		else if(e.getSource().equals(togglePerson)) {
			if(togglePerson.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.PERSON);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.PERSON);
		}
		else if(e.getSource().equals(toggleBus)) {
			if(toggleBus.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.BUS);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.BUS);
		}
		else if(e.getSource().equals(toggleCar)) {
			if(toggleCar.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.CAR);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.CAR);
		}
		else if(e.getSource().equals(toggleHouse)) {
			if(toggleHouse.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.HOUSE);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.HOUSE);
		}
		else if(e.getSource().equals(toggleMarket)) {
			if(toggleMarket.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.MARKET);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.MARKET);
		}
		else if(e.getSource().equals(toggleRestaurantZhang)) {
			if(toggleRestaurantZhang.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.RESTAURANTZHANG);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTZHANG);
		}
		else if(e.getSource().equals(toggleRestaurantChoi)) {
			if(toggleRestaurantChoi.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCHOI);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTCHOI);
		}
		else if(e.getSource().equals(toggleRestaurantChung)) {
			if(toggleRestaurantChung.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCHUNG);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTCHUNG);
		}
		else if(e.getSource().equals(toggleRestaurantJP)) {
			if(toggleRestaurantJP.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.RESTAURANTJP);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTJP);
		}
		else if(e.getSource().equals(toggleRestaurantTimms)) {
			if(toggleRestaurantTimms.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.RESTAURANTTIMMS);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTTIMMS);
		}
	}
}
