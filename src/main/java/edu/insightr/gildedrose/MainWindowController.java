package edu.insightr.gildedrose;

//import javafx.base.javafx.collections.FXCollections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

public class MainWindowController implements Initializable {

    private Inventory inv;

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
    TextField nameTF;
    @FXML
    TextField sellinTF;
    @FXML
    TextField qualityTF;
    @FXML
    PieChart pieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemsListView.getSelectionModel().selectedItemProperty()
                .addListener(e -> displayItemDetails(itemsListView.getSelectionModel().getSelectedItem().toString()));
        inv = new Inventory();
        fetchItems();

    }

    private void displayItemDetails(String name) {
        try {
            Item[] items = inv.getItems();
            Item item = new Item() {
                @Override
                public void accept(IVisitor aVisitor) {

                }
            };
            for (Item i: items) {
                if(i.getName().equals(name)){
                    item.setName(name);
                    item.setSellIn(i.getSellIn());
                    item.setQuality(i.getQuality());
                    }
                }
            nameTF.setText(item.getName());
            sellinTF.setText(Integer.toString(item.getSellIn()));
            qualityTF.setText(Integer.toString(item.getQuality()));
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

    public PieChart.Data fetchPieChartItem(ObservableList<Item> items ,String name, Function<Item,Boolean> func){
        PieChart.Data itemType = new PieChart.Data(name,0);
        int a=0;
        for(Item i : items){
            if(func.apply(i)){
                a=1;
            }
            else{
                a=0;
            }
            itemType.setPieValue(itemType.getPieValue()+a);
        }
        return itemType;
    }

    public void onDelete() {
        Item itemToDelete = inv.fetchItemByName(nameTF.getText());
        inv.deleteItem(itemToDelete);
        fetchItems();
    }
}
