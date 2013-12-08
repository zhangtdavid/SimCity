package city.gui.tabs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import trace.AlertTag;
import city.gui.MainFrame;

public class TraceTab extends JPanel {

	private static final long serialVersionUID = 9166425422374406573L;

	//============================================================================//
	// Data       
    //============================================================================//
	
	private MainFrame mainFrame;

	private JCheckBox toggleBank;
	private JCheckBox togglePerson;
	private JCheckBox toggleBus;
	private JCheckBox toggleCar;
	private JCheckBox toggleHouse;
	private JCheckBox toggleMarket;
	private JCheckBox toggleRestaurantZhang;
	private JCheckBox toggleRestaurantChoi;
	private JCheckBox toggleRestaurantChung;
	private JCheckBox toggleRestaurantJP;
	private JCheckBox toggleRestaurantTimms;
	private JPanel panelCheckboxes;
	private JPanel panelClear;
	private JButton btnClear;

	//============================================================================//
	// Constructor        
    //============================================================================//	

	public TraceTab(MainFrame f) {
		
		//--------------------------------------//
		// Start setup         
		//--------------------------------------//
		
		this.mainFrame = f;
		
		setBorder(new EmptyBorder(20, 20, 20, 20));
		setPreferredSize(new Dimension(300, 700));
		setMinimumSize(new Dimension(300, 700));
		setMaximumSize(new Dimension(300, 700));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		panelCheckboxes = new JPanel();
		panelCheckboxes.setPreferredSize(new Dimension(260, 300));
		panelCheckboxes.setMinimumSize(new Dimension(260, 300));
		panelCheckboxes.setMaximumSize(new Dimension(260, 300));
		add(panelCheckboxes);
		panelCheckboxes.setLayout(new BoxLayout(panelCheckboxes, BoxLayout.Y_AXIS));
		togglePerson = new JCheckBox("Show Tag: All People", true);
		panelCheckboxes.add(togglePerson);
		togglePerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.tracePanel.toggleAlertsWithTag(AlertTag.PERSON, togglePerson.isSelected());
			}
		});
		togglePerson.setFocusable(false);
		togglePerson.setSelected(false);
		toggleCar = new JCheckBox("Show Tag: Car", true);
		panelCheckboxes.add(toggleCar);
		toggleCar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.tracePanel.toggleAlertsWithTag(AlertTag.CAR, toggleCar.isSelected());
			}
		});
		toggleCar.setFocusable(false);
		toggleCar.setSelected(false);
		toggleBus = new JCheckBox("Show Tag: Bus", true);
		panelCheckboxes.add(toggleBus);
		toggleBus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.tracePanel.toggleAlertsWithTag(AlertTag.BUS, toggleBus.isSelected());
			}
		});
		toggleBus.setFocusable(false);
		toggleBus.setSelected(false);
		toggleHouse = new JCheckBox("Show Tag: House", true);
		panelCheckboxes.add(toggleHouse);
		toggleHouse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.tracePanel.toggleAlertsWithTag(AlertTag.HOUSE, toggleHouse.isSelected());
			}
		});
		toggleHouse.setFocusable(false);
		toggleHouse.setSelected(false);
		
				//--------------------------------------//
				// Create checkboxes
				//--------------------------------------//
				
				toggleBank = new JCheckBox("Show Tag: Bank", false);
				panelCheckboxes.add(toggleBank);
				toggleBank.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mainFrame.tracePanel.toggleAlertsWithTag(AlertTag.BANK, toggleBank.isSelected());
					}
				});
				toggleBank.setFocusable(false);
				toggleBank.setSelected(false);
				toggleMarket = new JCheckBox("Show Tag: Market", true);
				panelCheckboxes.add(toggleMarket);
				toggleMarket.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mainFrame.tracePanel.toggleAlertsWithTag(AlertTag.MARKET, toggleMarket.isSelected());
					}
				});
				toggleMarket.setFocusable(false);
				toggleMarket.setSelected(false);
				toggleRestaurantChoi = new JCheckBox("Show Tag: Restaurant Choi", true);
				panelCheckboxes.add(toggleRestaurantChoi);
				toggleRestaurantChoi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mainFrame.tracePanel.toggleAlertsWithTag(AlertTag.RESTAURANTCHOI, toggleRestaurantChoi.isSelected());
					}
				});
				toggleRestaurantChoi.setFocusable(false);
				toggleRestaurantChoi.setSelected(false);
				toggleRestaurantChung = new JCheckBox("Show Tag: Restaurant Chung", true);
				panelCheckboxes.add(toggleRestaurantChung);
				toggleRestaurantChung.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mainFrame.tracePanel.toggleAlertsWithTag(AlertTag.RESTAURANTCHUNG, toggleRestaurantChung.isSelected());
					}
				});
				toggleRestaurantChung.setFocusable(false);
				toggleRestaurantChung.setSelected(false);
				toggleRestaurantJP = new JCheckBox("Show Tag: Restaurant JP", true);
				panelCheckboxes.add(toggleRestaurantJP);
				toggleRestaurantJP.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mainFrame.tracePanel.toggleAlertsWithTag(AlertTag.RESTAURANTJP, toggleRestaurantJP.isSelected());
					}
				});
				toggleRestaurantJP.setFocusable(false);
				toggleRestaurantJP.setSelected(false);
				toggleRestaurantTimms = new JCheckBox("Show Tag: Restaurant Timms", true);
				panelCheckboxes.add(toggleRestaurantTimms);
				toggleRestaurantTimms.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mainFrame.tracePanel.toggleAlertsWithTag(AlertTag.RESTAURANTTIMMS, toggleRestaurantTimms.isSelected());
					}
				});
				toggleRestaurantTimms.setFocusable(false);
				toggleRestaurantTimms.setSelected(false);
				toggleRestaurantZhang = new JCheckBox("Show Tag: Restaurant Zhang", true);
				panelCheckboxes.add(toggleRestaurantZhang);
				toggleRestaurantZhang.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mainFrame.tracePanel.toggleAlertsWithTag(AlertTag.RESTAURANTZHANG, toggleRestaurantZhang.isSelected());
					}
				});
				toggleRestaurantZhang.setFocusable(false);
				toggleRestaurantZhang.setSelected(false);
				
				panelClear = new JPanel();
				panelClear.setAlignmentX(Component.LEFT_ALIGNMENT);
				panelClear.setPreferredSize(new Dimension(260, 100));
				panelClear.setMinimumSize(new Dimension(260, 100));
				panelClear.setMaximumSize(new Dimension(260, 100));
				add(panelClear);
				panelClear.setLayout(new BoxLayout(panelClear, BoxLayout.X_AXIS));
				
				btnClear = new JButton("Clear");
				btnClear.setFocusable(false);
				btnClear.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						clearTraceTabAndPanel();
					}
				});
				btnClear.setPreferredSize(new Dimension(260, 30));
				btnClear.setMinimumSize(new Dimension(260, 30));
				btnClear.setMaximumSize(new Dimension(260, 30));
				btnClear.setFont(getFont().deriveFont(Font.BOLD));
				panelClear.add(btnClear);
		
		//--------------------------------------//
		// Finish setup         
		//--------------------------------------//
		
		mainFrame.tracePanel.hideAllAlerts();
	
	}
	
	public void clearTraceTabAndPanel() {
		togglePerson.setSelected(false);
		toggleCar.setSelected(false);
		toggleBus.setSelected(false);
		toggleHouse.setSelected(false);
		toggleBank.setSelected(false);
		toggleMarket.setSelected(false);
		toggleRestaurantChoi.setSelected(false);
		toggleRestaurantChung.setSelected(false);
		toggleRestaurantJP.setSelected(false);
		toggleRestaurantTimms.setSelected(false);
		toggleRestaurantZhang.setSelected(false);
		mainFrame.tracePanel.hideAllAlerts();

	}
	
}
