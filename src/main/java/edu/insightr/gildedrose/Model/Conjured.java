package edu.insightr.gildedrose.Model;

import java.util.Date;

public class Conjured extends Item{

    public Conjured() {
        super();
    }

    public Conjured(String name, int sellIn, int quality) {
        super(name, sellIn, quality);
    }

    public Conjured(String name, int sellIn, int quality, Date creationDate) {
        super(name, sellIn, quality, creationDate);
    }

    @Override
    public void accept(IVisitor v){
        v.visit(this);

    }
}
