Feature: Unique IDs
  # Each Item has a unique ID, no two items can have the same ID

  Scenario: unique ID check
    Given I have a list of sorted item IDs
    Then No two consecutive values are the same