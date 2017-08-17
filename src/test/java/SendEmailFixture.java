
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.Keys;


@RunWith(ConcordionRunner.class)

public class SendEmailFixture{

  public WebDriver driver;

  public WebDriver getDriver() {
    if (driver == null){
      driver = new ChromeDriver();
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    return driver;
  }

  public String login(){
    String msg = "Successfully logged in - reached inbox";
    driver = this.getDriver();

    driver.get("https://mail.google.com");
    driver.findElement(By.cssSelector("input[type=email]")).sendKeys("sbe.automation@gmail.com");
    driver.findElement(By.cssSelector("div[role=button]")).click();
    driver.findElement(By.name("password")).sendKeys("boguspassword");
    //check for presence of next button - it can appear in different ways
    if (driver.findElements(By.id("passwordNext")).size() != 0) {
      driver.findElement(By.id("passwordNext")).click();
    } else {
      driver.findElement(By.xpath(".//div[contains(text(), 'NEXT')]")).click();
    }

    //wait for inbox to load
    WebDriverWait wait = new WebDriverWait(driver, 10);
    try {
      wait.until(ExpectedConditions.titleContains("Inbox"));
    } catch (TimeoutException timeout) {
      driver.close();
      msg = "Didn't get to Inbox";
    }

    return msg;
  }


  public String sendEmail(String to, String subject, String body, String outcomeMessage){
    String returnValue = "";
    if (driver == null) {
      login();
    }
    driver = this.getDriver();
    //driver.get("https://mail.google.com");

    //wait until compose button is clickable
    WebDriverWait wait = new WebDriverWait(driver, 10);
    try {
      wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[gh=cm]")));
    } catch (TimeoutException timeout) {
      driver.close();
      returnValue = "Compose button not clickable";
    }

    //click compose button
    driver.findElement(By.cssSelector("div[gh=cm]")).click();

    //wait for compose screen to show up (wait until we can type something in the "to" box)
    try {
      wait.until(ExpectedConditions.presenceOfElementLocated(By.name("to")));
    } catch (TimeoutException timeout) {
      driver.close();
      returnValue = "Couldn't open email compose screen";
    }

    //enter email details
    if (to != null){
      driver.findElement(By.name("to")).sendKeys(to);
    }
    if (subject != null){
      driver.findElement(By.name("subjectbox")).sendKeys(subject);
    }
    if (body != null){
      driver.findElement(By.cssSelector("div[aria-label='Message Body']")).sendKeys(body);
    }

    //send the email (note that for finding the send button, ^= searches for a string beginning with certain text)
    driver.findElement(By.cssSelector("div[aria-label^=Send]")).click();

    //check if the page contains the outcome message that we are looking for
    if(driver.findElements(By.xpath(".//div[contains(text(), '" + outcomeMessage + "')]")).size() != 0) {
      returnValue = outcomeMessage;
    } else {
      returnValue = "Couldn't find the specified outcome message";
    }

    //if there is an alert, close the alert dialog and close the email compose screen
    if(driver.findElements(By.name("ok")).size() != 0) {
      driver.findElement(By.name("ok")).click();
      try {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-label='Message Body']")));
        driver.findElement(By.cssSelector("div[aria-label='Message Body']")).sendKeys(Keys.ESCAPE);
      } catch (TimeoutException timeout) {
        returnValue = "Couldn't close email compose screen";
      }
    }

    //driver.close();
    return returnValue;
  }
}
