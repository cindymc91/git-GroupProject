package edu.insightr.gildedrose;

//This strategy concerns Conjured, Elixir & Vest items
public class StrategyDown implements IStrategy {

    @Override
    public void update(Item it){
        String theName = it.getName();

        //Update Conjured items
        if (theName.matches("(?i:.*conjured.*)")){
            if (it.getSellIn() < 0) {
                it.setQuality(it.getQuality() - 4);
            }
            else
                it.setQuality(it.getQuality() - 2);
        }
        //Update Elixir & Vest items
        else{
            if (it.getSellIn() < 0) {
                it.setQuality(it.getQuality() - 2);
            }
            else{
                it.setQuality(it.getQuality() - 1);
            }
        }

        //Concerns the 3 items of this strategy
        if (it.getQuality() < 0)
            it.setQuality(0);

        if (it.getQuality() > 50)
            it.setQuality(50);
    }
}
