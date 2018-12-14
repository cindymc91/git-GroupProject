package edu.insightr.gildedrose;

import java.util.Date;

public class Vest extends Item {

    public Vest() {
        super();
    }

    public Vest(String name, int sellIn, int quality) {
        super(name, sellIn, quality);
    }


    public Vest(String name, int sellIn, int quality, Date creationDate) {
        super(name, sellIn, quality, creationDate);
    }

    @Override
    public void accept(IVisitor v){
        v.visit(this);

    }
}
