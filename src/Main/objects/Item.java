package Main.objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable{

    private StringProperty name = new SimpleStringProperty();
    transient private ObservableList<Entry> entries = FXCollections.observableArrayList();
    private ArrayList<Entry> serializableEntries;

    public Item(){}

    public Item (String name){
        this.name.setValue(name);
        serializableEntries = new ArrayList<Entry>();
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {this.name.set(name);}

    public ObservableList<Entry> getEntries() {return entries;}

    public ArrayList<Entry> getSerializableEntries() {return serializableEntries;}

    public void setSerializableEntries(ArrayList<Entry> serializableEntries) {
        this.serializableEntries = serializableEntries;
    }

    public void refreshSerializableEntries() {serializableEntries = new ArrayList<Entry>(entries);}

    public void refreshEntries() {entries = FXCollections.observableArrayList(serializableEntries);}

    @Override
    public String toString(){return getName();}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return getName().equals(item.getName());
    }
}
