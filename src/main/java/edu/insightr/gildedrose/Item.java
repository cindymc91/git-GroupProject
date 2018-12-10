package edu.insightr.gildedrose;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Item implements IVisitable{

    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private String name;
    private int sellIn; //number of days remaining to sell the item
    private int quality; //how valuable the item is

    public Item(){
        this.id = count.incrementAndGet();
        this.name = null;
        this.sellIn = -1;
        this.quality = -1;
    }

    public Item(String name, int sellIn, int quality) {
        super();
        this.id = count.incrementAndGet();
        this.name = name;
        this.sellIn = sellIn;
        this.quality = quality;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSellIn() {
        return sellIn;
    }

    public void setSellIn(int sellIn) {
        this.sellIn = sellIn;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }


    public void edit(Item editedItem)
    {
        this.setName(editedItem.getName());
        this.setSellIn(editedItem.getSellIn());
        this.setQuality(editedItem.getQuality());
    }


    @Override
    public String toString() {
        return name;
    }


}