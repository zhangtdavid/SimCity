package city.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import utilities.DataModel;
import city.Application;
import city.BuildingInterface;
import city.RoleInterface;
import city.agents.CarAgent;
import city.animations.CarAnimation;
import city.buildings.HouseBuilding;
import city.interfaces.Person;

public class CityControlPanelEditPersonTab extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 9166425422374406573L;
	
	// Data
	
	private MainFrame mainFrame;
	private DataModel dataModel;
	private DefaultListModel<Person> peopleListModel;
	private DefaultListModel<RoleInterface> roleListModel;
	private JScrollPane scrollPeople;
	private JList<Person> listPeople;
	private JPanel panelName;
	private JLabel labelName;
	private JTextField textName;
	private JPanel panelCash;
	private JLabel labelCash;
	private JFormattedTextField textCash;
	private JPanel panelControl;
	private JButton btnRevert;
	private JButton btnSave;
	private JPanel panelState;
	private JLabel labelState;
	private JLabel labelStateValue;
	private Person listPeopleSelection; // Needed so that we can deactivate old listeners
	private JPanel panelRoles;
	private JScrollPane scrollRoles;
	private JList<RoleInterface> listRoles;
	private JLabel labelRoles;
	private RoleInterface listRolesSelection;
	private JPanel panelRoleState;
	private JLabel labelRoleState;
	private JLabel labelRoleStateValue;
	private JPanel panelRoleActive;
	private JLabel labelRoleActive;
	private JLabel labelRoleActiveValue;
	private JPanel panelRoleActivity;
	private JLabel labelRoleActivity;
	private JLabel labelRoleActivityValue;
	private JPanel panelToggles;
	private JButton btnHasCar;
	private JButton btnHouse;
	private JButton btnWork;
	private JButton btnHasHouse;
	private final ImageIcon car = new ImageIcon(CityControlPanelEditPersonTab.class.getResource("/icons/car.png"));
	private final ImageIcon nocar = new ImageIcon(CityControlPanelEditPersonTab.class.getResource("/icons/nocar.png"));
	private final ImageIcon house = new ImageIcon(CityControlPanelEditPersonTab.class.getResource("/icons/home.png"));
	private final ImageIcon apartment = new ImageIcon(CityControlPanelEditPersonTab.class.getResource("/icons/apartment.png"));
	private final ImageIcon job = new ImageIcon(CityControlPanelEditPersonTab.class.getResource("/icons/job.png"));
	private final ImageIcon nojob = new ImageIcon(CityControlPanelEditPersonTab.class.getResource("/icons/nojob.png"));
	private JPanel panelShift;
	private JLabel lblShift;
	private JLabel lblShiftStart;
	private JLabel lblShiftEnd;
	private JSpinner spnShiftStart;
	private JSpinner spnShiftEnd;
	private JPanel panelSalary;
	private JLabel lblRoleSalary;
	private JLabel labelRoleSalaryValue;
	private JPanel panelRoleControl;
	private JButton btnRoleRevert;
	private JButton btnRoleSave;
	private JButton btnHasJob;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(getDefaultLocale());
	
	// Constructor
		
	public CityControlPanelEditPersonTab(MainFrame mf) {
		// Set up variables
		this.mainFrame = mf;
		this.setVisible(true);
	    this.currencyFormat.setRoundingMode(RoundingMode.DOWN);
	    this.currencyFormat.setParseIntegerOnly(true);

		// Set up the JPanel
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.setAlignmentY(Component.TOP_ALIGNMENT);
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.setPreferredSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMaximumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMinimumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// The DataModel allows the list objects to auto-update
		this.dataModel = Application.getModel();
		this.dataModel.getPropertyChangeSupport().addPropertyChangeListener(DataModel.PEOPLE, this);
		
		// The ListModel is an object which stores what the JList displays
		this.peopleListModel = new DefaultListModel<Person>();
		this.roleListModel = new DefaultListModel<RoleInterface>();
        
		// Scroll for the list
		scrollPeople = new JScrollPane();
		scrollPeople.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPeople.setPreferredSize(new Dimension(300, 150));
		scrollPeople.setMinimumSize(new Dimension(300, 150));
		scrollPeople.setMaximumSize(new Dimension(300, 150));
		scrollPeople.setBorder(new LineBorder(SystemColor.menu, 3));
		this.add(scrollPeople);
		
        // Create the JList
		listPeople = new JList<Person>(peopleListModel);
		listPeople.addListSelectionListener(new ListSelectionListener() {
			/**
			 * When a Person is selected from the list, this is the code that kicks off the GUI update
			 * It also sets up a listener so that the GUI will update with changes for as long as that Person remains selected
			 */
			public void valueChanged(ListSelectionEvent arg0) {
				if (listPeople.getSelectedValue() != null) {
					if (listPeopleSelection != null) {
						// Remove the listener from the previous selection
						listPeopleSelection.getPropertyChangeSupport().removePropertyChangeListener(CityControlPanelEditPersonTab.this);
					}
					updatePersonFormValues(listPeople.getSelectedValue());
					listPeople.getSelectedValue().getPropertyChangeSupport().addPropertyChangeListener(CityControlPanelEditPersonTab.this);
					listPeopleSelection = listPeople.getSelectedValue();
					toggleRoleEditorAvailability(false);
					setRoleFormBlank();
				} else {
					setPersonFormBlank();
				}
			}
		});
		listPeople.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPeople.setBackground(Color.WHITE);
		listPeople.setAlignmentY(Component.TOP_ALIGNMENT);
		listPeople.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPeople.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -1088518111443365535L;
			
			/**
			 * Allows Person objects to be displayed as custom text (in this case, their names) in the JList
			 */
			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Person) {
                    ((JLabel) renderer).setText(((Person) value).getName());
                }
                return renderer;
            }
        });
        scrollPeople.setViewportView(listPeople);
        
        // Create the state area
        panelState = new JPanel();
        panelState.setPreferredSize(new Dimension(300, 20));
        panelState.setMinimumSize(new Dimension(300, 20));
        panelState.setMaximumSize(new Dimension(300, 20));
        add(panelState);
        panelState.setLayout(new BoxLayout(panelState, BoxLayout.X_AXIS));
        
        labelState = new JLabel("State");
        labelState.setBorder(new EmptyBorder(0, 10, 0, 10));
        labelState.setPreferredSize(new Dimension(70, 16));
        labelState.setMinimumSize(new Dimension(70, 16));
        labelState.setMaximumSize(new Dimension(70, 16));
        panelState.add(labelState);
        
        labelStateValue = new JLabel("null");
        labelStateValue.setFont(getFont().deriveFont(Font.BOLD));
        panelState.add(labelStateValue);
        
        // Create the name control
        panelName = new JPanel();
        panelName.setMinimumSize(new Dimension(300, 30));
        panelName.setMaximumSize(new Dimension(300, 30));
        panelName.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panelName.setPreferredSize(new Dimension(300, 30));
        add(panelName);
        panelName.setLayout(new BoxLayout(panelName, BoxLayout.X_AXIS));
        
        labelName = new JLabel("Name");
        labelName.setPreferredSize(new Dimension(70, 14));
        labelName.setMinimumSize(new Dimension(70, 14));
        labelName.setMaximumSize(new Dimension(70, 14));
        labelName.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelName.add(labelName);
        
        textName = new JTextField();
        textName.setPreferredSize(new Dimension(200, 20));
        textName.setMinimumSize(new Dimension(200, 20));
        textName.setMaximumSize(new Dimension(200, 20));
        panelName.add(textName);
        textName.setColumns(10);
        
        // Create the cash control
        panelCash = new JPanel();
        panelCash.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panelCash.setPreferredSize(new Dimension(300, 30));
        panelCash.setMinimumSize(new Dimension(300, 30));
        panelCash.setMaximumSize(new Dimension(300, 30));
        add(panelCash);
        panelCash.setLayout(new BoxLayout(panelCash, BoxLayout.X_AXIS));
        
        labelCash = new JLabel("Cash");
        labelCash.setPreferredSize(new Dimension(70, 14));
        labelCash.setMinimumSize(new Dimension(70, 14));
        labelCash.setMaximumSize(new Dimension(70, 14));
        labelCash.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelCash.add(labelCash);
       
        textCash = new JFormattedTextField(currencyFormat);
        textCash.setPreferredSize(new Dimension(200, 20));
        textCash.setMinimumSize(new Dimension(200, 20));
        textCash.setMaximumSize(new Dimension(200, 20));
        textCash.setColumns(10);
        panelCash.add(textCash);
        
        // Create the revert/save controls
        panelControl = new JPanel();
        panelControl.setPreferredSize(new Dimension(300, 30));
        panelControl.setMinimumSize(new Dimension(300, 30));
        panelControl.setMaximumSize(new Dimension(300, 30));
        panelControl.setLayout(new BoxLayout(panelControl, BoxLayout.X_AXIS));
        add(panelControl);
        
        btnRevert = new JButton("Revert");
        btnRevert.setFocusable(false);
        btnRevert.addActionListener(new ActionListener() {
        	/**
        	 * When the revert button is clicked, this code loads the current data about the person
        	 */
        	public void actionPerformed(ActionEvent arg0) {
        		if (!listPeople.isSelectionEmpty())
        			updatePersonFormValues(listPeopleSelection);
        	}
        });
        btnRevert.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelControl.add(btnRevert);
        
        btnSave = new JButton("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new ActionListener() {
        	/**
        	 * When the save button is clicked, this code saves the changed data about the person
        	 */
        	public void actionPerformed(ActionEvent arg0) {
        		if (!listPeople.isSelectionEmpty())
        			saveChangedPerson(listPeopleSelection);
        	}
        });
        btnSave.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelControl.add(btnSave);
        
        // House link
		btnHouse = new JButton("See House");
		btnHouse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!listPeople.isSelectionEmpty())
        			Application.getMainFrame().CP.editBuildingsTab.displayBuilding(listPeopleSelection.getHome());
			}
		});
		btnHouse.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panelControl.add(btnHouse);
		
		// Work link
		btnWork = new JButton("See Work");
		btnWork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!listPeople.isSelectionEmpty())
					Application.getMainFrame().CP.editBuildingsTab.displayBuilding(listPeopleSelection.getOccupation().getWorkplace(BuildingInterface.class));
			}
		});
		btnWork.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panelControl.add(btnWork);
        
        // Add car button
        panelToggles = new JPanel();
        panelToggles.setPreferredSize(new Dimension(300, 40));
        panelToggles.setMinimumSize(new Dimension(300, 40));
        panelToggles.setMaximumSize(new Dimension(300, 40));
        panelToggles.setLayout(new BoxLayout(panelToggles, BoxLayout.X_AXIS));
        add(panelToggles);
        
        // Car toggle
        btnHasCar = new JButton(nocar);
        btnHasCar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if (btnHasCar.getIcon().equals(car)) {
        			btnHasCar.setIcon(nocar);
        		} else {
        			btnHasCar.setIcon(car);
        		}
        		if (!listPeople.isSelectionEmpty())
        			togglePersonCar(listPeopleSelection);
        	}
        });
        btnHasCar.setFocusable(false);
        btnHasCar.setMargin(new Insets(2, 2, 2, 2));
        btnHasCar.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnHasCar.setHorizontalAlignment(SwingConstants.LEFT);
        btnHasCar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHasCar.setPreferredSize(new Dimension(55, 40));
        btnHasCar.setMinimumSize(new Dimension(55, 40));
        btnHasCar.setMaximumSize(new Dimension(55, 40));
        panelToggles.add(btnHasCar);
        
        // House toggle
        btnHasHouse = new JButton(house);
        btnHasHouse.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if (btnHasHouse.getIcon().equals(house)) {
        			btnHasHouse.setIcon(apartment);
        		} else {
        			btnHasHouse.setIcon(house);
        		}
        		if (!listPeople.isSelectionEmpty())
        			togglePersonHome(listPeopleSelection);
        	}
        });
        btnHasHouse.setFocusable(false);
        btnHasHouse.setMargin(new Insets(2, 2, 2, 2));
        btnHasHouse.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnHasHouse.setHorizontalAlignment(SwingConstants.LEFT);
        btnHasHouse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHasHouse.setPreferredSize(new Dimension(55, 40));
        btnHasHouse.setMinimumSize(new Dimension(55, 40));
        btnHasHouse.setMaximumSize(new Dimension(55, 40));
        panelToggles.add(btnHasHouse);
        
        // Job toggle
        btnHasJob = new JButton(job);
        btnHasJob.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if (btnHasJob.getIcon().equals(job)) {
        			btnHasJob.setIcon(nojob);
        		} else {
        			btnHasJob.setIcon(job);
        		}
        		if (!listPeople.isSelectionEmpty())
        			togglePersonJob(listPeopleSelection);
        	}
        });
        btnHasJob.setFocusable(false);
        btnHasJob.setMargin(new Insets(2, 2, 2, 2));
        btnHasJob.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnHasJob.setHorizontalAlignment(SwingConstants.LEFT);
        btnHasJob.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHasJob.setPreferredSize(new Dimension(55, 40));
        btnHasJob.setMinimumSize(new Dimension(55, 40));
        btnHasJob.setMaximumSize(new Dimension(55, 40));
        panelToggles.add(btnHasJob);
        
        // Add roles panel
        panelRoles = new JPanel();
        panelRoles.setBorder(new EmptyBorder(5, 0, 0, 0));
        panelRoles.setLayout(new BorderLayout(0, 0));
        panelRoles.setPreferredSize(new Dimension(300, 85));
        panelRoles.setMinimumSize(new Dimension(300, 85));
        panelRoles.setMaximumSize(new Dimension(300, 85));
        add(panelRoles);
        
        // Add roles label
        labelRoles = new JLabel("Person's Roles");
        labelRoles.setBorder(new EmptyBorder(0, 10, 0, 10));
        labelRoles.setPreferredSize(new Dimension(140, 20));
        labelRoles.setMinimumSize(new Dimension(140, 20));
        labelRoles.setMaximumSize(new Dimension(140, 20));
        labelRoles.setFont(getFont().deriveFont(Font.BOLD));
        panelRoles.add(labelRoles, BorderLayout.NORTH);
        
        // Add roles scroll pane
        scrollRoles = new JScrollPane();
        scrollRoles.setBorder(new LineBorder(SystemColor.menu, 3));
        scrollRoles.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollRoles.setPreferredSize(new Dimension(300, 60));
        scrollRoles.setMinimumSize(new Dimension(300, 60));
        scrollRoles.setMaximumSize(new Dimension(300, 60));
        panelRoles.add(scrollRoles, BorderLayout.CENTER);
        
        // Add roles list
        listRoles = new JList<RoleInterface>(roleListModel);
        listRoles.addListSelectionListener(new ListSelectionListener() {
			/**
			 * When a Role is selected from the list, this is the code that kicks off the GUI update
			 * It also sets up a listener so that the GUI will update with changes for as long as that Role remains selected
			 */
			public void valueChanged(ListSelectionEvent arg0) {
				if (listRoles.getSelectedValue() != null) {
					if (listRolesSelection != null) {
						// Remove the listener from the previous selection
						listRolesSelection.getPropertyChangeSupport().removePropertyChangeListener(CityControlPanelEditPersonTab.this);
					}
					updateRoleFormValues(listRoles.getSelectedValue());
					listRoles.getSelectedValue().getPropertyChangeSupport().addPropertyChangeListener(CityControlPanelEditPersonTab.this);
					listRolesSelection = listRoles.getSelectedValue();
				} 
			}
		});
        listRoles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listRoles.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 3801676414916520494L;

			/**
			 * Allows RoleInterface objects to be displayed as custom text in the JList
			 */
			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof RoleInterface) {
                    ((JLabel) renderer).setText(((RoleInterface) value).getClass().getName());
                }
                return renderer;
            }
        });
        scrollRoles.setViewportView(listRoles);
        
        // Create role state area
        panelRoleState = new JPanel();
        panelRoleState.setLayout(new BoxLayout(panelRoleState, BoxLayout.X_AXIS));
        panelRoleState.setPreferredSize(new Dimension(300, 20));
        panelRoleState.setMinimumSize(new Dimension(300, 20));
        panelRoleState.setMaximumSize(new Dimension(300, 20));
        add(panelRoleState);
        
        labelRoleState = new JLabel("State");
        labelRoleState.setPreferredSize(new Dimension(100, 16));
        labelRoleState.setMinimumSize(new Dimension(100, 16));
        labelRoleState.setMaximumSize(new Dimension(100, 16));
        labelRoleState.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelRoleState.add(labelRoleState);
        
        labelRoleStateValue = new JLabel("null");
        labelRoleStateValue.setFont(getFont().deriveFont(Font.BOLD));
        panelRoleState.add(labelRoleStateValue);
        
        // Create role active area
        panelRoleActive = new JPanel();
        panelRoleActive.setLayout(new BoxLayout(panelRoleActive, BoxLayout.X_AXIS));
        panelRoleActive.setPreferredSize(new Dimension(300, 20));
        panelRoleActive.setMinimumSize(new Dimension(300, 20));
        panelRoleActive.setMaximumSize(new Dimension(300, 20));
        add(panelRoleActive);
        
        labelRoleActive = new JLabel("Is Active");
        labelRoleActive.setPreferredSize(new Dimension(100, 16));
        labelRoleActive.setMinimumSize(new Dimension(100, 16));
        labelRoleActive.setMaximumSize(new Dimension(100, 16));
        labelRoleActive.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelRoleActive.add(labelRoleActive);
        
        labelRoleActiveValue = new JLabel("null");
        labelRoleActiveValue.setFont(getFont().deriveFont(Font.BOLD));
        panelRoleActive.add(labelRoleActiveValue);
        
        // Create role activity area
        panelRoleActivity = new JPanel();
        panelRoleActivity.setLayout(new BoxLayout(panelRoleActivity, BoxLayout.X_AXIS));
        panelRoleActivity.setPreferredSize(new Dimension(300, 20));
        panelRoleActivity.setMinimumSize(new Dimension(300, 20));
        panelRoleActivity.setMaximumSize(new Dimension(300, 20));
        add(panelRoleActivity);
        
        labelRoleActivity = new JLabel("Has Activity");
        labelRoleActivity.setPreferredSize(new Dimension(100, 16));
        labelRoleActivity.setMinimumSize(new Dimension(100, 16));
        labelRoleActivity.setMaximumSize(new Dimension(100, 16));
        labelRoleActivity.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelRoleActivity.add(labelRoleActivity);
        
        labelRoleActivityValue = new JLabel("null");
        labelRoleActivityValue.setFont(getFont().deriveFont(Font.BOLD));
        panelRoleActivity.add(labelRoleActivityValue);
        
        // Create role salary area
        panelSalary = new JPanel();
        panelSalary.setSize(new Dimension(300, 20));
        panelSalary.setMinimumSize(new Dimension(300, 20));
        panelSalary.setMaximumSize(new Dimension(300, 20));
        add(panelSalary);
        panelSalary.setLayout(new BoxLayout(panelSalary, BoxLayout.X_AXIS));
        
        lblRoleSalary = new JLabel("Salary");
        lblRoleSalary.setPreferredSize(new Dimension(100, 16));
        lblRoleSalary.setMinimumSize(new Dimension(100, 16));
        lblRoleSalary.setMaximumSize(new Dimension(100, 16));
        lblRoleSalary.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelSalary.add(lblRoleSalary);
        
        labelRoleSalaryValue = new JLabel("null");
        labelRoleSalaryValue.setFont(getFont().deriveFont(Font.BOLD));
        panelSalary.add(labelRoleSalaryValue);
        
        // Create role shift editor
        panelShift = new JPanel();
        panelShift.setPreferredSize(new Dimension(300, 30));
        panelShift.setMinimumSize(new Dimension(300, 30));
        panelShift.setMaximumSize(new Dimension(300, 30));
        add(panelShift);
        panelShift.setLayout(new BoxLayout(panelShift, BoxLayout.X_AXIS));
        
        lblShift = new JLabel("Shift");
        lblShift.setPreferredSize(new Dimension(100, 16));
        lblShift.setMinimumSize(new Dimension(100, 16));
        lblShift.setMaximumSize(new Dimension(100, 16));
        lblShift.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelShift.add(lblShift);
        
        lblShiftStart = new JLabel("Start");
        lblShiftStart.setBorder(new EmptyBorder(0, 0, 0, 5));
        panelShift.add(lblShiftStart);
        
        spnShiftStart = new JSpinner();
        spnShiftStart.setPreferredSize(new Dimension(40, 20));
        spnShiftStart.setMinimumSize(new Dimension(40, 20));
        spnShiftStart.setMaximumSize(new Dimension(40, 20));
        panelShift.add(spnShiftStart);
        
        lblShiftEnd = new JLabel("End");
        lblShiftEnd.setBorder(new EmptyBorder(0, 15, 0, 5));
        panelShift.add(lblShiftEnd);
        
        spnShiftEnd = new JSpinner();
        spnShiftEnd.setPreferredSize(new Dimension(40, 20));
        spnShiftEnd.setMinimumSize(new Dimension(40, 20));
        spnShiftEnd.setMaximumSize(new Dimension(40, 20));
        panelShift.add(spnShiftEnd);
        
        panelRoleControl = new JPanel();
        panelRoleControl.setPreferredSize(new Dimension(300, 30));
        panelRoleControl.setMinimumSize(new Dimension(300, 30));
        panelRoleControl.setMaximumSize(new Dimension(300, 30));
        add(panelRoleControl);
        panelRoleControl.setLayout(new BoxLayout(panelRoleControl, BoxLayout.X_AXIS));
        
        btnRoleRevert = new JButton("Revert");
        btnRoleRevert.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		updateRoleFormValues(listRolesSelection);
        	}
        });
        btnRoleRevert.setFocusable(false);
        btnRoleRevert.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelRoleControl.add(btnRoleRevert);
        
        btnRoleSave = new JButton("Save");
        btnRoleSave.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		saveChangedRole(listRolesSelection);
        	}
        });
        btnRoleSave.setFocusable(false);
        panelRoleControl.add(btnRoleSave);
		
        // Disable the buttons until a list item is selected
        toggleButtonAvailability(false);
        // Disable the role editor until an occupation is selected
        toggleRoleEditorAvailability(false);
	}
	
	// Listeners
	
	/**
	 * This updates the list when people are added and removed
	 * This updates the GUI when the currently selected Person from the list is changed
	 */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    propertyChange(evt);
                }
            });
            return;
        }
        if (evt.getSource() == dataModel) {
        	// A person has been added or removed from the list
            if (DataModel.PEOPLE.equals(evt.getPropertyName())) {
                if (evt.getOldValue() != null && evt.getNewValue() == null) {
                    peopleListModel.removeElement(evt.getOldValue());
                } else if (evt.getOldValue() == null && evt.getNewValue() != null) {
                    peopleListModel.addElement((Person) evt.getNewValue());
                }
            }
        }
        if (evt.getSource() == listPeopleSelection) {
        	if (Person.STATE.equals(evt.getPropertyName())) {
        		updatePersonFormValues(listPeopleSelection);
        	}
        	if (Person.ROLES.equals(evt.getPropertyName())) {
                if (evt.getOldValue() != null && evt.getNewValue() == null) {
                    roleListModel.removeElement(evt.getOldValue());
                    if (listRolesSelection == evt.getOldValue()) {
                    	setRoleFormBlank();
                    }
                } else if (evt.getOldValue() == null && evt.getNewValue() != null) {
                    roleListModel.addElement((RoleInterface) evt.getNewValue());
                }
        	}
        }
    }

    // Getters
    
	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	// Utilities
	
	public void updatePersonFormValues(Person p) {
		labelStateValue.setText(p.getState().toString());
		textName.setText(p.getName());
		textCash.setValue(p.getCash());
		if (p.getCar() != null) {
			btnHasCar.setIcon(car);
		} else {
			btnHasCar.setIcon(nocar);
		}
		if (p.getHome() != null) {
			if (p.getHome().getClass().equals(HouseBuilding.class)) {
				btnHasHouse.setIcon(house);
			} else {
				btnHasHouse.setIcon(apartment);
			}
		} else {
			btnHasHouse.setIcon(house);
			btnHasHouse.setEnabled(false);
		}
		if (p.getOccupation() != null) {
			btnHasJob.setIcon(job);
		} else {
			btnHasJob.setIcon(nojob);
		}
		roleListModel.clear();
		for (RoleInterface r : p.getRoles()) {
			roleListModel.addElement(r);
		}
		toggleButtonAvailability(true);
	}
	
	public void updateRoleFormValues(RoleInterface r) {
		labelRoleStateValue.setText(r.getStateString());
		labelRoleActiveValue.setText(String.valueOf(r.getActive()));
		labelRoleActivityValue.setText(String.valueOf(r.getActivity()));
		if (r.getSalary() != -1 && r.getShiftStart() != -1 && r.getShiftEnd() != -1) {
			toggleRoleEditorAvailability(true);
			labelRoleSalaryValue.setText(currencyFormat.format(r.getSalary()));
			try {
				spnShiftStart.commitEdit();
				spnShiftEnd.commitEdit();
			} catch (ParseException e) {}
			spnShiftStart.setValue(r.getShiftStart());
			spnShiftEnd.setValue(r.getShiftEnd());
		} else {
			toggleRoleEditorAvailability(false);
		}
	}
	
	public void setPersonFormBlank() {
		labelStateValue.setText("null");
		textName.setText("");
		textCash.setText("");
		btnHasCar.setIcon(nocar);
		btnHasHouse.setIcon(house);
		btnHasJob.setIcon(nojob);
		roleListModel.clear();
		toggleButtonAvailability(false);
		setRoleFormBlank();
	}
	
	public void setRoleFormBlank() {
		labelRoleStateValue.setText("null");
		labelRoleActiveValue.setText("null");
		labelRoleActivityValue.setText("null");
		labelRoleSalaryValue.setText("null");
		toggleRoleEditorAvailability(false);
	}
	
	public void toggleButtonAvailability(boolean b) {
		btnRevert.setEnabled(b);
		btnSave.setEnabled(b);
		btnHasCar.setEnabled(b);
		btnHasHouse.setEnabled(false);
		btnHasJob.setEnabled(b);
		btnHouse.setEnabled(false);
		btnWork.setEnabled(false);
		if (b && listPeopleSelection != null) {
			if (listPeopleSelection.getHome() != null) {
				btnHouse.setEnabled(b);
				btnHasHouse.setEnabled(b);
			}
			if (listPeopleSelection.getOccupation() != null) {
				btnWork.setEnabled(b);
			}
		}
	}
	
	public void toggleRoleEditorAvailability(boolean b) {
		panelSalary.setVisible(b);
		panelShift.setVisible(b);
		panelRoleControl.setVisible(b);
	}
	
	public void saveChangedPerson(Person p) {
		p.setName(textName.getText());
		try {
			textCash.commitEdit();
		} catch (ParseException e) {}
		p.setCash(Integer.parseInt(textCash.getValue().toString()));
		listPeople.repaint();
	}
	
	public void saveChangedRole(RoleInterface r) {
		try {
			spnShiftStart.commitEdit();
			spnShiftEnd.commitEdit();
		} catch (ParseException e) {}
		r.setShift(Integer.parseInt(spnShiftStart.getValue().toString()), Integer.parseInt(spnShiftEnd.getValue().toString()));
	}
	
	public void togglePersonCar(Person p) {
		if (p.getCar() == null) {
			CarAgent c = new CarAgent(p.getHome(), p);
			CarAnimation ca = new CarAnimation(c, p.getHome());
			c.setAnimation(ca);
			Application.getMainFrame().cityView.addAnimation(ca);
		} else {
			p.getCar().stopThread();
			CarAnimation ca = (CarAnimation) p.getCar().getAnimation();
			Application.getMainFrame().cityView.removeAnimation(ca);
			p.setCar(null);
		}
	}
	
	public void togglePersonHome(Person p) {
		// TODO
		return;
	}
	
	public void togglePersonJob(Person p) {
		// TODO
		return;
	}
	
	public void displayPerson(Person p) {
		listPeople.setSelectedValue(p, true);
	}
}
