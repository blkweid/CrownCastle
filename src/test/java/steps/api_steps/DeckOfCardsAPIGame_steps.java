package steps.api_steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

public class DeckOfCardsAPIGame_steps {
    // All of this can also be done better using Jackson library, I can talk about it on follow-up call.

    // Variables to store the API response and deck ID
    Response response;
    String deckId;
    // Players and their initial card values
    final static String p1 = "Susan";
    int p1Value;
    final static String p2 = "Nancy";
    int p2Value;

    // Step to set the base URL for the deck of cards API
    @Given("deck of cards api game url")
    public void deckOfCardsApiGameUrl() {
        RestAssured.baseURI = "https://deckofcardsapi.com/";
        RestAssured.basePath = "/api/deck/";
    }

    // Step to draw two cards from a new shuffled deck
    @When("trying to use the card api")
    public void tryingToUseTheCardApi() {
        RequestSpecification httpRequest = RestAssured.given();
        response = httpRequest.get("/new/draw/?count=2");

        int statusCode = response.getStatusCode();
        Assert.assertEquals("Drew 2 cards from a new shuffled deck", 200, statusCode);
    }

    // Step to shuffle and get a new deck
    @Then("get a deck")
    public void getADeck() {
        RequestSpecification httpRequest = RestAssured.given();
        response = httpRequest.get("/new/shuffle/");

        int statusCode = response.getStatusCode();
        JsonPath jPath = new JsonPath(response.body().asString());
        deckId = jPath.getString("deck_id");
        System.out.println("first deck id: "+deckId);
        Assert.assertEquals("New Deck Drawn 1", 200, statusCode );
    }

    // Step to get a new deck of cards
    @Given("A new deck of cards")
    public void aNewDeckOfCards() {
        RestAssured.baseURI = "https://deckofcardsapi.com/";
        RestAssured.basePath = "/api/deck/";

        RequestSpecification httpRequest = RestAssured.given();
        response = httpRequest.get("/new/shuffle/");

        int statusCode = response.getStatusCode();
        JsonPath jPath = new JsonPath(response.body().asString());
        deckId = jPath.getString("deck_id");
        System.out.println("second deck id: "+deckId);
        Assert.assertEquals("New Deck Drawn 1", 200, statusCode);
    }

    // Step to shuffle the deck
    @When("shuffled")
    public void shuffled() {
        RequestSpecification httpRequest = RestAssured.given();
        response = httpRequest.get("/"+deckId+"/shuffle/");

        int statusCode = response.getStatusCode();

        Assert.assertEquals("Deck Shuffled", 200, statusCode );
    }

    // Step to deal three cards to two players and calculate their values
    @And("Dealt three card to two players")
    public void dealtThreeCardToTwoPlayers() {
        RestAssured.baseURI = "https://deckofcardsapi.com/";
        RestAssured.basePath = "/api/deck/";

        RequestSpecification httpRequest = RestAssured.given();

        // Draw three cards for player 1
        response = httpRequest.get("/"+deckId+"/draw/?count=3");

        JsonPath jPath = new JsonPath(response.body().asString());

        p1Value = 0;
        p2Value = 0;

        // Prepare API call to add cards to player 1's pile
        StringBuilder p1AddCards = new StringBuilder("/"+deckId+"/pile/"+p1+"/add/?cards=");

        for(int i=0;i<3;i++){

            StringBuilder card = new StringBuilder(jPath.getString("cards["+i+"].value"));
            p1AddCards.append(jPath.getString("cards[" + i + "].code")).append(",");

            // Calculate card values for player 1
            switch (card.toString()){
                case "KING": case "QUEEN": case "JACK":
                    p1Value +=10;
                    break;
                case "ACE":
                    p1Value +=11;
                    break;
                default:
                    p1Value += Integer.parseInt(card.toString());
            }
        }

        // Add cards to player 1's pile
        response = httpRequest.get(p1AddCards.toString());


        // Draw three cards for player 2
        response = httpRequest.get("/"+deckId+"/draw/?count=3");

        jPath = new JsonPath(response.body().asString());

        // Prepare API call to add cards to player 2's pile
        StringBuilder p2AddCards = new StringBuilder("/"+deckId+"/pile/"+p2+"/add/?cards=");

        for(int i=0;i<3;i++){

            StringBuilder card = new StringBuilder(jPath.getString("cards["+i+"].value"));
            p2AddCards.append(jPath.getString("cards[" + i + "].code")).append(",");

            // Calculate card values for player 2
            switch (card.toString()){
                case "KING": case "QUEEN": case "JACK":
                    p2Value +=10;
                    break;
                case "ACE":
                    p2Value +=11;
                    break;
                default:
                    p2Value += Integer.parseInt(card.toString());
            }
        }
        // Add cards to player 2's pile
        response = httpRequest.get(p2AddCards.toString());
    }

    // Step to print the values of player cards and determine the winner
    @Then("Check who has blackjack")
    public void checkWhoHasBlackjack() {
        System.out.println(p1+" has cards in value of: "+p1Value+" \nwhile "+p2+" has cards in value of: "+p2Value);
    }

    // Step to determine the winner based on card values
    @And("Name the winner")
    public void nameTheWinner() {

        if(p1Value == p2Value){
            System.out.println("Draw");
        } else if ((p1Value <= 21) && (p2Value <= 21) && p1Value > p2Value){
            System.out.println(p1 +" is the winner");
        } else if (p1Value <= 21 && p2Value <= 21){
            System.out.println(p2 +" is the winner");
        } else if (p1Value > 21 && p2Value <= 21){
            System.out.println(p2 +" is the winner");
        } else if (p2Value > p1Value){
            System.out.println(p1 +" is the winner");
        }

    }
}
