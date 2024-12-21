package flows;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.google.gson.Gson;

import FlyModules.BrowserContants;
import FlyModules.flyAdeal;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pageObjects.BaseClass;
import pageObjects.Database;


public class FlyAdealCacheFlow{
	static WebDriver driver;
	private int iTestCaseRow;
	boolean status;
	private Database PnrDetails;
	public static String flyAdealApiUrl;
	
	@Test 
	public void test() throws Exception {
		 
		if (BrowserContants.ENV.equals("PRD")) {
			RestAssured.baseURI = BrowserContants.PRD_API_URL;
			System.out.println(BrowserContants.PRD_API_URL);
			
		} else if (BrowserContants.ENV.equals("STG")) {
			RestAssured.baseURI = BrowserContants.STG_API_URL;
			System.out.println(BrowserContants.STG_API_URL);
		}
		
        LocalTime currentTime = LocalTime.now();
        
        int days;
        int skipdays;
        
        LocalTime startTime = LocalTime.of(23, 0); // 11 PM
        LocalTime endTime = LocalTime.of(5, 0); // 5 AM
        
        // Check if the current time falls within the specified range
        if (currentTime.isAfter(startTime) || currentTime.equals(startTime) ||
            currentTime.isBefore(endTime) && currentTime.isAfter(LocalTime.MIDNIGHT)) {
            days = 5;
            skipdays = 4;
        } else {
        	days = 5;
            skipdays = 4;
        }
	    
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "text/json");
		Response response = request.get("/GetF3Routes?days="+days+"&group=4&skipdays="+skipdays+"");
		System.out.println("Response body: " + response.body().asString());
		String s=response.body().asString();
		System.out.println(s);
		int statusCode = response.getStatusCode();
		System.out.println("The status code recieved: " + statusCode);
		
		Gson g = new Gson();
		Database[] mcArray = g.fromJson(s, Database[].class);
		List<Database> p = Arrays.asList(mcArray);
		for(Database data:p){
			try{
				
				Date depDate=new SimpleDateFormat("dd MMM yyyy").parse(data.DepartureDate);  
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String strDate= formatter.format(depDate);
				System.out.println("strDate :"+strDate);
				flyAdealApiUrl="https://www.flyadeal.com/en/booking/select/?origin1="+data.From.toUpperCase()+"&destination1="+data.To.toUpperCase()+"&departure1="+strDate +"&adt1=1&chd1=0&inf1=0&currency=SAR&utm_source=HomePage";
				//flyAdealApiUrl="https://www.flyadeal.com/en/search-flight/?origin1="+data.From.toUpperCase()+"&destination1="+data.To.toUpperCase()+"&departure1="+strDate +"&adt1=1&chd1=0&inf1=0&currency=SAR&source=airtrfx?utm_source=wego_meta&utm_medium=landingpage&utm_campaign=promomar&utm_content=herobanner";
				
                //String flyAdealApiUrl="https://www.flyadeal.com/en/booking/select?destination1="+data.To+"&inf1=0&currency=SAR&source=airtrfx&chd1=0&adt1=1&origin1="+data.From+"&departure1="+strDate +"";
                //String flyAdealApiUrl="https://www.flyadeal.com/en/booking/select?destination1="+data.To+"&inf1=0&utm_campaign=FlightsFromTabuk_En_8122021&utm_medium=Cover&currency=SAR&source=airtrfx&chd1=0&adt1=1&origin1="+data.From+"&departure1="+strDate +"&utm_source=HomePage";
			     //flyAdealApiUrl="https://www.flyadeal.com/en/search-flight/?origin1=RUH&destination1=JED&departure1=2024-04-01&adt1=1&chd1=0&inf1=0&origin2=JED&destination2=RUH&departure2=2024-04-02&adt2=1&chd2=0&inf2=0&currency=SAR&source=airtrfx?wego_click_id=4081a328199e48acaea02ff9dd5c76c1?utm_source=wego_meta&utm_medium=landingpage&utm_campaign=promomar&utm_content=herobanner";
				
				System.out.println("API URL "+flyAdealApiUrl);
				PnrDetails=data;
				//System.setProperty("webdriver.gecko.driver","D:\\Softwares\\geckodriver.exe"); 
				FirefoxOptions options = new
				FirefoxOptions();  
				options.addPreference("layout.css.devPixelsPerPx", "0.3");
				options.addPreference("permissions.default.image", 2);
				options.addArguments("--headless");
				driver = new FirefoxDriver(options);
				driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
				driver.manage().deleteAllCookies();
				driver.get(flyAdealApiUrl);
				new BaseClass(driver);
				Thread.sleep(10000);
				flyAdeal.FlightDetails2(driver,PnrDetails);
				driver.quit();
				
				}
			
				catch(Exception e)
				{
					
				}
			}
		
		}
	
	

	 @AfterMethod
     public void stop() throws Exception
      {
		 
          if (driver != null) {
		        driver.quit();
		    }
  }

}	
	

		
		


				
				
		
		
			