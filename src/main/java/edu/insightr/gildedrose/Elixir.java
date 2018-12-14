package edu.insightr.gildedrose;

import java.util.Date;

public class Elixir extends Item{

    public Elixir() {
        super();
    }

    public Elixir(String name, int sellIn, int quality) {
        super(name, sellIn, quality);
    }

    public Elixir(String name, int sellIn, int quality, Date creationDate) {
        super(name, sellIn, quality, creationDate);
    }

    @Override
    public void accept(IVisitor v){
        v.visit(this);

    }
}
