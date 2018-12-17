package edu.insightr.gildedrose.Model;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Inventory {
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

    public Inventory() throws ParseException {
        String datestring = "09-12-2018";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = format.parse(datestring);
        items = new Item[]{
                new Vest("+5 Dexterity Vest", 10, 20, date),
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

    public void updateQuality(){
        UpdateVisitor aVisitor = new UpdateVisitor();
        for (int i = 0; i < items.length; i++){
            items[i].accept(aVisitor);
        }
    }

    public void updateSellin(){
        for (int i = 0; i < items.length; i++){
            items[i].setSellIn(items[i].getSellIn()-1);
        }
    }

    public void deleteItem(Item item) {
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


    public void addItem(Item item){
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

    public void editItem(int idDeItemAModifier, Item nouveauItem)
    {
        for(int i=0; i<items.length;i++)
        {
            if(items[i].getId() == idDeItemAModifier)
            {
                items[i].edit(nouveauItem);
            }
        }
    }

    public Item fetchItemById(int id){
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
}
