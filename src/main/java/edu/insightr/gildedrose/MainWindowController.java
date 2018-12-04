package edu.insightr.gildedrose;

//import javafx.base.javafx.collections.FXCollections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class MainWindowController {
    @FXML
    ListView itemsListView;
    @FXML
    Button addItemButton;
    @FXML
    Button deleteButton;
    @FXML
    Button updateButton;

    public void fetchItems(){
        Inventory inv = new Inventory();
        ObservableList<Item> itemslist = FXCollections.observableArrayList(inv.getItems());
        itemsListView.setItems(itemslist);
    }
}
