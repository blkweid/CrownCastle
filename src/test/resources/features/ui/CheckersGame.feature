@regression
Feature: Checkers UI game

  Scenario: Verifying that the checkers game website is up
    Given the user navigates to checkers game website
    Then user should be able to access the checkers game

  Scenario: Verifying making five legal moves as orange
    Given the user navigates to checkers game website
    When user makes five legal moves and takes a blue piece
    Then user should be able to take the next step before each move

    Scenario: Verifying that the game restart can be done successfully
      Given the user navigates to checkers game website
      When user makes five legal moves and takes a blue piece
      And user should be able to restart the game after five moves
