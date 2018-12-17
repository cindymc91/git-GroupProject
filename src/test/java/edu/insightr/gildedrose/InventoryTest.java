package edu.insightr.gildedrose;

import edu.insightr.gildedrose.Model.Inventory;
import edu.insightr.gildedrose.Model.Item;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

public class InventoryTest {

    protected Inventory inv; //Toujours créer une instance de la classe à tester
    protected Item[] oldList;

    @Before
    public void setUp() throws ParseException {
        inv = new Inventory();
        oldList = inv.getItems(); //On définit l'ancienne liste ici, car c'est un préalable au test
    }


    //Testing :
    //Once the sell by date has passed, Quality degrades twice as fast
    //"Sulfuras", being a legendary item, never has to be sold or decreases in Quality
    @Test
    public void testUpdateQualityWhenSellInFinished() throws Exception {
        inv.updateQuality(); //Méthode qui update la qualité

        for (int i = 0; i < inv.getItems().length; i++) {
            if (oldList[i].getSellIn() == 0) {
                if (oldList[i].getName().matches(".*Sulfuras.*"))
                    assertEquals(80, inv.getItems()[i].getQuality());
                else
                    assertEquals(oldList[i].getQuality() - 2, inv.getItems()[i].getQuality());
            }
        }
    }

    //Testing :
    //"Aged Brie" actually increases in Quality the older it gets
    @Test
    public void testUpdateAgedBrie() throws Exception {
        inv.updateQuality();
        for (int i = 0; i < inv.getItems().length; i++) {
            if (oldList[i].getName().matches(".*Aged Brie.*"))
                assertFalse(oldList[i].getQuality() > inv.getItems()[i].getQuality());
        }

    }

    //Testing :
    //"Backstage passes", like aged brie, increases in Quality as it's SellIn value approaches;
    // Quality increases by 2 when there are 10 days or less and by 3 when there are 5 days or less but Quality drops to 0 after the concert
    //I'm considering that "increasing by 3" means that if the quality is to increment by 1 then it increments by 3 instead
    @Test
    public void testUpdateBackstage() throws Exception {
        inv.updateQuality();
        for (int i = 0; i < inv.getItems().length; i++) {
            if (oldList[i].getName().matches(".*Backstage.*")) {
                assertFalse(oldList[i].getQuality() > inv.getItems()[i].getQuality());
                if (oldList[i].getSellIn() == 0)
                    assertEquals(0, inv.getItems()[i].getQuality());
                else {
                    if (oldList[i].getSellIn() <= 5 && oldList[i].getSellIn() > 0)
                        assertEquals(oldList[i].getQuality() + 3, inv.getItems()[i].getQuality());
                    if (oldList[i].getSellIn() <= 10 && oldList[i].getSellIn() > 0)
                        assertEquals(oldList[i].getQuality() + 2, inv.getItems()[i].getQuality());
                }

            }
        }

    }

    //Testing:
    // "Conjured" items degrade in Quality twice as fast as normal items

    /*public void testUpdateConjured() throws Exception {
        inv.updateQuality();
        for (int i = 0; i < inv.getItems().length; i++) {
            if (oldList[i].getName().matches(".*Conjured.*")) {
                assertEquals(oldList[i].getQuality() - 2, inv.getItems()[i].getQuality());
            }
        }
    }*/

    @Test
    public void testAddItemInventory() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        inv.addItem(new Elixir("Elixir",15,20,format.parse("17-12-2018")));
        assertEquals(oldList.length+1,inv.getItems().length);
    }



    // Testing :
    // Length of inventory list decreased by one
    @Test
    public void testDeleteLength() throws Exception{
        Item item = inv.getItems()[0];
        inv.deleteItem(item);
        assertEquals(oldList.length - 1,inv.getItems().length);
    }

    // Testing :
    // Deleted item cannot be found in the inventory list = item successfully deleted
    @Test
    public void testDeleteCorrectItem() throws Exception{
        Item item = inv.getItems()[0];
        int id = item.getId();
        inv.deleteItem(item);
        for(Item i : inv.getItems())
        {
            assertFalse(i.getId() == id);
        }
    }


    @Test
    public void main() {
    }
}