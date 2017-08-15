import java.util.concurrent.TimeUnit;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

@RunWith(ConcordionRunner.class)
public class LoginFixture{

  WebDriver driver;

  public void initialiseLogin(){
    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    driver.get("https://mail.google.com");
  }

  public void enterCredentials(){
    WebElement emailBox = driver.findElement(By.cssSelector("input[type=email]"));
    emailBox.sendKeys("sbe.automation@gmail.com");
    WebElement nextButton = driver.findElement(By.cssSelector("div[role=button]"));
    nextButton.click();
    WebElement passwordBox = driver.findElement(By.name("password"));
    passwordBox.sendKeys("boguspassword");
    WebElement signInButton = driver.findElement(By.id("passwordNext"));
    signInButton.click();
  }

  public String checkContentOfPage(){
    String pageTitle = driver.getTitle();
    driver.quit();
    return pageTitle;
  }
}
