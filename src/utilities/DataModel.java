package utilities;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import city.agents.interfaces.Person;
import city.bases.interfaces.BuildingInterface;

public class DataModel {
    public static final String PEOPLE = "people";
    public static final String BUILDINGS  = "buildings";

    private List<Person> people;
    private List<BuildingInterface> buildings;

    private PropertyChangeSupport propertyChangeSupport;

    public DataModel() {
        people = new ArrayList<Person>();
        buildings = new ArrayList<BuildingInterface>();
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    public List<Person> getPeople() {
        return people;
    }
    
    public List<BuildingInterface> getBuildings() {
        return buildings;
    }

    public void addPerson(Person p) {
        people.add(p);
        getPropertyChangeSupport().firePropertyChange(PEOPLE, null, p);
    }

    public void removePerson(Person p) {
        people.remove(p);
        getPropertyChangeSupport().firePropertyChange(PEOPLE, p, null);
    }
    
    public void addBuilding(BuildingInterface b) {
    	buildings.add(b);
    	getPropertyChangeSupport().firePropertyChange(BUILDINGS, null, b);
    }
    
    public void removeBuilding(BuildingInterface b) {
       	buildings.remove(b);
        getPropertyChangeSupport().firePropertyChange(BUILDINGS, b, null);
    }
}
