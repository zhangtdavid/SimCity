package city.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import utilities.DataModel;
import city.Application;
import city.Application.FOOD_ITEMS;
import city.agents.interfaces.Person;
import city.bases.Building;
import city.bases.ResidenceBuilding;
import city.bases.RestaurantBuilding;
import city.bases.interfaces.AnimationInterface;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.RoleInterface;
import city.buildings.BankBuilding;
import city.buildings.MarketBuilding;
import city.gui.MainFrame;

public class EditBuildingTab extends JPanel implements PropertyChangeListener, ActionListener{

	private static final long serialVersionUID = 9166425422374406573L;

	//============================================================================//
	// Data       
    //============================================================================//
	
	private final MainFrame mainFrame;
	
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(getDefaultLocale());
	
	private DataModel dataModel;
	private DefaultListModel<Building> buildingsListModel;
	private DefaultListModel<Person> roleListModel;
	private DefaultComboBoxModel<String> jobComboBoxModel;
	private DefaultComboBoxModel<BuildingInterface> workplaceComboBoxModel;
	private Building buildingSelectedFromList; // Needed so that we can deactivate old listeners
	private Person roleSelectedFromList;
	private String jobSelectedFromComboBox;
	
	private JScrollPane scrollBuildings;
	private JList<Building> listBuildings;
	private JPanel panelName;
	private JLabel labelName;
	private JTextField textName;
	private JPanel panelCash;
	private JLabel labelCash;
	private JFormattedTextField textCash;
	
	private JPanel panelControl;
	private JButton btnSave;
	private JPanel panelRoles;
	private JScrollPane scrollRoles;
	private JList<Person> listRoles;
	private JLabel labelRoles;
	// private JButton btnHasHouse; // TODO
	private JPanel panelRoleRevertSave;
	private JButton buttonRoleSave;
	private JPanel panelJob;
	private JComboBox<String> comboBoxJob;
	private JLabel labelJob;
	private JPanel panelWorkplace;
	private JLabel labelWorkplace;
	private JPanel panelAdd;
	private JButton buttonAdd;
	private JPanel panelNewJob;
	private JPanel panelEditJob;
	private JLabel label;
	private JPanel panel;
	private JButton giveFood;
	private JButton emptyFood;
	
	// private JButton btnSleep; // TODO
	private JPanel panelHouseData;
	private JLabel lblLivesIn;
	private JLabel lblResidence;
	
	//============================================================================//
	// Constructor        
    //============================================================================//
		
