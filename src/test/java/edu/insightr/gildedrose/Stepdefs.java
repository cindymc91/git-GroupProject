package edu.insightr.gildedrose;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.insightr.gildedrose.Model.Inventory;
import edu.insightr.gildedrose.Model.Item;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

public class Stepdefs {
    private Inventory inventory;
    private Item[] items;
    private ArrayList<Integer> itemIDs = new ArrayList<Integer>();
    private Item conjured;

    @Given("^I have a new inventory$")
    public void iHaveANewInventory() throws Throwable {
        inventory = new Inventory();
        items = inventory.getItems();
        conjured = items[5];
    }

    @Given("^I have a list of sorted item IDs$")
    public void iHaveAListOfSortedItemIds() throws Throwable {
        inventory = new Inventory();
        items = inventory.getItems();

        for (Item it : items) {
            itemIDs.add(it.getId());
        }
        itemIDs.add(4);
        Collections.sort(itemIDs);
    }

    @Then("^No two consecutive values are the same$")
    public void noConsecutiveValuesSame() throws Throwable {
        for (int i = 1; i < itemIDs.size(); i++) {
            System.out.println(itemIDs.get(i));
            assertNotSame("should not be same Object",itemIDs.get(i), itemIDs.get(i-1));
        }
    }

    @Then("^the quality of the conjured item is initialized at (\\d+)$")
    public void theQualityOfTheConjuredIs(int conjuredQuality) throws Throwable {
        assertThat(conjured.getQuality(), is(conjuredQuality));
    }

    @When("^I update the inventory$")
    public void iUpdateTheInventory() throws Throwable {
        inventory.updateQuality();
    }

    @Then("^the quality of the conjured item is (\\d+)$")
    public void theQualityOfTheConjuredItemIs(int conjuredQuality) throws Throwable {
        assertThat(conjured.getQuality(), is(conjuredQuality));
    }
}