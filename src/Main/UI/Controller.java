package Main.UI;

import Main.objects.Entry;
import Main.objects.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Item currentItem = null;
    private Entry currentEntry = null;
    private boolean descriptionsAreBound = false;

    private ObservableList<Item> items = FXCollections.observableArrayList();

    // Context menu Stuff
    private ContextMenu listContextMenu = new ContextMenu();
    private ContextMenu entryContextMenu = new ContextMenu();

    private MenuItem deleteItem = new MenuItem("Delete");
    private MenuItem renameItem = new MenuItem("Rename");
    private MenuItem deleteEntry = new MenuItem("Delete");
    private MenuItem renameEntry = new MenuItem("Rename");
    private MenuItem redateEntry = new MenuItem("Redate");


    @FXML
    private ListView<Item> itemListView;

    @FXML
    private Button newEntry;

    @FXML
    private TextArea problemDescription;

    @FXML
    private TextArea fixDescription;

    @FXML
    private ListView<Entry> entryListView;

    @FXML
    private void newEntry(){
        if(currentItem != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New Entry");
            dialog.setHeaderText(null);
            dialog.setContentText("Please enter the item name.");
            dialog.initStyle(StageStyle.UTILITY);

            Optional<String> result = dialog.showAndWait();

            while(result.isPresent() && duplicateEntry(result.get())){
                dialog.setResult("");
                result = dialog.showAndWait();
            }

            if(result.isPresent() && !result.get().isEmpty()){
                Entry entry = new Entry(result.get());
                currentItem.getEntries().add(entry);
                entryListView.getSelectionModel().select(entry);
            }
        }
    }

    @FXML
    private void newItem(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Item");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the item name.");
        dialog.initStyle(StageStyle.UTILITY);

        Optional<String> result = dialog.showAndWait();

        while(result.isPresent() && duplicateItem(result.get())){
            dialog.setResult("");
            result = dialog.showAndWait();
        }

        if (result.isPresent() && !result.get().isEmpty()){
            Item i = new Item(result.get());
            items.add(i);
            itemListView.getSelectionModel().select(i);
        }
        items.sort(Comparator.comparing(Item::getName));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        itemListView.setItems(items);

        // Context Menu (Right click) stuff

        // Item ListView - ContextMenu
        deleteItem.setOnAction(event -> {
            if(deleteConfirmation()) {
                items.remove(currentItem);
                setCurrentItem(null);
            }
        });
        renameItem.setOnAction(event -> renameItem());

        listContextMenu.getItems().addAll(deleteItem, renameItem);
        itemListView.setOnContextMenuRequested(
                event -> listContextMenu.show(itemListView, event.getScreenX(), event.getScreenY()));

        // Entry ListView - ContextMenu
        renameEntry.setOnAction(event -> renameEntry());

        redateEntry.setOnAction(event -> redateEntry());

        deleteEntry.setOnAction( event -> {
            if(deleteConfirmation()) {
                currentItem.getEntries().remove(currentEntry);
                setCurrentEntry(null);
            }
        });

        entryContextMenu.getItems().addAll(renameEntry, redateEntry, deleteEntry);
        entryListView.setOnContextMenuRequested(
                event -> entryContextMenu.show(itemListView, event.getScreenX(), event.getScreenY()));

        // Current Entry & Item Listeners
        itemListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setCurrentItem(newValue));

        entryListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setCurrentEntry(newValue));
    }

    private boolean duplicateItem(String itemName){

        boolean isDuplicate = false;

        for(Item i : items){
            if(itemName.equals(i.getName())) {
                isDuplicate = true;
                break;
            }
        }

        if(isDuplicate) {
            Alert alert = new Alert(AlertType.INFORMATION);

            alert.setTitle("Delete Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("you already have an item named \"" + itemName + "\", please make a unique name.");

            alert.showAndWait();
        }

        return isDuplicate;
    }

    private boolean duplicateEntry(String entryName){

        boolean isDuplicate = false;

        for(Entry e : currentItem.getEntries()){
            if(entryName.equals(e.getName())) {
                isDuplicate = true;
                break;
            }
        }

        if(isDuplicate) {
            Alert alert = new Alert(AlertType.INFORMATION);

            alert.setTitle("Delete Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("you already have an entry named " + entryName + ", please make a unique name.");

            alert.showAndWait();
        }

        return isDuplicate;
    }

    private boolean deleteConfirmation(){
        Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void renameItem(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the new name.");
        dialog.initStyle(StageStyle.UTILITY);

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty())  currentItem.setName(result.get());
        itemListView.refresh();
    }

    private void renameEntry() {
        if (currentEntry != null) {

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Rename");
            dialog.setHeaderText(null);
            dialog.setContentText("Please enter the new name.");
            dialog.initStyle(StageStyle.UTILITY);

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent() && !result.get().isEmpty()) currentEntry.setName(result.get());
            entryListView.refresh();
        }
    }

    private void redateEntry(){
    if (currentEntry != null) {
            Dialog<ButtonType> dialog = new Dialog<>();

            // Dialog setup
            dialog.setTitle("Change Date");
            dialog.setHeaderText(null);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);

            // Layout and Controllers
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(10, 30, 10, 50));
            gridPane.setPrefSize(300, 50);

            DatePicker datePicker = new DatePicker();
            Label label = new Label("Please choose new date");
            label.setFont(new Font(16));
            gridPane.add(label, 1, 1);
            gridPane.add(datePicker, 1, 2);
            dialog.getDialogPane().setContent(gridPane);

            // Changing the date
            LocalDate startDate = currentEntry.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            datePicker.setValue(startDate);
            datePicker.valueProperty().addListener((observable, oldValue, newValue) -> currentEntry.setDate(newValue));

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get().equals(ButtonType.CANCEL))
                currentEntry.setDate(startDate);

            entryListView.refresh();
        }
    }

    private void setCurrentEntry(Entry newEntry){
        entryContextMenu.hide();
        if(newEntry != null){
            unbindDescriptions();
            currentEntry = newEntry;
            bindDescriptions();
            disableEntryContextMenuItems(false);
        }
        else{
            unbindDescriptions();
            currentEntry = null;
            disableEntryContextMenuItems(true);
        }
    }

    private void disableEntryContextMenuItems(Boolean set){
        fixDescription.setDisable(set);
        problemDescription.setDisable(set);
        renameEntry.setDisable(set);
        redateEntry.setDisable(set);
        deleteEntry.setDisable(set);
    }

    private void setCurrentItem(Item newItem){
        listContextMenu.hide();
        if(newItem != null) {
            unbindDescriptions();
            currentItem = newItem;
            entryListView.setItems(currentItem.getEntries());
            newEntry.setDisable(false);
            disableItemContextMenuItems(false);

            if(entryListView.getSelectionModel().isEmpty()) {
                currentEntry = null; }
            else {
                entryListView.getSelectionModel().selectFirst(); }
        }
        else{
            itemListView.getSelectionModel().clearSelection();
            entryListView.setItems(null);
            newEntry.setDisable(true);
            disableItemContextMenuItems(true);
        }
    }

    private void disableItemContextMenuItems(Boolean set){
        deleteItem.setDisable(set);
        renameItem.setDisable(set);
    }

    private void bindDescriptions(){
        if(!descriptionsAreBound) {
            fixDescription.textProperty().bindBidirectional(currentEntry.fixProperty());
            problemDescription.textProperty().bindBidirectional(currentEntry.problemProperty());
            descriptionsAreBound = true;
        }
    }

    private void unbindDescriptions(){
        if(descriptionsAreBound) {
            fixDescription.textProperty().unbindBidirectional(currentEntry.fixProperty());
            problemDescription.textProperty().unbindBidirectional(currentEntry.problemProperty());
            fixDescription.setText("");
            problemDescription.setText("");
            descriptionsAreBound = false;
        }
    }

    void setItemList(ArrayList<Item> initialItems) {
        if(!initialItems.isEmpty()) {
            items.addAll(initialItems);
            items.forEach(item -> item.refreshEntries());
            itemListView.getSelectionModel().selectFirst();
            if(!currentItem.getEntries().isEmpty())
                entryListView.getSelectionModel().selectFirst();
        }
    }

    ArrayList<Item> getSerializableItemList(){
        items.forEach(item -> item.refreshSerializableEntries());
        return new ArrayList<>(items);
    }
}