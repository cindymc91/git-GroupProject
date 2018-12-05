package edu.insightr.gildedrose;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Inventory extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("mainwindow.fxml"));
        primaryStage.setTitle("Hello World");
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
        for (int i = 0; i < items.length; i++) {
            if (item.getName().equals(items[i].getName())) {
                items[i] = items[i + 1];
            }
        }
        items[items.length - 1] = null;
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

    Item fetchItemByName(String name){
        for(int i = 0; i < items.length; i++){
            if(items[i].getName().equals(name))
                return items[i];
        }
        return null;
    }


    public static void main(String[] args) {
        launch(args);
//        Inventory inventory = new Inventory();
//        for (int i = 0; i < 10; i++) {
//            inventory.updateSellin();
//            inventory.updateQuality();
//            inventory.printInventory();
//        }
    }
}
