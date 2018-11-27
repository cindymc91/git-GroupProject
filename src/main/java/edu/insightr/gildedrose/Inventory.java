package edu.insightr.gildedrose;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private Item[] items;
    private List<IStrategy> listStrat;

    public Item[] getItems() {
        return items;
    }

    public Inventory(Item[] items) {
        this.items = items;
    }

    public Inventory() {
        items = new Item[]{
                new Item("+5 Dexterity Vest", 10, 20),
                new Item("Aged Brie", 2, 0),
                new Item("Elixir of the Mongoose", 5, 7),
                new Item("Sulfuras, Hand of Ragnaros", 0, 80),
                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                new Item("Conjured Mana Cake", 3, 6)
        };

        listStrat = new ArrayList<>();
        listStrat.add(new StrategyUp());
        listStrat.add(new StrategyDown());
        listStrat.add(new StrategySulfuras());

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

    public void updateQuality(){
        String theName = null;
        for(int i = 0; i < items.length; i++){
            theName = items[i].getName();
            if(theName.matches("(?i:.*aged brie.*)") || theName.matches("(?i:.*backstage.*)")){
                listStrat.get(0).update(items[i]);
            }
            else if(theName.matches("(?i:.*Sulfuras.*)"))
                listStrat.get(2).update(items[i]);
            else
                listStrat.get(1).update(items[i]);
        }
    }

    void updateSellin(){
        for (int i = 0; i < items.length; i++){
            items[i].setSellIn(items[i].getSellIn()-1);
        }
    }


    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        for (int i = 0; i < 10; i++) {
            inventory.updateSellin();
            inventory.updateQuality();
            inventory.printInventory();
        }
    }


}
