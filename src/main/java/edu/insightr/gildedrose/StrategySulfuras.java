package edu.insightr.gildedrose;

public class StrategySulfuras implements IStrategy {

    @Override
    public void update(Item it){
        it.setQuality(80);
    }
}
