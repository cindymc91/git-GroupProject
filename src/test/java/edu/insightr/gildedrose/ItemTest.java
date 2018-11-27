package edu.insightr.gildedrose;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {

    Item anItem;

    @Before
    public void setUp() {
        anItem = new Sulfuras("Sulfuras", 0, 80);
    }

    @Test
    public void getName() {
    }

    @Test
    public void setName() {
    }

    @Test
    public void getSellIn() {
        if(anItem.getName().matches(".*Sulfuras.*"))
            assertEquals(0, anItem.getSellIn());
    }

    @Test
    public void setSellIn() {
    }

    //Testing:
    //The Quality of an item is never negative
    //The Quality of an item is never more than 50
    //"Sulfuras" is a legendary item and as such its Quality is 80 and it never alters
    @Test
    public void getQuality() {
        assertTrue(anItem.getQuality() >= 0);

        if (anItem.getName().matches(".*Sulfuras.*")) //The name contains 'Sulfuras'
            assertEquals(80, anItem.getQuality());
        else
            assertFalse(anItem.getQuality() > 50);

    }

    @Test
    public void setQuality() {
    }
}