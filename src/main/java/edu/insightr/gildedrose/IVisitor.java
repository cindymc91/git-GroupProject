package edu.insightr.gildedrose;

public interface IVisitor {

    //public void visit(Item item);
    public void visit(AgedBrie agedBrie);
    public void visit(Backstage backStage);
    public void visit(Conjured conjuredItem);
    public void visit(Elixir elixir);
    public void visit(Sulfuras sulfuras);
    public void visit(Vest vest);
}
