package steps.ui_steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Driver;
import utilities.Flow;

import java.time.Duration;

public class CheckersGame_steps {

    @Given("the user navigates to checkers game website")
    public void theUserNavigatesToCheckersGameWebsite() {
        Driver.getDriver().get("https://www.gamesforthebrain.com/game/checkers/");
        Flow.wait(2000);
    }

    @Then("user should be able to access the checkers game")
    public void userShouldBeAbleToAccessTheCheckersGame() {
        String expectedTitle = "Checkers - Games for the Brain";
        String actualTitle = Driver.getDriver().getTitle();
        Assert.assertEquals(expectedTitle,actualTitle);
    }

    @When("user makes five legal moves and takes a blue piece")
    public void userMakesFiveLegalMovesAndTakesABluePiece() {
        String[][] moves = {{"space02", "space13"}, {"space11","space02"},
                {"space13", "space35"}, {"space22","space13"},
                {"space42", "space24"}};

        for (String[] move : moves) {
            WebElement myPiece = Driver.getDriver().findElement(By.name(move[0]));
            Flow.wait(2000);
            myPiece.click();

            WebElement mySpace = Driver.getDriver().findElement(By.name(move[1]));
            Flow.wait(2000);
            mySpace.click();

            WebElement message = Driver.getDriver().findElement(By.id("message"));

            Flow.wait(2000);

            Wait<WebDriver> wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(5));
            wait.until(d -> message.getText().equals("Make a move."));
        }

    }

    @Then("user should be able to take the next step before each move")
    public void userShouldBeAbleToTakeTheNextStepBeforeEachMove() {
        WebElement message = Driver.getDriver().findElement(By.id("message"));
        String actualMessage = message.getText();
        String expectedMessage = "Make a move.";
        Assert.assertEquals(actualMessage,expectedMessage);
    }


    @And("user should be able to restart the game after five moves")
    public void userShouldBeAbleToRestartTheGameAfterFiveMoves() {
        Driver.getDriver().navigate().refresh();
        WebElement message = Driver.getDriver().findElement(By.id("message"));
        String actualMessage = message.getText();
        String expectedMessage = "Select an orange piece to move.";
        Assert.assertEquals(actualMessage,expectedMessage);
    }



}
