package edu.insightr.gildedrose;

//import javafx.base.javafx.collections.FXCollections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

public class MainWindowController implements Initializable {

    private Inventory inv;
    private boolean addMode;
    private String itemNameToEdit;

    @FXML
    ListView<Item> itemsListView;
    @FXML
    Button addItemButton;
    @FXML
    Button deleteButton;
    @FXML
    Button updateButton;
    @FXML
    Button editButton;
    @FXML
    Button saveButton;
    @FXML
    TextField nameTF;
    @FXML
    TextField sellinTF;
    @FXML
    TextField qualityTF;
    @FXML
    PieChart pieChart;
    @FXML
    ComboBox typeComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemsListView.getSelectionModel().selectedItemProperty()
                .addListener(e -> displayItemDetails(itemsListView.getSelectionModel().getSelectedItem()));
        inv = new Inventory();
        addMode = false;
        itemNameToEdit = null;
        fetchItems();
    }

    private void displayItemDetails(Item item) {
        try {
            addMode=false;
            nameTF.setText(item.getName());
            sellinTF.setText(Integer.toString(item.getSellIn()));
            qualityTF.setText(Integer.toString(item.getQuality()));
            typeComboBox.setValue(item.getClass().getSimpleName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void fetchItems() {
        ObservableList<Item> itemslist = FXCollections.observableArrayList(inv.getItems());
        itemsListView.setItems(itemslist);


        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        fetchPieChartItem(itemslist, "AgedBrie", (item) -> item instanceof AgedBrie),
                        fetchPieChartItem(itemslist, "Backstage", (item) -> item instanceof Backstage),
                        fetchPieChartItem(itemslist, "Conjured", (item) -> item instanceof Conjured),
                        fetchPieChartItem(itemslist, "Sulfuras", (item) -> item instanceof Sulfuras),
                        fetchPieChartItem(itemslist, "Elixir", (item) -> item instanceof Elixir),
                        fetchPieChartItem(itemslist, "Vest", (item) -> item instanceof Vest)

                );
        pieChart.setData(pieChartData);
        pieChart.setStartAngle(90);
    }

    public PieChart.Data fetchPieChartItem(ObservableList<Item> items, String name, Function<Item, Boolean> func) {
        PieChart.Data itemType = new PieChart.Data(name, 0);
        int a = 0;
        for (Item i : items) {
            if (func.apply(i)) {
                a = 1;
            } else {
                a = 0;
            }
            itemType.setPieValue(itemType.getPieValue() + a);
        }
        return itemType;
    }

    public void onDelete() {
        Item itemToDelete = inv.fetchItemByName(nameTF.getText());
        inv.deleteItem(itemToDelete);
        fetchItems();
        allBlank();
    }

    public void onEdit(){
        editButton.setDisable(false);
        saveButton.setDisable(false);
        nameTF.setEditable(true);
        sellinTF.setEditable(true);
        qualityTF.setEditable(true);
        addMode = false;

        itemNameToEdit = nameTF.getText();
    }

    public void onAdd(){
        addMode=true;
        allBlank();
    }

    public void onSave(){
        System.out.println(itemNameToEdit);
        String name = nameTF.getText();
        System.out.println(name);
        String type = (String) typeComboBox.getValue();
        System.out.println(type);
        int sellin = 0;
        int quality = 0;
        try
        {
            sellin = Integer.parseInt(sellinTF.getText());
        } catch(Exception e){sellin = 0;}
        try
        {
            quality = Integer.parseInt(qualityTF.getText());
        }catch (Exception e){quality =0;}

        System.out.println(sellin);
        System.out.println(quality);
        if(addMode == true)
        {
            switch(type)
            {
                case "AgedBrie":
                    AgedBrie agedBrie = new AgedBrie(name,sellin,quality);
                    inv.addItem(agedBrie);
                    break;
                case "Backstage":
                    Backstage backstage = new Backstage(name,sellin,quality);
                    inv.addItem(backstage);
                    break;
                case "Conjured":
                    Conjured conjured = new Conjured(name,sellin,quality);
                    inv.addItem(conjured);
                    break;
                case "Elixir":
                    Elixir elixir = new Elixir(name,sellin,quality);
                    inv.addItem(elixir);
                    break;
                case "Sulfuras":
                    Sulfuras sulfuras = new Sulfuras(name,sellin,quality);
                    inv.addItem(sulfuras);
                    break;
                case "Vest":
                    Vest vest = new Vest(name,sellin,quality);
                    inv.addItem(vest);
                    break;
            }
        }
        else
        {
            switch(type)
            {
                case "AgedBrie":
                    AgedBrie ab = new AgedBrie(name,sellin,quality);
                    inv.editItem(itemNameToEdit,ab);
                    System.out.println(ab.toString());
                    break;
                case "Backstage":
                    Backstage bs = new Backstage(name,sellin,quality);
                    inv.editItem(itemNameToEdit,bs);
                    break;
                case "Conjured":
                    Conjured conjured = new Conjured(name,sellin,quality);
                    inv.editItem(itemNameToEdit,conjured);
                    break;
                case "Elixir":
                    Elixir elix = new Elixir(name,sellin,quality);
                    inv.editItem(itemNameToEdit,elix);
                    break;
                case "Sulfuras":
                    Sulfuras sulf = new Sulfuras(name,sellin,quality);
                    inv.editItem(itemNameToEdit,sulf);
                    break;
                case "Vest":
                    Vest vest = new Vest(name,sellin,quality);
                    inv.editItem(itemNameToEdit,vest);
                    break;
            }
            //Item newItem = new Item(name,sellin,quality);

        }
        fetchItems();
    }

    public void allBlank(){
        nameTF.setText("");
        sellinTF.setText("");
        qualityTF.setText("");
        typeComboBox.setValue(null);
    }
}
