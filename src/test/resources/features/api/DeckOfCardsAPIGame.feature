@regression
Feature: Deck of cards api game

  Scenario: Navigate and check status of the checker game website
    Given deck of cards api game url
    When trying to use the card api
    Then get a deck

  Scenario: Get a new deck and play blackjack
    Given A new deck of cards
    When shuffled
    And Dealt three card to two players
    Then Check who has blackjack
    And Name the winner

