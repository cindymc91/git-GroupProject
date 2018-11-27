package edu.insightr.gildedrose;

public class Elixir extends Item{

    public Elixir() {
        super();
    }

    public Elixir(String name, int sellIn, int quality) {
        super(name, sellIn, quality);
    }

    @Override
    public void accept(IVisitor v){
        v.visit(this);

    }
}
