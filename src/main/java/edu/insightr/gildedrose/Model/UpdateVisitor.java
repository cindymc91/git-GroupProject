package edu.insightr.gildedrose.Model;

public class UpdateVisitor implements IVisitor {

    public void visit(AgedBrie agedBrie) {
        if (agedBrie.getQuality() >= 0 && agedBrie.getQuality() < 50) {
            agedBrie.setQuality(agedBrie.getQuality() + 1);
        }

        if (agedBrie.getQuality() < 0)
            agedBrie.setQuality(0);

        if (agedBrie.getQuality() > 50)
            agedBrie.setQuality(50);
    }

    public void visit(Backstage backStage) {
            if (backStage.getSellIn() < 0) {
                    backStage.setQuality(0);
            } else {
                if (backStage.getSellIn() <= 10) {
                    if (backStage.getSellIn() <= 5) {
                        backStage.setQuality(backStage.getQuality() + 3);
                    } else {
                        backStage.setQuality(backStage.getQuality() + 2);
                    }
                } else {
                    backStage.setQuality(backStage.getQuality() + 1);
                }
            }

        if (backStage.getQuality() < 0)
            backStage.setQuality(0);

        if (backStage.getQuality() > 50)
            backStage.setQuality(50);
    }

    public void visit(Conjured conjuredItem) {
            if (conjuredItem.getSellIn() < 0) {
                conjuredItem.setQuality(conjuredItem.getQuality() - 4);
            }
            else
                conjuredItem.setQuality(conjuredItem.getQuality() - 2);

        if (conjuredItem.getQuality() < 0)
            conjuredItem.setQuality(0);

        if (conjuredItem.getQuality() > 50)
            conjuredItem.setQuality(50);
    }

    public void visit(Elixir elixir) {
            if (elixir.getSellIn() < 0) {
                elixir.setQuality(elixir.getQuality() - 2);
            }
            else{
                elixir.setQuality(elixir.getQuality() - 1);
            }


        if (elixir.getQuality() < 0)
            elixir.setQuality(0);

        if (elixir.getQuality() > 50)
            elixir.setQuality(50);
    }

    public void visit(Sulfuras sulfuras) {
        sulfuras.setQuality(80);
    }

    public void visit(Vest vest) {
            if (vest.getSellIn() < 0) {
                vest.setQuality(vest.getQuality() - 2);
            }
            else{
                vest.setQuality(vest.getQuality() - 1);
            }
        if (vest.getQuality() < 0)
            vest.setQuality(0);

        if (vest.getQuality() > 50)
            vest.setQuality(50);
    }

}
