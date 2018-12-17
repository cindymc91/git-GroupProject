package edu.insightr.gildedrose.Model;

import java.util.Date;

public class Backstage extends Item{

    public Backstage() {
        super();
    }

    public Backstage(String name, int sellIn, int quality) {
        super(name, sellIn, quality);
    }

    public Backstage(String name, int sellIn, int quality, Date creationDate) {
        super(name, sellIn, quality, creationDate);
    }

    @Override
    public void accept(IVisitor v){
        v.visit(this);

    }
}
