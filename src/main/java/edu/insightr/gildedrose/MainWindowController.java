package edu.insightr.gildedrose;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.function.Function;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;

import static java.lang.Math.toIntExact;

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
    @FXML
    Button jsonButton;
    @FXML
    Button cancelButton;

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
            saveButton.setDisable(true);
            nameTF.setDisable(true);
            sellinTF.setDisable(true);
            qualityTF.setDisable(true);
            typeComboBox.setDisable(true);
            deleteButton.setDisable(false);
            editButton.setDisable(false);
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

        //To avoid errors with the pie chart, we create a new one, pieChartDataOk, with all elements
        //that are different from 0
        ObservableList<PieChart.Data> pieChartDataOk = FXCollections.observableArrayList();
        for (PieChart.Data p : pieChartData)
        {
            if(p.getPieValue()!=0)
            {
                pieChartDataOk.add(p);
            }
        }
        pieChart.setData(pieChartDataOk);
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
        saveButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        allBlank();
    }

    public void onEdit(){
        saveButton.setDisable(false);
        nameTF.setDisable(false);
        sellinTF.setDisable(false);
        qualityTF.setDisable(false);
        typeComboBox.setDisable(false);
        addMode = false;

        itemNameToEdit = nameTF.getText();
        cancelButton.setDisable(false);
    }

    public void onAdd(){
        addMode=true;
        allBlank();
        saveButton.setDisable(false);
        nameTF.setDisable(false);
        sellinTF.setDisable(false);
        qualityTF.setDisable(false);
        typeComboBox.setDisable(false);
        typeComboBox.getSelectionModel().select(-1);;
        cancelButton.setDisable(false);
    }

    public void onSave(){
        String name = nameTF.getText();
        String type = (String) typeComboBox.getValue();
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
        saveButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void onUpdate(){
        inv.updateSellin();
        inv.updateQuality();
        fetchItems();
    }

    public void onCancel(){
        nameTF.setDisable(true);
        sellinTF.setDisable(true);
        qualityTF.setDisable(true);
        typeComboBox.setDisable(true);
        saveButton.setDisable(true);
        editButton.setDisable(true);
        cancelButton.setDisable(true);

        if(addMode==true)
        {
            allBlank();
        }
        else
        {
            fetchItems();
        }
    }

    public void onLoad(){
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);

        File file = null;
        if(returnVal == JFileChooser.APPROVE_OPTION){
            file = fc.getSelectedFile();
            System.out.println("Opening " + file.getName());
        }

        JSONParser parser = new JSONParser();

        try{
            Object obj = parser.parse(new FileReader(file));

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray itemsList = (JSONArray) jsonObject.get("items");

            Iterator<JSONObject> iterator = itemsList.iterator();
            while (iterator.hasNext()) {
                JSONObject tempObj = iterator.next();
                String name = (String) tempObj.get("name");
                String type = (String) tempObj.get("type");
                int quality = toIntExact((long)tempObj.get("quality"));
                int sellin = toIntExact((long)tempObj.get("sellIn"));

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
        }catch (Exception e){
            e.printStackTrace();
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
