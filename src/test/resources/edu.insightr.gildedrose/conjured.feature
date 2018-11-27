Feature: Conjured Item
  # a conjured item quality decreases twice the normal rate

  Scenario: conjured item update
    Given I have a new inventory
    Then the quality of the conjured item is initialized at 6
    When I update the inventory
    Then the quality of the conjured item is 4