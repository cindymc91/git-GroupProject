package edu.insightr.gildedrose;

import javafx.application.Application;
import javafx.beans.binding.IntegerBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import static java.lang.Math.toIntExact;

public class Inventory extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("mainwindow.fxml"));
        primaryStage.setTitle("Main Window");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private Item[] items;

    public Item[] getItems() {
        return items;
    }

    public Inventory(Item[] items) {
        this.items = items;
    }

   /* public Inventory() {
        items = new Item[]{
                new Item("+5 Dexterity Vest", 10, 20),
                new Item("Aged Brie", 2, 0),
                new Item("Elixir of the Mongoose", 5, 7),
                new Item("Sulfuras, Hand of Ragnaros", 0, 80),
                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                new Item("Conjured Mana Cake", 3, 6)
        };
    }*/

    public Inventory() {
        items = new Item[]{
                new Vest("+5 Dexterity Vest", 10, 20),
                new AgedBrie("Aged Brie", 2, 0),
                new Elixir("Elixir of the Mongoose", 5, 7),
                new Sulfuras("Sulfuras, Hand of Ragnaros", 0, 80),
                new Backstage("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                new Conjured("Conjured Mana Cake", 3, 6)
        };
    }

    public void printInventory() {
        System.out.println("***************");
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.println("***************");
        System.out.println("\n");
    }


    /*public void updateQuality() { //Méthode qu'on appelle à chaque fin de journée pour mettre à jour la qualité de chaque item
        for (int i = 0; i < items.length; i++) {
            if (items[i].getName() != "Aged Brie"
                    && items[i].getName() != "Backstage passes to a TAFKAL80ETC concert") {
                if (items[i].getQuality() > 0) {
                    if (items[i].getName() != "Sulfuras, Hand of Ragnaros") {
                        items[i].setQuality(items[i].getQuality() - 1);
                    }
                }
            } else {
                if (items[i].getQuality() < 50) {
                    items[i].setQuality(items[i].getQuality() + 1);

                    if (items[i].getName() == "Backstage passes to a TAFKAL80ETC concert") {
                        if (items[i].getSellIn() < 11) {
                            if (items[i].getQuality() < 50) {
                                items[i].setQuality(items[i].getQuality() + 1);
                            }
                        }

                        if (items[i].getSellIn() < 6) {
                            if (items[i].getQuality() < 50) {
                                items[i].setQuality(items[i].getQuality() + 1);
                            }
                        }
                    }
                }
            }

            if (items[i].getName() != "Sulfuras, Hand of Ragnaros") {
                items[i].setSellIn(items[i].getSellIn() - 1);
            }

            if (items[i].getSellIn() < 0) {
                if (items[i].getName() != "Aged Brie") {
                    if (items[i].getName() != "Backstage passes to a TAFKAL80ETC concert") {
                        if (items[i].getQuality() > 0) {
                            if (items[i].getName() != "Sulfuras, Hand of Ragnaros") {
                                items[i].setQuality(items[i].getQuality() - 1);
                            }
                        }
                    } else {
                        items[i].setQuality(items[i].getQuality() - items[i].getQuality());
                    }
                } else {
                    if (items[i].getQuality() < 50) {
                        items[i].setQuality(items[i].getQuality() + 1);
                    }
                }
            }
        }
    }*/

    void updateQuality(){
        UpdateVisitor aVisitor = new UpdateVisitor();
        for (int i = 0; i < items.length; i++){
            items[i].accept(aVisitor);
        }
    }

    void updateSellin(){
        for (int i = 0; i < items.length; i++){
            items[i].setSellIn(items[i].getSellIn()-1);
        }
    }

    void deleteItem(Item item) {
        int position= -1;
        for (int i = 0; i < items.length; i++) {
            if (item.getId() == items[i].getId()) {
                position = i;
            }
        }
        Item [] newItems = new Item[items.length -1];
        for (int i = 0; i < position; i++){
            newItems[i] = items[i];
        }
        for(int i = position; i < newItems.length; i++){
            newItems[i] = items[i+1];
        }
        items = newItems;
    }


    void addItem(Item item){
        int freeIndex = -1;
        for( int i= 0;i<items.length;i++){
            if(items[i]==null){
                freeIndex =i;
                break;
            }
        }

        if(freeIndex == -1){
            Item[] temp = new Item[items.length+1];
            for(int i =0;i<items.length;i++){
                temp[i]=items[i];
            }
            temp[items.length]=item;
            items=temp;
        }
        else{
            items[freeIndex]= item;
        }
    }

    void editItem(int idDeItemAModifier, Item nouveauItem)
    {
        for(int i=0; i<items.length;i++)
        {
            if(items[i].getId() == idDeItemAModifier)
            {
                items[i].edit(nouveauItem);
            }
        }
    }

    Item fetchItemById(int id){
        try{
            for(int i = 0; i < items.length; i++) {
                if (items[i].getId() == id)
                    return items[i];
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static void main(String[] args) {

        //launch(args);
        Inventory inventory = new Inventory();
        for (int i = 0; i < inventory.getItems().length; i++) {
            System.out.println(inventory.getItems()[i].getId() + " " + inventory.getItems()[i].getName() + " "
            +inventory.getItems()[i].getCreationDate());
        }

//        for (int i = 0; i < 10; i++) {
//            inventory.updateSellin();
//            inventory.updateQuality();
//            inventory.printInventory();
//        }
    }
}