	public EditBuildingTab(MainFrame mf) {
		
		//--------------------------------------//
		// Start setup         
		//--------------------------------------//
		
		// Set up variables
		this.setVisible(true);
	    this.currencyFormat.setRoundingMode(RoundingMode.DOWN);
	    this.currencyFormat.setParseIntegerOnly(true);
	    this.mainFrame = mf;

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
		this.dataModel.getPropertyChangeSupport().addPropertyChangeListener(DataModel.BUILDINGS, this);
		this.dataModel.getPropertyChangeSupport().addPropertyChangeListener(DataModel.PEOPLE, this);
		
		// The ListModel is an object which stores what the JList displays
		this.buildingsListModel = new DefaultListModel<Building>();
		this.roleListModel = new DefaultListModel<Person>();
		
		// The ComboBoxModel is an object which stores what the ComboBox displays
		this.jobComboBoxModel = new DefaultComboBoxModel<String>();
		this.workplaceComboBoxModel = new DefaultComboBoxModel<BuildingInterface>();
		jobComboBoxModel.addElement("city.roles.BankManagerRole");
		jobComboBoxModel.addElement("city.roles.BankTellerRole");
		jobComboBoxModel.addElement("city.roles.MarketCashierRole");
		jobComboBoxModel.addElement("city.roles.MarketDeliveryBuildingRole");
		jobComboBoxModel.addElement("city.roles.MarketEmployeeRole");
		jobComboBoxModel.addElement("city.roles.MarketManagerRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChoiCashierRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChoiCookRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChoiHostRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChoiWaiterDirectRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChoiWaiterQueueRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChungCashierRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChungCookRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChungHostRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChungWaiterMessageCookRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChungWaiterRevolvingStandRole");
		jobComboBoxModel.addElement("city.roles.RestaurantJPCashierRole");
		jobComboBoxModel.addElement("city.roles.RestaurantJPCookRole");
		jobComboBoxModel.addElement("city.roles.RestaurantJPHostRole");
		jobComboBoxModel.addElement("city.roles.RestaurantJPWaiterRole");
		jobComboBoxModel.addElement("city.roles.RestaurantJPWaiterSharedDataRole");
		jobComboBoxModel.addElement("city.roles.RestaurantTimmsCashierRole");
		jobComboBoxModel.addElement("city.roles.RestaurantTimmsCookRole");
		jobComboBoxModel.addElement("city.roles.RestaurantTimmsHostRole");
		jobComboBoxModel.addElement("city.roles.RestaurantTimmsWaiterRole");
		jobComboBoxModel.addElement("city.roles.RestaurantZhangCashierRole");
		jobComboBoxModel.addElement("city.roles.RestaurantZhangCookRole");
		jobComboBoxModel.addElement("city.roles.RestaurantZhangHostRole");
		jobComboBoxModel.addElement("city.roles.RestaurantZhangWaiterRegularRole");
		jobComboBoxModel.addElement("city.roles.RestaurantZhangWaiterSharedDataRole");
		
		//--------------------------------------//
		// Building list         
		//--------------------------------------//
        
		// Scroll for the list
		scrollBuildings = new JScrollPane();
		scrollBuildings.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBuildings.setPreferredSize(new Dimension(300, 120));
		scrollBuildings.setMinimumSize(new Dimension(300, 120));
		scrollBuildings.setMaximumSize(new Dimension(300, 120));
		scrollBuildings.setBorder(new LineBorder(SystemColor.menu, 3));
		this.add(scrollBuildings);
		
        // Create the JList
		listBuildings = new JList<Building>(buildingsListModel);
		listBuildings.addListSelectionListener(new ListSelectionListener() {
			/**
			 * When a Building is selected from the list, this is the code that kicks off the GUI update
			 * It also sets up a listener so that the GUI will update with changes for as long as that Building remains selected
			 */
			public void valueChanged(ListSelectionEvent arg0) {
				if (listBuildings.getSelectedValue() != null) {
					if (buildingSelectedFromList != null) {
						// Remove the listener from the previous selection
						buildingSelectedFromList.getPropertyChangeSupport().removePropertyChangeListener(EditBuildingTab.this);
						mainFrame.personTracePanel.toggleAlertsWithOString(Integer.toString(buildingSelectedFromList.hashCode()), false);
						goToBuildingLocation(buildingSelectedFromList);
					}
					buildingSelectedFromList = listBuildings.getSelectedValue();
					buildingSelectedFromList.getPropertyChangeSupport().addPropertyChangeListener(EditBuildingTab.this);
					mainFrame.personTracePanel.toggleAlertsWithOString(Integer.toString(buildingSelectedFromList.hashCode()), true);
					updateBuildingValues();
				} else {
					setEditBuildingBlank();
				}
				toggleEditJob(false);
				toggleNewJob(false);
				setEditJobBlank();
				setNewJobBlank();
			}
		});
		listBuildings.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listBuildings.setBackground(Color.WHITE);
		listBuildings.setAlignmentY(Component.TOP_ALIGNMENT);
		listBuildings.setAlignmentX(Component.LEFT_ALIGNMENT);
        listBuildings.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -1088518111443365535L;
			
			/**
			 * Allows Building objects to be displayed as custom text (in this case, their names) in the JList
			 */
			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Building) {
                    ((JLabel) renderer).setText(((Building) value).getName());
                }
                return renderer;
            }
        });
        scrollBuildings.setViewportView(listBuildings);
        
        
		//--------------------------------------//
		// Building form        
		//--------------------------------------//
        
        // Create the name control
        panelName = new JPanel();
        panelName.setMinimumSize(new Dimension(300, 30));
        panelName.setMaximumSize(new Dimension(300, 30));
        panelName.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panelName.setPreferredSize(new Dimension(300, 30));
        panelName.setLayout(new BoxLayout(panelName, BoxLayout.X_AXIS));
        add(panelName);
        
        labelName = new JLabel("Name");
        labelName.setPreferredSize(new Dimension(70, 14));
        labelName.setMinimumSize(new Dimension(70, 14));
        labelName.setMaximumSize(new Dimension(70, 14));
        labelName.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelName.add(labelName);
        
        textName = new JTextField();
        textName.setEnabled(false);
        textName.setPreferredSize(new Dimension(200, 20));
        textName.setMinimumSize(new Dimension(200, 20));
        textName.setMaximumSize(new Dimension(200, 20));
        textName.setColumns(10);
        panelName.add(textName);
        
        // Create the cash control
        panelCash = new JPanel();
        panelCash.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panelCash.setPreferredSize(new Dimension(300, 30));
        panelCash.setMinimumSize(new Dimension(300, 30));
        panelCash.setMaximumSize(new Dimension(300, 30));
        panelCash.setLayout(new BoxLayout(panelCash, BoxLayout.X_AXIS));
        add(panelCash);
        
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
        
        panelHouseData = new JPanel();
        panelHouseData.setPreferredSize(new Dimension(300, 30));
        panelHouseData.setMinimumSize(new Dimension(300, 30));
        panelHouseData.setMaximumSize(new Dimension(300, 30));
        add(panelHouseData);
        panelHouseData.setLayout(new BoxLayout(panelHouseData, BoxLayout.X_AXIS));
        
        lblLivesIn = new JLabel("Type");
        lblLivesIn.setPreferredSize(new Dimension(70, 14));
        lblLivesIn.setMinimumSize(new Dimension(70, 14));
        lblLivesIn.setMaximumSize(new Dimension(70, 14));
        lblLivesIn.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelHouseData.add(lblLivesIn);
        
        lblResidence = new JLabel("-");
        lblResidence.setMinimumSize(new Dimension(200, 14));
        lblResidence.setMaximumSize(new Dimension(200, 14));
        lblResidence.setPreferredSize(new Dimension(200, 14));
        lblResidence.setFont(getFont().deriveFont(Font.BOLD));
        panelHouseData.add(lblResidence);
    
        //rchoi
        giveFood = new JButton("Give 20 of all food");
        giveFood.addActionListener(this);
        giveFood.setVisible(false);
        emptyFood = new JButton("Empty all food");
        emptyFood.addActionListener(this);
        emptyFood.setVisible(false);

        
        //
		//--------------------------------------//
		// Building buttons 1        
		//--------------------------------------//
        
        panelControl = new JPanel();
        panelControl.setPreferredSize(new Dimension(300, 30));
        panelControl.setMinimumSize(new Dimension(300, 30));
        panelControl.setMaximumSize(new Dimension(300, 30));
        panelControl.setLayout(new BoxLayout(panelControl, BoxLayout.X_AXIS));
        add(panelControl);
        
        // Save
        btnSave = new JButton("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		saveChangedBuilding();
        	}
        });
        btnSave.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelControl.add(btnSave);
        
		//--------------------------------------//
		// Person list      
		//--------------------------------------//
        
        // Add roles panel
        panelRoles = new JPanel();
        panelRoles.setBorder(new EmptyBorder(5, 0, 0, 0));
        panelRoles.setLayout(new BorderLayout(0, 0));
        panelRoles.setPreferredSize(new Dimension(300, 120));
        panelRoles.setMinimumSize(new Dimension(300, 120));
        panelRoles.setMaximumSize(new Dimension(300, 120));
        add(panelRoles);
        
        // Add roles label
        labelRoles = new JLabel("Building Occupants");
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
        scrollRoles.setPreferredSize(new Dimension(300, 100));
        scrollRoles.setMinimumSize(new Dimension(300, 100));
        scrollRoles.setMaximumSize(new Dimension(300, 100));
        panelRoles.add(scrollRoles, BorderLayout.CENTER);
        
        // Add roles list
        listRoles = new JList<Person>(roleListModel);
        listRoles.addListSelectionListener(new ListSelectionListener() {
			/**
			 * When a Role is selected from the list, this is the code that kicks off the GUI update
			 * It also sets up a listener so that the GUI will update with changes for as long as that Role remains selected
			 */
			public void valueChanged(ListSelectionEvent arg0) {
				if (listRoles.getSelectedValue() != null) {
					if (roleSelectedFromList != null) {
						// Remove the listener from the previous selection
						roleSelectedFromList.getPropertyChangeSupport().removePropertyChangeListener(EditBuildingTab.this);
					}
					roleSelectedFromList = listRoles.getSelectedValue();
					roleSelectedFromList.getPropertyChangeSupport().addPropertyChangeListener(EditBuildingTab.this);
					updateRoleValues();
					setEditJobBlank();
				}  else {
					toggleEditJob(false);
					setEditJobBlank();
				}
				toggleNewJob(false);
				setNewJobBlank();
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
                if (renderer instanceof JLabel && value instanceof Person) {
                    ((JLabel) renderer).setText(((Person)value).getName());
                }
                return renderer;
            }
        });
        scrollRoles.setViewportView(listRoles);
        
        
        
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
		// Edit role 
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        
        panelEditJob = new JPanel();
        panelEditJob.setLayout(new BoxLayout(panelEditJob, BoxLayout.Y_AXIS));
        add(panelEditJob);

        //
		//--------------------------------------//
		// Revert/Save
		//--------------------------------------//
        
        panelRoleRevertSave = new JPanel();
        panelRoleRevertSave.setPreferredSize(new Dimension(300, 30));
        panelRoleRevertSave.setMinimumSize(new Dimension(300, 30));
        panelRoleRevertSave.setMaximumSize(new Dimension(300, 30));
        panelRoleRevertSave.setLayout(new BoxLayout(panelRoleRevertSave, BoxLayout.X_AXIS));
        panelEditJob.add(panelRoleRevertSave);
        
        buttonRoleSave = new JButton("Save");
        buttonRoleSave.setFocusable(false);
        buttonRoleSave.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//saveChangedJob((JobRole) roleSelectedFromList);
        	}
        });
        panelRoleRevertSave.add(buttonRoleSave);
        
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
		// New role 
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        
        panelNewJob = new JPanel();
        panelNewJob.setPreferredSize(new Dimension(300, 145));
        panelNewJob.setMinimumSize(new Dimension(300, 145));
        panelNewJob.setMaximumSize(new Dimension(300, 145));
        panelNewJob.setLayout(new BoxLayout(panelNewJob, BoxLayout.Y_AXIS));
        add(panelNewJob);
        
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 0, 0, 0));
        panel.setPreferredSize(new Dimension(300, 25));
        panel.setMinimumSize(new Dimension(300, 25));
        panel.setMaximumSize(new Dimension(300, 25));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panelNewJob.add(panel);
        
        label = new JLabel("New Job");
        label.setPreferredSize(new Dimension(140, 20));
        label.setMinimumSize(new Dimension(140, 20));
        label.setMaximumSize(new Dimension(140, 20));
        label.setBorder(new EmptyBorder(0, 10, 0, 10));
        label.setFont(getFont().deriveFont(Font.BOLD));
        panel.add(label);
        
		//--------------------------------------//
		// Job
		//--------------------------------------//
        
        panelJob = new JPanel();
        panelJob.setPreferredSize(new Dimension(300, 30));
        panelJob.setMinimumSize(new Dimension(300, 30));
        panelJob.setMaximumSize(new Dimension(300, 30));
        panelJob.setLayout(new BoxLayout(panelJob, BoxLayout.X_AXIS));
        panelNewJob.add(panelJob);
        
        labelJob = new JLabel("Job");
        labelJob.setBorder(new EmptyBorder(0, 10, 0, 10));
        labelJob.setMaximumSize(new Dimension(100, 16));
        labelJob.setMinimumSize(new Dimension(100, 16));
        labelJob.setPreferredSize(new Dimension(100, 16));
        panelJob.add(labelJob);
        
        comboBoxJob = new JComboBox<String>(jobComboBoxModel);
        comboBoxJob.setFocusable(false);
        comboBoxJob.setPreferredSize(new Dimension(190, 20));
        comboBoxJob.setMinimumSize(new Dimension(190, 20));
        comboBoxJob.setMaximumSize(new Dimension(190, 20));
        comboBoxJob.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		jobSelectedFromComboBox = (String) comboBoxJob.getSelectedItem();
        		updateWorkplaceValues();
        	}
        });
        panelJob.add(comboBoxJob);
        
		//--------------------------------------//
		// Workplace
		//--------------------------------------//
        
        panelWorkplace = new JPanel();
        panelWorkplace.setPreferredSize(new Dimension(300, 30));
        panelWorkplace.setMaximumSize(new Dimension(300, 30));
        panelWorkplace.setMinimumSize(new Dimension(300, 30));
        panelWorkplace.setLayout(new BoxLayout(panelWorkplace, BoxLayout.X_AXIS));
        panelNewJob.add(panelWorkplace);
        
        labelWorkplace = new JLabel("Workplace");
        labelWorkplace.setBorder(new EmptyBorder(0, 10, 0, 10));
        labelWorkplace.setPreferredSize(new Dimension(100, 16));
        labelWorkplace.setMinimumSize(new Dimension(100, 16));
        labelWorkplace.setMaximumSize(new Dimension(100, 16));
        panelWorkplace.add(labelWorkplace);
        
		//--------------------------------------//
		// Add
		//--------------------------------------//
        
        panelAdd = new JPanel();
        panelAdd.setPreferredSize(new Dimension(300, 30));
        panelAdd.setMinimumSize(new Dimension(300, 30));
        panelAdd.setMaximumSize(new Dimension(300, 30));
        panelAdd.setLayout(new BoxLayout(panelAdd, BoxLayout.X_AXIS));
        panelNewJob.add(panelAdd);
        
        buttonAdd = new JButton("Add");
       
        buttonAdd.setFocusable(false);
        buttonAdd.setAlignmentX(1.0f);
        panelAdd.add(buttonAdd);
        
        
        //rchoi set food buttons
        add(giveFood);
        add(emptyFood);
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
		// Finish setup
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
		
        // Make sure that there is a building shown (if one exists) for whatever role is selected
        jobSelectedFromComboBox = (String) comboBoxJob.getSelectedItem();
        // Disable the buttons until a Building is selected
        toggleButtons(false);
        // Disable the job editor until a JobRole is selected
        toggleEditJob(false);
        // Disable the new job form
        toggleNewJob(false);
	}
	
	//============================================================================//
	// Listeners     
    //============================================================================//
	
	/**
	 * This updates the list when buildings are added and removed
	 * This updates the GUI when the currently selected Building from the list is changed
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
        	// A building has been added or removed from the list
            if (DataModel.BUILDINGS.equals(evt.getPropertyName())) {
                if (evt.getOldValue() != null && evt.getNewValue() == null) {
                    buildingsListModel.removeElement(evt.getOldValue());
                } else if (evt.getOldValue() == null && evt.getNewValue() != null) {
                    buildingsListModel.addElement((Building) evt.getNewValue());
                }

            	// Rather than updating the model directly, use this method.
            	// For some reason you can't iterate over the whole model. Since we need to show/hide
            	// buildings based on which role is selected, that is a problem.
                updateBuildingValues();
                updateWorkplaceValues();
            }
            // A building has been added or removed from the combo box list
            if (DataModel.PEOPLE.equals(evt.getPropertyName())) {
            }
        }
        if (Person.ROLES.equals(evt.getPropertyName())) {
            if (evt.getOldValue() != null && evt.getNewValue() == null) {
                roleListModel.removeElement(evt.getOldValue());
                if (roleSelectedFromList == evt.getOldValue()) {
                	toggleEditJob(false);
                	setEditJobBlank();
                }
            } else if (evt.getOldValue() == null && evt.getNewValue() != null) {
            	if(!roleListModel.contains(((RoleInterface) evt.getNewValue()).getPerson())) // no dupe adding
                	roleListModel.addElement(((RoleInterface) evt.getNewValue()).getPerson());
            }
    	}
    }
    
	//============================================================================//
	// Messages   
    //============================================================================//
    
	public void displayBuilding(BuildingInterface buildingInterface) {
		listBuildings.setSelectedValue(buildingInterface, true);
		goToBuilding(buildingInterface);
	}
    
	//============================================================================//
	// Actions   
    //============================================================================//
	
	//--------------------------------------//
	// Saves
	//--------------------------------------//
	
	/**
	 * When the save button is clicked, this code saves the changed data about the building
	 */
	private void saveChangedBuilding() {
		try {
			textCash.commitEdit();
		} catch (ParseException e) {}
		buildingSelectedFromList.setCash(Integer.parseInt(textCash.getValue().toString()));
		listBuildings.repaint();
	}
	
	//============================================================================//
	// Utilities    
    //============================================================================//
	
	//--------------------------------------//
	// Update forms
	//--------------------------------------//
    
	/**
	 * When a new Building is selected (or the selected building changes) this code updates the data
	 * When the revert button is clicked, this code loads the current data about the building
	 */
	private void updateBuildingValues() {
		Building p = buildingSelectedFromList;
		if(p != null){
			textName.setText(p.getName());
			textCash.setValue(p.getCash());
			if(p instanceof BankBuilding || p instanceof MarketBuilding || p instanceof RestaurantBuilding){
				textCash.setEnabled(true);
			}else{
				textCash.setEnabled(false);
			}
			String string = p.getClass().toString();
			String st = string.split(" ")[1];
			String s[] = st.split(".buildings.",0);
			lblResidence.setText(s[1]);
			if(p instanceof RestaurantBuilding || p instanceof ResidenceBuilding || p instanceof MarketBuilding){
				giveFood.setVisible(true);
				emptyFood.setVisible(true);
			}else{
				giveFood.setVisible(false);
				emptyFood.setVisible(false);
			}
			roleListModel.clear();
			if(p instanceof ResidenceBuilding){
				
				for(int i = 0; i < ((ResidenceBuilding)p).getResidents().size(); i++)
					roleListModel.addElement(((ResidenceBuilding)p).getResidents().get(i).getPerson());
				
			}
			for (Entry<RoleInterface, AnimationInterface> entry : p.getOccupyingRoles().entrySet()){
				roleListModel.addElement(entry.getKey().getPerson()); // go through all the residentroles with anims and go~
			}
		}
		toggleButtons(true);
	}
	
	private void updateRoleValues() {
		// Person r = roleSelectedFromList;
		return;
	}
	
	private void updateWorkplaceValues() {
		workplaceComboBoxModel.removeAllElements();
		if (jobSelectedFromComboBox != null) {
			for (BuildingInterface b : Application.CityMap.getBuildings()) {
				if (b.getWorkerRoleClassNames().contains(jobSelectedFromComboBox)) {
					workplaceComboBoxModel.addElement(b);
				}
			}
		}
	}
	
	//--------------------------------------//
	// Blank forms
	//--------------------------------------//
	
	private void setEditBuildingBlank() {
		textName.setText("");
		textCash.setText("");
		lblResidence.setText("-");
		roleListModel.clear();
		toggleButtons(false);
		setEditJobBlank();
	}
	
	private void setEditJobBlank() {
		toggleEditJob(false);
	}
	
	private void setNewJobBlank() {
		comboBoxJob.setSelectedIndex(0);
	}
	
	//--------------------------------------//
	// Toggle views
	//--------------------------------------//
	
	private void toggleButtons(boolean b) {
		btnSave.setEnabled(b);
	}
	
	private void toggleEditJob(boolean b) {
		panelEditJob.setVisible(b);
	}
	
	private void toggleNewJob(boolean b) {
		updateWorkplaceValues();
		panelNewJob.setVisible(b);
	}
	
	private void goToBuildingLocation(Building p){
			Application.getMainFrame().buildingView.setView(p.getCityViewBuilding().getID()); // visual go-to
			p.getPanel().repaint(); // equivalent to ActionPerformed for building panels
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(giveFood)){ // give 20 of each food to the building.
			System.out.println("=================================");
			System.out.println("OVERRIDE: The food supply is 20 of everything! FOR " + buildingSelectedFromList.getName());
			System.out.println("=================================");
			HashMap<FOOD_ITEMS, Integer> temp = new HashMap<FOOD_ITEMS, Integer>();
			temp.put(FOOD_ITEMS.chicken, 20);
			temp.put(FOOD_ITEMS.salad, 20);
			temp.put(FOOD_ITEMS.pizza, 20);
			temp.put(FOOD_ITEMS.steak, 20);
			Building b = buildingSelectedFromList;
			if(b instanceof ResidenceBuilding){ // give all residents food.
				for(int i = 0; i < ((ResidenceBuilding)b).getResidents().size(); i++)
					((ResidenceBuilding)b).setFood(((ResidenceBuilding)b).getResidents().get(i).getPerson(), temp);
				
			}else if (b instanceof RestaurantBuilding){
				((RestaurantBuilding)b).setFoodQuantity(FOOD_ITEMS.chicken, 20);
				((RestaurantBuilding)b).setFoodQuantity(FOOD_ITEMS.pizza, 20);
				((RestaurantBuilding)b).setFoodQuantity(FOOD_ITEMS.salad, 20);
				((RestaurantBuilding)b).setFoodQuantity(FOOD_ITEMS.steak, 20);
				
			}else if (b instanceof MarketBuilding){
				((MarketBuilding)b).setInventory(temp);			
			}
			
		}else if(e.getSource().equals(emptyFood)){
			System.out.println("=================================");
			System.out.println("OVERRIDE: The food supply is 0 of everything! FOR " + buildingSelectedFromList.getName());
			System.out.println("=================================");
			Building b = buildingSelectedFromList;
			if(b instanceof ResidenceBuilding){
				for(int i = 0; i < ((ResidenceBuilding)b).getResidents().size(); i++){
					HashMap<FOOD_ITEMS, Integer> temp1 = ((ResidenceBuilding)b).getFoodItems(((ResidenceBuilding)b).getResidents().get(i).getPerson());
					temp1.put(FOOD_ITEMS.chicken, 0);
					temp1.put(FOOD_ITEMS.salad, 0);
					temp1.put(FOOD_ITEMS.pizza, 0);
					temp1.put(FOOD_ITEMS.steak, 0);
					((ResidenceBuilding)b).setFood(((ResidenceBuilding)b).getResidents().get(i).getPerson(), temp1);
				}
			}else if (b instanceof RestaurantBuilding){
				((RestaurantBuilding)b).setFoodQuantity(FOOD_ITEMS.chicken, 0);
				((RestaurantBuilding)b).setFoodQuantity(FOOD_ITEMS.pizza, 0);
				((RestaurantBuilding)b).setFoodQuantity(FOOD_ITEMS.salad, 0);
				((RestaurantBuilding)b).setFoodQuantity(FOOD_ITEMS.steak, 0);
			}else if (b instanceof MarketBuilding){
				HashMap<FOOD_ITEMS, Integer> temp = new HashMap<FOOD_ITEMS, Integer>();
				temp.put(FOOD_ITEMS.chicken, 0);
				temp.put(FOOD_ITEMS.salad, 0);
				temp.put(FOOD_ITEMS.pizza, 0);
				temp.put(FOOD_ITEMS.steak, 0);
				((MarketBuilding)b).setInventory(temp);
			}			
			
			
		}
	}
	public void goToBuilding(BuildingInterface b){
		Application.getMainFrame().buildingView.setView(b.getCityViewBuilding().getID()); // visual go-to
		b.getPanel().repaint(); // equivalent to ActionPerformed for building panels
	}
}
