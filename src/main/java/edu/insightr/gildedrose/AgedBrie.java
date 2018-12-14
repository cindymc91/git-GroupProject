package edu.insightr.gildedrose;

import java.util.Date;

public class AgedBrie extends Item{

    public AgedBrie() {
        super();
    }

    public AgedBrie(String name, int sellIn, int quality) {
        super(name, sellIn, quality);
    }

    public AgedBrie(String name, int sellIn, int quality, Date creationDate) {
        super(name, sellIn, quality, creationDate);
    }

    @Override
    public void accept(IVisitor v){
        v.visit(this);

    }

}
