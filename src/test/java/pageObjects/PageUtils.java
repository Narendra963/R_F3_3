package pageObjects;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import FlyModules.BrowserContants;
import FlyModules.FadFlightResponse;



public class PageUtils {
	
	public static String uniqueMailID;

	
	
	/**
	 * This method waits for the specified element to load
	 * 
	 * @param element
	 */
	public static void waitForElementToLoad(WebDriver driver, WebElement element) {
		long timeoutInSeconds = 60;
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)); 
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	/**
	 * Wait for page load 
	 * 
	 * @param driver
	 */
	public static void waitForPageLoad(WebDriver driver) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); 
		wait.until(pageLoadCondition);
	}

//	public static void clickElementForFluent(WebDriver driver, WebElement element) {
//		//long timeoutInSeconds = 60;
//		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
//			    .withTimeout(60, TimeUnit.SECONDS)
//			    .pollingEvery(5, TimeUnit.SECONDS)
//			    .ignoring(NoSuchElementException.class);
//		//WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
//		element = wait.until(ExpectedConditions.elementToBeClickable(element));
//		//waitForElementToLoad(driver, element);
//		element.click();
//		//waitForPageLoad(driver);
//	}
	public static void clickElement(WebDriver driver, WebElement element) {
		long timeoutInSeconds = 30;
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)); 
		element = wait.until(ExpectedConditions.elementToBeClickable(element));
		waitForElementToLoad(driver, element);
		element.click();
		waitForPageLoad(driver);
	}
	
	
	/**
	 * This method sends the text to the specified WebElement after clearing it
	 * 
	 * @param driver
	 * @param element
	 * @param text
	 */
	public static void sendKeysAfterClearingElement(WebDriver driver, WebElement element, String text) {
		waitForElementToLoad(driver, element);
		element.clear();
		element.sendKeys(text);
	}
	/**
	 * This method is used to wait for fixed time
	 * 
	 * @throws InterruptedException
	 */
	public static  void waitForFixedTime(String waitTime) throws InterruptedException {
		if (waitTime.equalsIgnoreCase(BrowserContants.WAIT_VERY_SMALL_ENGINE) ) {
			Thread.sleep(1000);
		} else if (waitTime.equalsIgnoreCase(BrowserContants.WAIT_SMALL_ENGINE)) {
			Thread.sleep(2000);
		} else if (waitTime.equalsIgnoreCase(BrowserContants.WAIT_VERY_SMALL)) {
			Thread.sleep(3000);
		} else if (waitTime.equalsIgnoreCase(BrowserContants.WAIT_SMALL)) {
			Thread.sleep(5000);
		} else if (waitTime.equalsIgnoreCase(BrowserContants.WAIT_MEDIUM)) {
			Thread.sleep(10000);
		} else if (waitTime.equalsIgnoreCase(BrowserContants.WAIT_LONG)) {
			Thread.sleep(15000);
		} else if (waitTime.equalsIgnoreCase(BrowserContants.WAIT_VERY_LONG)) {
			Thread.sleep(25000);
		} else if (waitTime.equalsIgnoreCase(BrowserContants.WAIT_EXCEPTIONAL_CASES)) {
			Thread.sleep(45000);
		}
	}
	
	/**
	 * This method is used to check if the element is displayed
	 * 
	 * @param driver
	 * @param element
	 * @return boolean
	 */
	public static boolean isElementDisplayed(WebDriver driver, WebElement element) {
		boolean status = false;
		waitForElementToLoad(driver, element);
		status = element.isDisplayed();
		return status;
	}
	public static boolean isElementLocated(WebDriver driver, By by) {
		boolean status = false;
		try {
			waitForElementToLoadByLocator(driver, by);
			status = driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			status = false;
		}
		return status;
	}
	public static void waitForElementToLoadByLocator(WebDriver driver, By by) {
		long timeoutInSeconds = 80;
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)); 
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}
	public static void urlChangeTime(WebDriver driver,String text){
		long timeoutInSeconds = 60;
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)); 
	wait.until(ExpectedConditions.urlContains(text));
	}
	
	
	public static boolean isElementVisibil(WebDriver driver,  WebElement element) {
		boolean status = false;
		try {
			waitForElementToVisibility(driver, element);
			status = element.isDisplayed();
		} catch (Exception e) {
			status = false;
		}
		return status;
	}public static void waitForElementToVisibility(WebDriver driver, WebElement element) {
		long timeoutInSeconds = 60;
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)); 
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	/**
	 * This method waits for the specified text to be present in the element
	 * 
	 * @param driver
	 * @param element
	 * @param textToAppear
	 */
	public static void waitForElementTextToBe(WebDriver driver, WebElement element, String textToAppear) {
		long timeoutInSeconds = 100;
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)); 
		wait.until(ExpectedConditions.textToBePresentInElement(element, textToAppear));
	}

	public static void scrollDownExact(WebDriver driver, WebElement element) {
		 JavascriptExecutor js = (JavascriptExecutor) driver;
		 js.executeScript("arguments[0].scrollIntoView();", element);
	}
	public static void scrollDown(WebDriver driver)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,500)");
		
		/*JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollTo(0, document.body.scrollHeight);");*/
	}
	public static void scrollDown2(WebDriver driver)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,50)");
		
	}
	
	public static void scrollDown3(WebDriver driver)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,75)");
	}
	
	public static void scrollDown4(WebDriver driver)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,100)");
		
	}
	
	public static void scrollUp(WebDriver driver)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,-3000)");
		
	
	}
	

	public static void scrollUp1(WebDriver driver)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,-100)");
		
	
	}
	
	 private static String[] Countries = {"Sudan","Yemen","Syria","Egypt","Kuwait","Chad","Oman","Bahrain","United Arab Emirates","Bangladesh","Jordan","Philippines","Serbia","South Korea","Spain","Morocco","Kazakhstan"};
	 
	 
	 private static Random rand = new Random();

	   public static String generateFirstName() {
         
		   return Countries[rand.nextInt(Countries.length)];
	   } 
	   
	   public static String getCountry() {
		   uniqueMailID = generateFirstName();
			//System.out.println("uniqueMailId: " + uniqueMailID);
			return uniqueMailID;
		}
	
	   public static void getScreenShot(FadFlightResponse from,WebDriver driver) throws IOException
	   {
	  	   File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	       FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "/src/main/java/ScreenShot/"+from+"_"+timestamp()+".png"));
	  		
	   }
	   
	   public static void scrollUP(WebDriver driver)
	   {
	   JavascriptExecutor js = (JavascriptExecutor) driver;
	   js.executeScript("window.scrollBy(0,-500)");
	   }
	   
	   public static String timestamp() {
	       return new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
	  }
	   
	   public static String TodayName() {
	  	 Calendar cal = Calendar.getInstance();
	  	 cal.add(Calendar.DATE, 0);
	  	  return new SimpleDateFormat("EEEE").format(cal.getTime());
	  	 
	  	}
	   static String parent;
	   static String child;
	   public static void switchWindows(WebDriver driver) {
			Set<String> set1 = driver.getWindowHandles();
			Iterator<String> win1 = set1.iterator();
			parent = win1.next();
			child = win1.next();
			driver.switchTo().window(child);
		}

		public static void switchToParentWindow(WebDriver driver) {
			driver.switchTo().window(parent);
		}
}
