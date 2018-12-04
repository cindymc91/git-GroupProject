package edu.insightr.gildedrose;

//import javafx.base.javafx.collections.FXCollections;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    Inventory inv;

    @FXML
    ListView itemsListView;
    @FXML
    Button addItemButton;
    @FXML
    Button deleteButton;
    @FXML
    Button updateButton;
    @FXML
    Button editButton;
    @FXML
    TextField nameTF;
    @FXML
    TextField sellinTF;
    @FXML
    TextField qualityTF;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inv = new Inventory();
        fetchItems();
    }

    public void fetchItems(){
        ObservableList<Item> itemslist = FXCollections.observableArrayList(inv.getItems());
        itemsListView.setItems(itemslist);
    }
}
