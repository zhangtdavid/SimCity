package city.gui.tabs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import city.gui.MainFrame;
import city.gui.exteriors.CityViewBuilding;

public class AddBuildingTab extends JPanel implements ActionListener{

	private static final long serialVersionUID = 9166425422374406573L;

	private MainFrame mainFrame;

	// TODO add controls like buttons, radio buttons, etc. here
	private JButton buttonBank;
	private JButton buttonApt;
	private JButton buttonHouse;
	private JButton buttonMarket;
	private JButton buttonRestaurantZhang;
	private JButton buttonRestaurantChoi;
	private JButton buttonRestaurantChung;
	private JButton buttonRestaurantJP;
	private JButton buttonRestaurantTimms;
	private JButton buttonTogglePrettyView;

	public AddBuildingTab(MainFrame mf) {
		mainFrame = mf;
		this.setPreferredSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMaximumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMinimumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setVisible(true);

		this.setLayout(new FlowLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		buttonBank = new JButton("Add Bank");
		add(buttonBank);
		buttonBank.addActionListener(this);
		buttonApt = new JButton("Add Apartment");
		add(buttonApt);
		buttonApt.addActionListener(this);
		buttonHouse = new JButton("Add House");
		add(buttonHouse);
		buttonHouse.addActionListener(this);
		buttonMarket = new JButton("Add Market");
		add(buttonMarket);
		buttonMarket.addActionListener(this);
		buttonRestaurantZhang = new JButton("Add RestaurantZhang");
		add(buttonRestaurantZhang);
		buttonRestaurantZhang.addActionListener(this);
		buttonRestaurantChoi = new JButton("Add RestaurantChoi");
		add(buttonRestaurantChoi);
		buttonRestaurantChoi.addActionListener(this);
		buttonRestaurantChung = new JButton("Add RestaurantChung");
		add(buttonRestaurantChung);
		buttonRestaurantChung.addActionListener(this);
		buttonRestaurantJP = new JButton("Add RestaurantJP");
		add(buttonRestaurantJP);
		buttonRestaurantJP.addActionListener(this);
		buttonRestaurantTimms = new JButton("Add RestaurantTimms");
		add(buttonRestaurantTimms);
		buttonRestaurantTimms.addActionListener(this);
		buttonTogglePrettyView = new JButton("Toggle Ugly! (Debug View)");
		add(buttonTogglePrettyView);
		buttonTogglePrettyView.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if(mainFrame.cityView.addingObject == true) {
			mainFrame.cityView.statics.remove(mainFrame.cityView.statics.remove(mainFrame.cityView.statics.size() - 1));
			mainFrame.cityView.addingObject = false;
		}
		if(e.getSource().equals(buttonTogglePrettyView)) {
			mainFrame.cityView.setUgly(!mainFrame.cityView.getUgly());
		} else if(e.getSource().equals(buttonApt)) {
			mainFrame.cityView.addObject(CityViewBuilding.BUILDINGTYPE.APT);
		} else if (e.getSource().equals(buttonHouse)) {
			mainFrame.cityView.addObject(CityViewBuilding.BUILDINGTYPE.HOUSE);
		} else if (e.getSource().equals(buttonMarket)) {
			mainFrame.cityView.addObject(CityViewBuilding.BUILDINGTYPE.MARKET);
		} else if (e.getSource().equals(buttonRestaurantZhang)) {
			mainFrame.cityView.addObject(CityViewBuilding.BUILDINGTYPE.RESTAURANTZHANG);
		} else if (e.getSource().equals(buttonRestaurantChoi)) {
			mainFrame.cityView.addObject(CityViewBuilding.BUILDINGTYPE.RESTAURANTCHOI);
		} else if (e.getSource().equals(buttonRestaurantJP)) {
			mainFrame.cityView.addObject(CityViewBuilding.BUILDINGTYPE.RESTAURANTJP);
		} else if (e.getSource().equals(buttonRestaurantTimms)) {
			mainFrame.cityView.addObject(CityViewBuilding.BUILDINGTYPE.RESTAURANTTIMMS);
		} else if (e.getSource().equals(buttonRestaurantChung)) {
			mainFrame.cityView.addObject(CityViewBuilding.BUILDINGTYPE.RESTAURANTCHUNG);
		} else if (e.getSource().equals(buttonBank)) {
			mainFrame.cityView.addObject(CityViewBuilding.BUILDINGTYPE.BANK);
		}
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}
}
