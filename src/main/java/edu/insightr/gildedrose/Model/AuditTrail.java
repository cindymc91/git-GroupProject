package edu.insightr.gildedrose.Model;

import java.util.ArrayList;

public class AuditTrail {
    private ArrayList<Item> achats;
    private ArrayList<Item> ventes;

    public ArrayList<Item> getAchats() {
        return achats;
    }

    public void setAchats(ArrayList<Item> achats) {
        this.achats = achats;
    }

    public ArrayList<Item> getVentes() {
        return ventes;
    }

    public void setVentes(ArrayList<Item> ventes) {
        this.ventes = ventes;
    }

    public AuditTrail() {
        achats = new ArrayList<Item>();
        ventes = new ArrayList<Item>();
    }

    public void addToAchats(Item item)
    {
        achats.add(item);
    }

    public void addToVentes(Item item)
    {
        ventes.add(item);
    }
}
