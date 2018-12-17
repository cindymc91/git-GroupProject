package edu.insightr.gildedrose.Controller;

import edu.insightr.gildedrose.Model.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;

import static java.lang.Math.toIntExact;
import static javafx.application.Application.launch;

public class MainWindowController extends Application implements Initializable{

    private Inventory inv;
    private boolean addMode;
    private AuditTrail auditTrail;

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
    @FXML
    StackedBarChart barChartSellIn;
    @FXML
    Label idNumberLabel;
    @FXML
    StackedBarChart barChartCreationDate;
    @FXML
    DatePicker creationDateDatePicker;
    @FXML
    ListView<Item> listeAchats;
    @FXML
    ListView<Item> listeVentes;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("mainwindow.fxml"));
        primaryStage.setTitle("Main Window");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemsListView.getSelectionModel().selectedItemProperty()
                .addListener(e -> displayItemDetails(itemsListView.getSelectionModel().getSelectedItem()));

        listeAchats.getSelectionModel().selectedItemProperty()
                .addListener(e -> displayItemDetails(listeAchats.getSelectionModel().getSelectedItem()));

        listeVentes.getSelectionModel().selectedItemProperty()
                .addListener(e -> displayItemDetails(listeVentes.getSelectionModel().getSelectedItem()));

        try {
            inv = new Inventory();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        addMode = false;
        auditTrail = new AuditTrail();

        for (Item i : inv.getItems()) {
            auditTrail.addToAchats(i);
        }

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
            addMode = false;
            idNumberLabel.setText(Integer.toString(item.getId()));
            nameTF.setText(item.getName());
            sellinTF.setText(Integer.toString(item.getSellIn()));
            qualityTF.setText(Integer.toString(item.getQuality()));
            typeComboBox.setValue(item.getClass().getSimpleName());
            creationDateDatePicker.setValue(item.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void fetchItems() {
        ObservableList<Item> itemslist = FXCollections.observableArrayList(inv.getItems());
        itemsListView.setItems(itemslist);

        listeAchats.setItems(FXCollections.observableArrayList(auditTrail.getAchats()));
        listeVentes.setItems(FXCollections.observableArrayList(auditTrail.getVentes()));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        //count of each Item
        int nbAgedBrie = countItem(itemslist, (item) -> item instanceof AgedBrie);
        int nbBackstage = countItem(itemslist, (item) -> item instanceof Backstage);
        int nbConjured = countItem(itemslist, (item) -> item instanceof Conjured);
        int nbSulfuras = countItem(itemslist, (item) -> item instanceof Sulfuras);
        int nbElixir = countItem(itemslist, (item) -> item instanceof Elixir);
        int nbVest = countItem(itemslist, (item) -> item instanceof Vest);


        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        //PieChart
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        createPieChartItem("AgedBrie", nbAgedBrie),
                        createPieChartItem("Backstage", nbBackstage),
                        createPieChartItem("Conjured", nbConjured),
                        createPieChartItem("Elixir", nbElixir),
                        createPieChartItem("Sulfuras", nbSulfuras),
                        createPieChartItem("Vest", nbVest)
                );

        //To avoid errors with the pie chart, we create a new one, pieChartDataOk, with all elements
        //that are different from 0
        ObservableList<PieChart.Data> pieChartDataOk = FXCollections.observableArrayList();
        for (PieChart.Data p : pieChartData) {
            if (p.getPieValue() != 0) {
                pieChartDataOk.add(p);
            }
        }
        pieChart.setData(pieChartDataOk);
        pieChart.setStartAngle(90);


        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        //BarChart sellin

        int maxSellIn = checkMaxSellIn(itemslist);
        barChartSellIn.getXAxis().setLabel("Sell In");
        barChartSellIn.getYAxis().setAutoRanging(false);
        NumberAxis tmp = (NumberAxis) barChartSellIn.getYAxis();
        tmp.setLowerBound(0);
        tmp.setUpperBound(_countItemBySellIn(itemslist, checkNumberMaxItemBySellin(itemslist, maxSellIn)) + 2);
        barChartSellIn.getYAxis().setLabel("Quantity");

        XYChart.Series seriesAgedBrie = new XYChart.Series();
        XYChart.Series seriesBackstage = new XYChart.Series();
        XYChart.Series seriesConjured = new XYChart.Series();
        XYChart.Series seriesElixir = new XYChart.Series();
        XYChart.Series seriesSulfuras = new XYChart.Series();
        XYChart.Series seriesVest = new XYChart.Series();

        barChartSellIn.getData().clear();

        seriesAgedBrie.setName("Aged Brie");
        seriesBackstage.setName("Backstage");
        seriesConjured.setName("Conjured");
        seriesElixir.setName("Elixir");
        seriesSulfuras.setName("Sulfuras");
        seriesVest.setName("Vest");

        int numberAb = 0;
        int numberBs = 0;
        int numberC = 0;
        int numberE = 0;
        int numberS = 0;
        int numberV = 0;

        for (int i = -10; i <= maxSellIn; i++) {
            if (atLeastOne(itemslist, i)) {
                String barName = String.valueOf(i);
                seriesAgedBrie.getData().add(new XYChart.Data(barName, countItemBySellIn(itemslist, i, (item) -> item instanceof AgedBrie)));
                seriesBackstage.getData().add(new XYChart.Data(barName, countItemBySellIn(itemslist, i, (item) -> item instanceof Backstage)));
                seriesConjured.getData().add(new XYChart.Data(barName, countItemBySellIn(itemslist, i, (item) -> item instanceof Conjured)));
                seriesElixir.getData().add(new XYChart.Data(barName, countItemBySellIn(itemslist, i, (item) -> item instanceof Elixir)));
                seriesSulfuras.getData().add(new XYChart.Data(barName, countItemBySellIn(itemslist, i, (item) -> item instanceof Sulfuras)));
                seriesVest.getData().add(new XYChart.Data(barName, countItemBySellIn(itemslist, i, (item) -> item instanceof Vest)));

                numberAb += countItemBySellIn(itemslist, i, (item) -> item instanceof AgedBrie);
                numberBs += countItemBySellIn(itemslist, i, (item) -> item instanceof Backstage);
                numberC += countItemBySellIn(itemslist, i, (item) -> item instanceof Conjured);
                numberE += countItemBySellIn(itemslist, i, (item) -> item instanceof Elixir);
                numberS += countItemBySellIn(itemslist, i, (item) -> item instanceof Sulfuras);
                numberV += countItemBySellIn(itemslist, i, (item) -> item instanceof Vest);
            }
        }
        if (numberAb != 0)
            barChartSellIn.getData().add(seriesAgedBrie);
        if (numberBs != 0)
            barChartSellIn.getData().add(seriesBackstage);
        if (numberC != 0)
            barChartSellIn.getData().add(seriesConjured);
        if (numberE != 0)
            barChartSellIn.getData().add(seriesElixir);
        if (numberS != 0)
            barChartSellIn.getData().add(seriesSulfuras);
        if (numberV != 0)
            barChartSellIn.getData().add(seriesVest);

        //barChartSellIn.getData().addAll(seriesAgedBrie, seriesBackstage, seriesConjured, seriesElixir, seriesSulfuras, seriesVest);


        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Barchart creationDate
        //Initialisation
        barChartCreationDate.getXAxis().setLabel("Creation Date");
        barChartCreationDate.getYAxis().setLabel("Quantity");
        barChartCreationDate.getYAxis().setAutoRanging(false);
        //XYChart.Series series2 = new XYChart.Series();
        //series2.setName("All items");

        XYChart.Series series2AgedBrie = new XYChart.Series();
        XYChart.Series series2Backstage = new XYChart.Series();
        XYChart.Series series2Conjured = new XYChart.Series();
        XYChart.Series series2Elixir = new XYChart.Series();
        XYChart.Series series2Sulfuras = new XYChart.Series();
        XYChart.Series series2Vest = new XYChart.Series();

        barChartCreationDate.getData().clear();

        series2AgedBrie.setName("Aged Brie");
        series2Backstage.setName("Backstage");
        series2Conjured.setName("Conjured");
        series2Elixir.setName("Elixir");
        series2Sulfuras.setName("Sulfuras");
        series2Vest.setName("Vest");

        int number2Ab = 0;
        int number2Bs = 0;
        int number2C = 0;
        int number2E = 0;
        int number2S = 0;
        int number2V = 0;

        //On recupere dans une liste les DIFFERENTES DATES
        //Chaque date est donc contenue une et une unique fois dans la liste
        ObservableList<Date> itemsDate = FXCollections.observableArrayList();
        for (Item i : itemslist) {
            if (itemsDate.contains(i.getCreationDate())) {
                //Si la liste contient la date ne fais rien
            } else {
                //Sinon ajoute la date
                itemsDate.add(i.getCreationDate());
            }
        }


        //Pour chaque date, compte le nombre de date dans itemsList et ajoute la donnée dans series2
        for (Date d : itemsDate) {
            if (atLeastOne(itemslist, String.valueOf(d))) {
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String barName = formatter.format(d);
                series2AgedBrie.getData().add(new XYChart.Data(barName, countItemByCreationDate(itemslist, barName, (item) -> item instanceof AgedBrie)));
                series2Backstage.getData().add(new XYChart.Data(barName, countItemByCreationDate(itemslist, barName, (item) -> item instanceof Backstage)));
                series2Conjured.getData().add(new XYChart.Data(barName, countItemByCreationDate(itemslist, barName, (item) -> item instanceof Conjured)));
                series2Elixir.getData().add(new XYChart.Data(barName, countItemByCreationDate(itemslist, barName, (item) -> item instanceof Elixir)));
                series2Sulfuras.getData().add(new XYChart.Data(barName, countItemByCreationDate(itemslist, barName, (item) -> item instanceof Sulfuras)));
                series2Vest.getData().add(new XYChart.Data(barName, countItemByCreationDate(itemslist, barName, (item) -> item instanceof Vest)));

                number2Ab += countItemByCreationDate(itemslist, barName, (item) -> item instanceof AgedBrie);
                number2Bs += countItemByCreationDate(itemslist, barName, (item) -> item instanceof Backstage);
                number2C += countItemByCreationDate(itemslist, barName, (item) -> item instanceof Conjured);
                number2E += countItemByCreationDate(itemslist, barName, (item) -> item instanceof Elixir);
                number2S += countItemByCreationDate(itemslist, barName, (item) -> item instanceof Sulfuras);
                number2V += countItemByCreationDate(itemslist, barName, (item) -> item instanceof Vest);
            }
        }


        NumberAxis tmp2 = (NumberAxis) barChartCreationDate.getYAxis();
        tmp2.setLowerBound(0);
        tmp2.setUpperBound(number2Ab + number2Bs + number2C + number2E + number2S + number2V + 2);

        if (number2Ab != 0)
            barChartCreationDate.getData().add(series2AgedBrie);
        if (number2Bs != 0)
            barChartCreationDate.getData().add(series2Backstage);
        if (number2C != 0)
            barChartCreationDate.getData().add(series2Conjured);
        if (number2E != 0)
            barChartCreationDate.getData().add(series2Elixir);
        if (number2S != 0)
            barChartCreationDate.getData().add(series2Sulfuras);
        if (number2V != 0)
            barChartCreationDate.getData().add(series2Vest);

        //Ajoute toutes les données de series2 dans le barchart
        //barChartCreationDate.getData().addAll(series2AgedBrie, series2Backstage, series2Conjured, series2Elixir, series2Sulfuras, series2Vest);

    }

    private int countItem(ObservableList<Item> items, Function<Item, Boolean> func) {
        int count = 0;
        for (Item i : items) {
            if (func.apply(i)) {
                count++;
            }
        }
        return count;
    }

    //Retourne la valeur max de sellin possible
    private int checkMaxSellIn(ObservableList<Item> items) {
        int max = 0;
        for (Item i : items) {
            if (i.getSellIn() > max) {
                max = i.getSellIn();
            }
        }
        return max;
    }

    //Retourne le sellin qui contient le plus d'item
    private int checkNumberMaxItemBySellin(ObservableList<Item> items, int sellinMax) {
        int max = -1;
        int resultSellin = 0;
        int numberItem = -1;
        for (int k = 0; k < sellinMax; k++) {
            for (Item i : items) {
                if (atLeastOne(items, k)) {
                    if (i.getSellIn() == k) {
                        numberItem++;
                    }
                }
            }
            if (numberItem > max) {
                max = numberItem;
                numberItem = 0;
                resultSellin = k;
            }
        }
        return resultSellin;
    }


    //Renvoie true s'il y a au moins un item pour le sellin indiqué
    private boolean atLeastOne(ObservableList<Item> items, int sellIn) {
        for (Item i : items) {
            if (i.getSellIn() == sellIn) {
                return true;
            }
        }
        return false;
    }

    //Compte le nombre d'item de chaque type par date de Sellin
    private int countItemBySellIn(ObservableList<Item> items, int sellIn, Function<Item, Boolean> func) {
        int count = 0;
        for (Item i : items) {
            if (func.apply(i)) {
                if (i.getSellIn() == sellIn) {
                    count++;
                }
            }
        }
        return count;
    }

    //Fonction qui compte cb il y a d'item pour le sellin indiqué, sans prendre en compte le type
    private int _countItemBySellIn(ObservableList<Item> items, int sellIn) {
        int count = 0;
        for (Item i : items) {

            if (i.getSellIn() == sellIn) {
                count++;
            }

        }
        return count;
    }


    private boolean atLeastOne(ObservableList<Item> items, String creationDate) {
        for (Item i : items) {
            String dateItem = String.valueOf(i.getCreationDate());
            if (dateItem.equals(creationDate)) {
                return true;
            }
        }
        return false;
    }

    //Compte le nombre d'item de chaque type par date de creation
    private int countItemByCreationDate(ObservableList<Item> items, String creationDate, Function<Item, Boolean> func) {
        int count = 0;
        for (Item i : items) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateItem = formatter.format(i.getCreationDate());
            if (func.apply(i)) {
                if (dateItem.equals(creationDate)) {
                    count++;
                }
            }
        }
        return count;
    }

    private int countItemByCreationDate(ObservableList<Item> items, String creationDate) {
        int count = 0;
        for (Item i : items) {
            String dateItem = String.valueOf(i.getCreationDate());
            if (dateItem.equals(creationDate)) {
                count++;
            }
        }
        return count;
    }

    public PieChart.Data createPieChartItem(String name, int count) {
        return new PieChart.Data(name, count);
    }

    public void onDelete() {
        Item itemToDelete = inv.fetchItemById(Integer.parseInt(idNumberLabel.getText()));
        inv.deleteItem(itemToDelete);
        auditTrail.addToVentes(itemToDelete);
        fetchItems();
        saveButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        allBlank();
    }

    public void onEdit() {
        saveButton.setDisable(false);
        nameTF.setDisable(false);
        sellinTF.setDisable(false);
        qualityTF.setDisable(false);
        typeComboBox.setDisable(false);
        addMode = false;

        cancelButton.setDisable(false);
    }

    public void onAdd() {
        addMode = true;
        allBlank();
        saveButton.setDisable(false);
        nameTF.setDisable(false);
        sellinTF.setDisable(false);
        qualityTF.setDisable(false);
        typeComboBox.setDisable(false);
        creationDateDatePicker.setDisable(false);
        typeComboBox.getSelectionModel().select(-1);
        ;
        cancelButton.setDisable(false);
    }

    public void onSave() {
        String name = nameTF.getText();
        String type = (String) typeComboBox.getValue();
        int sellin = 0;
        int quality = 0;
        try {
            sellin = Integer.parseInt(sellinTF.getText());
        } catch (Exception e) {
            sellin = 0;
        }
        try {
            quality = Integer.parseInt(qualityTF.getText());
        } catch (Exception e) {
            quality = 0;
        }
        Date date = Date.from(creationDateDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        if (addMode == true) {
            switch (type) {
                case "AgedBrie":
                    AgedBrie agedBrie = new AgedBrie(name, sellin, quality,date);
                    inv.addItem(agedBrie);
                    auditTrail.addToAchats(agedBrie);
                    break;
                case "Backstage":
                    Backstage backstage = new Backstage(name, sellin, quality,date);
                    inv.addItem(backstage);
                    auditTrail.addToAchats(backstage);
                    break;
                case "Conjured":
                    Conjured conjured = new Conjured(name, sellin, quality,date);
                    inv.addItem(conjured);
                    auditTrail.addToAchats(conjured);
                    break;
                case "Elixir":
                    Elixir elixir = new Elixir(name, sellin, quality,date);
                    inv.addItem(elixir);
                    auditTrail.addToAchats(elixir);
                    break;
                case "Sulfuras":
                    Sulfuras sulfuras = new Sulfuras(name, sellin, quality,date);
                    inv.addItem(sulfuras);
                    auditTrail.addToAchats(sulfuras);
                    break;
                case "Vest":
                    Vest vest = new Vest(name, sellin, quality,date);
                    inv.addItem(vest);
                    auditTrail.addToAchats(vest);
                    break;
            }

        } else {
            int id = Integer.parseInt(idNumberLabel.getText());
            switch (type) {
                case "AgedBrie":
                    AgedBrie ab = new AgedBrie(name, sellin, quality);
                    inv.editItem(id, ab);
                    break;
                case "Backstage":
                    Backstage bs = new Backstage(name, sellin, quality);
                    inv.editItem(id, bs);
                    break;
                case "Conjured":
                    Conjured conjured = new Conjured(name, sellin, quality);
                    inv.editItem(id, conjured);
                    break;
                case "Elixir":
                    Elixir elix = new Elixir(name, sellin, quality);
                    inv.editItem(id, elix);
                    break;
                case "Sulfuras":
                    Sulfuras sulf = new Sulfuras(name, sellin, quality);
                    inv.editItem(id, sulf);
                    break;
                case "Vest":
                    Vest vest = new Vest(name, sellin, quality);
                    inv.editItem(id, vest);
                    break;
            }
            //Item newItem = new Item(name,sellin,quality);

        }
        fetchItems();
        saveButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        creationDateDatePicker.setDisable(true);
    }

    public void onUpdate() {
        inv.updateSellin();
        inv.updateQuality();
        fetchItems();
    }

    public void onCancel() {
        nameTF.setDisable(true);
        sellinTF.setDisable(true);
        qualityTF.setDisable(true);
        typeComboBox.setDisable(true);
        saveButton.setDisable(true);
        editButton.setDisable(true);
        creationDateDatePicker.setDisable(true);
        cancelButton.setDisable(true);

        if (addMode == true) {
            allBlank();
        } else {
            fetchItems();
        }
    }

    public void onLoad() {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);

        File file = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            System.out.println("Opening " + file.getName());
        }

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(file));

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray itemsList = (JSONArray) jsonObject.get("items");

            Iterator<JSONObject> iterator = itemsList.iterator();
            while (iterator.hasNext()) {
                JSONObject tempObj = iterator.next();
                String name = (String) tempObj.get("name");
                String type = (String) tempObj.get("type");
                int quality = toIntExact((long) tempObj.get("quality"));
                int sellin = toIntExact((long) tempObj.get("sellIn"));
                boolean hasDate = false;
                Date date = new Date();
                if (tempObj.containsKey("creationDate")) {
                    hasDate = true;
                    date = new SimpleDateFormat("dd-MM-yyyy").parse((String) tempObj.get("creationDate"));
                }

                switch (type) {
                    case "AgedBrie":
                        AgedBrie agedBrie;
                        if (hasDate)
                            agedBrie = new AgedBrie(name, sellin, quality, date);
                        else agedBrie = new AgedBrie(name, sellin, quality);
                        inv.addItem(agedBrie);
                        auditTrail.addToAchats(agedBrie);
                        break;
                    case "Backstage":
                        Backstage backstage;
                        if (hasDate)
                            backstage = new Backstage(name, sellin, quality, date);
                        else backstage = new Backstage(name, sellin, quality);
                        inv.addItem(backstage);
                        auditTrail.addToAchats(backstage);
                        break;
                    case "Conjured":
                        Conjured conjured;
                        if (hasDate)
                            conjured = new Conjured(name, sellin, quality, date);
                        else conjured = new Conjured(name, sellin, quality);
                        inv.addItem(conjured);
                        auditTrail.addToAchats(conjured);
                        break;
                    case "Elixir":
                        Elixir elixir;
                        if (hasDate)
                            elixir = new Elixir(name, sellin, quality, date);
                        else elixir = new Elixir(name, sellin, quality);
                        inv.addItem(elixir);
                        auditTrail.addToAchats(elixir);
                        break;
                    case "Sulfuras":
                        Sulfuras sulfuras;
                        if (hasDate)
                            sulfuras = new Sulfuras(name, sellin, quality, date);
                        else sulfuras = new Sulfuras(name, sellin, quality);
                        inv.addItem(sulfuras);
                        auditTrail.addToAchats(sulfuras);
                        break;
                    case "Vest":
                        Vest vest;
                        if (hasDate)
                            vest = new Vest(name, sellin, quality, date);
                        else vest = new Vest(name, sellin, quality);
                        inv.addItem(vest);
                        auditTrail.addToAchats(vest);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fetchItems();
    }

    public void allBlank() {
        nameTF.setText("");
        sellinTF.setText("");
        qualityTF.setText("");
        idNumberLabel.setText("");
        typeComboBox.setValue(null);
    }

    public static void main(String[] args) {

        launch(args);
    }
}
