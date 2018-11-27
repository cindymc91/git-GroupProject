package edu.insightr.gildedrose;

public class AgedBrie extends Item{

    public AgedBrie() {
        super();
    }

    public AgedBrie(String name, int sellIn, int quality) {
        super(name, sellIn, quality);
    }

    @Override
    public void accept(IVisitor v){
        v.visit(this);

    }

}
