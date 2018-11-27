package edu.insightr.gildedrose;

//This strategy concerns Aged Brie & Backstage items
public class StrategyUp implements IStrategy{

    @Override
    public void update(Item it){
        String theName = it.getName();

        //Update Aged Brie items
       if (theName.matches("(?i:.*aged brie.*)")){
           if (it.getSellIn() < 0)
               it.setQuality(0);
           else
               it.setQuality(it.getQuality() + 1);
       }
       //Update Backstage items
       else{
           if (it.getSellIn() < 0) {
               it.setQuality(0);
           } else {
               if (it.getSellIn() <= 10) {
                   if (it.getSellIn() <= 5) {
                       it.setQuality(it.getQuality() + 3);
                   } else {
                       it.setQuality(it.getQuality() + 2);
                   }
               } else {
                   it.setQuality(it.getQuality() + 1);
               }
           }
       }

       //Concerns the 2 items of this strategy
        if (it.getQuality() < 0)
            it.setQuality(0);

        if (it.getQuality() > 50)
            it.setQuality(50);
    }
}
