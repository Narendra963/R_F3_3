package FlyModules;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import flows.FlyAdealCacheFlow;
import pageObjects.Database;
import pageObjects.PageUtils;

public class flyAdeal extends FlyAdealCacheFlow  {
	
	
	static String Depdate=null;
	static String Currency=null;
	static String Year=null;
	
	private static String getCurrentDateFormatted(String format) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return currentDate.format(formatter);
    }

    // Helper method to get the date after a certain number of days in the specified format
    private static String getDateAfterDaysFormatted(int days, String format) {
        LocalDate futureDate = LocalDate.now().plusDays(days);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return futureDate.format(formatter);
    }
    
	public static void search(WebDriver driver) throws Exception
	{

		driver.get("https://accounts.google.com/v3/signin/identifier?dsh=S873427101%3A1670174877878096&continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&flowEntry=ServiceLogin&flowName=GlifWebSignIn&rip=1&sacu=1&service=mail&ifkv=ARgdvAv7qIg9j-X7zxwLWrETGRTaquhiB_tbb7YW19ONpQZ-z4IHi9LknQfITZIbwMLY0zXURVL5jg");
	    Thread.sleep(1500);
		
		
	}
    
    public static void FlightDetails2(WebDriver driver, Database PnrDetails) throws Exception {
        String date;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25)); // Set the maximum wait time to 60 seconds
		boolean isPageLoaded = false;
		int maxAttempts = 2;
		int attempt = 1;

		while (!isPageLoaded && attempt <= maxAttempts) {
		    try {
		        // Wait for the page to load completely
		        isPageLoaded = wait.until(ExpectedConditions.urlContains("https://www.flyadeal.com/en/select-flight"));
		    } catch (Exception e) {
		   
		    	try {
		    	driver.manage().deleteAllCookies();
		    	search(driver);
		    	driver.manage().deleteAllCookies();
		        // Refresh the page
		        driver.get(flyAdealApiUrl);
		        Thread.sleep(10000);
		        System.out.println("Cookies deleted. Page refreshed.");
		        }
		    	catch (Exception e1) {
		    		try {
		    		 isPageLoaded = wait.until(ExpectedConditions.urlContains("https://www.flyadeal.com/en/select-flight"));
		    		}
		    		catch (Exception e2) {
				    	driver.manage().deleteAllCookies();
				    	search(driver);
				    	driver.manage().deleteAllCookies();
				        // Refresh the page
				        driver.get(flyAdealApiUrl);
				        Thread.sleep(10000);
				        System.out.println("Cookies deleted. Page refreshed.");
				        }
		    	}
		    	
		    }

		    attempt++;
		}
        
        try {
            
            driver.findElement(By.cssSelector("div.select_date_previous")).click();
            Thread.sleep(1000);

            for (int weekOffset = 0; weekOffset < 7; weekOffset++) {
                for (int dayOffset = 1; dayOffset <= 7; dayOffset++) {
                    int totalOffset = weekOffset * 7 + dayOffset;

                    if (totalOffset > 45) {
                        break; // Exit the loop if the total days processed exceed 70
                    }

                    Depdate = getDateAfterDaysFormatted(totalOffset - 1, "dd MMM yyyy");
                    //System.out.println("Processing for date: " + Depdate);

                    driver.findElement(By.xpath("//app-trip-one-way/div/div[1]/div[2]/div[" + dayOffset + "]")).click();
                    Thread.sleep(1000);
                    String DepDate=driver.findElement(By.xpath("//app-journey-one-way/section/app-trip-one-way/div/div[1]/div[2]/div["+dayOffset+"]/div/strong")).getText().replace("month.", "");
                    System.out.println(DepDate);
                    String[] dateParts = DepDate.split("\\W+");
                    String day = dateParts[0];
                    String monthAbbreviation = dateParts[1];
                    
                    if (monthAbbreviation.equals("Nov") || monthAbbreviation.equals("Dec")) {
                    	Year = "2024";
                    } else {
                    	Year = "2025";
                    }
                    String Departdate = String.format("%s %s %s", day, monthAbbreviation, Year);

                    //System.out.println("SRP Date: " + Departdate);
                    
                    //Thread.sleep(2000);
                    
                    Depdate = getDateAfterDaysFormatted(totalOffset - 1, "dd MMM yyyy");
                    //System.out.println("System Date: " + Depdate);
                    
                    if (Depdate.equals(Departdate)) {
                    	Depdate=Departdate;
                    } 
                    else {
                    	Depdate=Departdate;
                    }
                    String websiteDate = driver.findElement(By.xpath("//span[contains(text(),'Passenger')]")).getText();
                    date = websiteDate.split("\\|")[0].trim();
                    Currency = driver.findElement(By.cssSelector("span.currency.ng-star-inserted")).getText().replaceAll(" ", "");
                    
                    String F3Flights=driver.findElement(By.xpath("//app-journey-one-way[1]/section[1]/app-trip-one-way[1]/app-journey-fare-details[1]/div[2]")).getText().replaceAll(" ", "").replaceAll("journeyFareDetails-Popup.", "");
                    //System.out.println(F3Flights);
                    
                    if (F3Flights.contains("Noflightsavailable") || F3Flights.contains("journeyFare.para1")) {
                    	System.out.println("No Flights");
    	                String From = PnrDetails.From;
    	                String To = PnrDetails.To;
    	                ApiMethods.sendResults(Currency, From, To, Depdate, new ArrayList<FadFlightDetails>());
                        
                    }
                    else {
                    	
                    FlightDetailsSending(driver, PnrDetails);
                    	 
                    }

                    /*List<WebElement> flightDetails = driver.findElements(By.xpath("//div[@class='flight_details_wrap']"));
                    //System.out.println("Total Flights :" + flightDetails.size());

                    if (flightDetails.size() == 0) {
                    	System.out.println("No Flights");
    	                String From = PnrDetails.From;
    	                String To = PnrDetails.To;
    	                ApiMethods.sendResults(Currency, From, To, Depdate, new ArrayList<FadFlightDetails>());
                        // Handle no flights scenario
                    } else {
                        FlightDetailsSending(driver, PnrDetails);
                    }*/

                    // If it's the last iteration of the inner loop and not the last week, click on the "Next" button
                    if (dayOffset == 7 && weekOffset < 6) {
                        driver.findElement(By.cssSelector("div.select-date-range.next-date-range")).click();
                        Thread.sleep(1000);
                    }
                    
                }
            }
        } catch (Exception e) {
            // Handle exceptions
        }
    }



	
	public static void FlightDetailsSending(WebDriver driver,Database PnrDetails) throws Exception
	{
		//flyadealPage.flight_Details();
		String DataChange=null;
		String date = null;
		String month = null;
		String year = null;
		String FlightNum = null;
		String JournyTimeHours = null;
		 String JournyTimeMin=null;
		 String EndTime=null;
		 String From=PnrDetails.From;
		 String To=PnrDetails.To;
		 
		 String flySeatNum="99";
		 String flyFare=null;
		 String FlyplusFare=null;
		 String StartTerminal=null;
		 String EndTerminal=null;
		 
		 List<WebElement> elementF= null;
		        elementF = driver.findElements(By.cssSelector("div.flight_details_wrap"));
	            System.out.println("Total Flights :" +elementF.size());

		 String flyPlusSeatNum="99";
		 String Sold=null;
		 List<FadFlightDetails> finalList =  new ArrayList<FadFlightDetails>();
		try {
			String ele = null;
			List<WebElement> element = driver.findElements(By.cssSelector("div.flight_details_wrap"));
			
			 for (WebElement e1 : element) {
					 ele = e1.getText();
					 //System.out.println(ele);
					 FadFlightDetails currentFlightFly = new FadFlightDetails();
					 FadFlightDetails currentFlightFlyPlus = new FadFlightDetails();
					 
                     //String str1=ele.replaceAll("[\r\n]+", " ").replace(",", "");
					 String str1=ele.replaceAll("[\r\n]+", " ").replace(",", "").replace("F3 ", "F3").replace("Promo Applied from SAR  ", "from ");
					 
					 String s=str1.replaceAll("Select Fare","").replaceAll("Your #flyforless flight ","").replaceAll("Sharm El Sheikh", "SharmElSheikh").replaceAll("Terminal[1-9] ", "").replaceAll("From ", "").replaceAll("journeyFareDetails-Popup.", "").replaceAll("Dubai Airport ", "Dubai ").replaceAll("Soldout", "Sold Out").replaceAll("Dubai Al Maktoum Airport", "DubaiAlMaktoumAirport").replaceAll("Dubai Al Maktoum", "DubaiAlMaktoum").replaceAll("SAR", " SAR ");
					 String Str = new String(s);
				        
				      
					 //driver.get("https://www.google.com/");
				     
				      if(Str.matches("(.*)Sold Out(.*)"))
				      {
				    	  //07:55 Jeddah JED F3303 2h 5m 10:00 Dammam DMM Soldout
				    	  System.out.println(s);
				      }
				      else {
				    	  //System.out.println("Gopi:"+s);
				     //05:00 Riyadh RUH F3101 1h 55m 06:55 Jeddah JED  SAR 249.00
				     //01:10 Riyadh RUH F3159 1h 45m 02:55 Jeddah JED SAR 1149.00
				     //01:10 Riyadh RUH F3159 1h 45m 02:55 Jeddah JED from SAR 1149.00
					 //22:40 F34237 00:40 1 Tabuk TUU 2h Riyadh RUH From SAR1029.00
				     //23:05 F34692 00:50 1 Riyadh RUH 1h 45m Jeddah JED From SAR239.00 
					 //23:05 F34797 00:10 1 Dammam DMM 1h 5m Terminal 5 Riyadh RUH From SAR199.00 
					 //12:30 F34778 13:35 1 Riyadh RUH Terminal 5 1h 5m Dammam DMM From SAR369.00 
					// 00:35 F34698 02:20 1 Riyadh RUH Terminal 5 1h 45m Terminal T1 Jeddah JED From SAR279.00 
					
					System.out.println(s);
					
					String StartTime = s.split(" ")[0];
			        FlightNum = s.split(" ")[3];
			        EndTime = s.split(" ")[6];
			        //Currency = driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Total:'])[1]/following::span[1]")).getText().replaceAll(" ", "");
			        // Check if the string contains "+1" or "Sold out"
			        boolean isDayChange = s.contains("+1");
			        boolean isSoldOut = s.contains("Sold Out");

			        From = s.split(" ")[2];
			        JournyTimeHours = s.split(" ")[4].replace("h", "");
			        JournyTimeMin = s.split(" ")[5].replace("m", "");
			        To = isDayChange ? s.split(" ")[9] : s.split(" ")[8];
			        flyFare = isSoldOut ? "Sold Out" : s.split(" ")[s.split(" ").length - 1];
		            
					
			        if (isDayChange) {
			        	DataChange="1";
			        }
			        else {
			        	
			        }
			        
			        if (isSoldOut) {
			        	flyFare="00.0" ;
			        } 
			        
					
				    
					     
					/*System.out.println("From:"+From);
					System.out.println("To:"+To);
					
					System.out.println("DepartureDate:"+Depdate);
					System.out.println("Currency:"+Currency);
					System.out.println("FareType :fly");
					
					System.out.println("FlightNumber:"+FlightNum);
					System.out.println("Class :Economy");*/
					int Hours = Integer.parseInt(JournyTimeHours);	
					int TotalMin=Hours * 60;
					
					int Min = Integer.parseInt(JournyTimeMin);	
					int Total=TotalMin+Min;
					String JournyTimeTotal=Integer.toString(Total);
					//System.out.println("JourneyTime:"+Total);
					
					
					int add = 125; 
					if (From.equals("CAI")) {
						 add=200;
						} else if (To.equals("CAI")) {
							 add=200;
						} else {
							if (From.equals("AMM")) {
								 add=200;
								} else if (To.equals("AMM")) {
									 add=200;
								} else {
									if (From.equals("KRT")) {
										 add=200;
										} else if (To.equals("KRT")) {
											 add=200;
										}else {
											if (From.equals("SSH")) {
												 add=200;
											} else if (To.equals("SSH")) {
												 add=200;
											}else{
												 add=125;
					                     	}
						             } 
								}
							}
					
					 float flyPlus=Float.parseFloat(flyFare);
					 double flyfareadd=add+flyPlus;
					  FlyplusFare=Double.toString(flyfareadd);
					 //System.out.println(FlyplusFare);
					 if(flyFare.equals("00.0"))
					 {
						 FlyplusFare="00.0";
					 }
					
					/*System.out.println("StartTime :"+StartTime);
					System.out.println("EndTime :"+EndTime);
					System.out.println("StartDate:"+Depdate);
					System.out.println("EndDate:Null");
					System.out.println("Start Airport:"+From);
					System.out.println("End Airport:"+To);
					System.out.println("Fly Fare:"+flyFare);
					System.out.println("FlyPlus Fare:"+FlyplusFare);
					System.out.println("Fly Seats:"+flySeatNum);
					System.out.println("FlyPlus Seats:"+flyPlusSeatNum);
					
					
					
					System.out.println("Adult Baggage : 25 Kg");
					System.out.println("Child Baggage : 25 Kg");
					System.out.println("Infant Baggage : 0");
					System.out.println("AdultBasePrice:"+flyFare);
					System.out.println("AdultBasePrice:"+FlyplusFare);
					
					
					System.out.println("----------------------------------------");*/
					


					
					currentFlightFlyPlus.FareType=currentFlightFly.FareType="fly";
					currentFlightFlyPlus.Class=currentFlightFly.Class="Economy";
					currentFlightFlyPlus.StartAirp = currentFlightFly.StartAirp =From;
					currentFlightFlyPlus.EndAirp=currentFlightFly.EndAirp=To;
					currentFlightFlyPlus.StartDt=currentFlightFly.StartDt=Depdate;
					currentFlightFlyPlus.ADTBG=currentFlightFly.ADTBG="";
					currentFlightFlyPlus.CHDBG=currentFlightFly.CHDBG="";
					currentFlightFlyPlus.INFBG=currentFlightFly.INFBG="";
					currentFlightFlyPlus.DayChg=currentFlightFly.DayChg = DataChange;
					currentFlightFlyPlus.Fltnum=currentFlightFly.Fltnum=FlightNum;
					currentFlightFlyPlus.JrnyTm=currentFlightFly.JrnyTm=JournyTimeTotal;
					currentFlightFlyPlus.StartTm=currentFlightFly.StartTm=StartTime;
					currentFlightFlyPlus.EndTm=currentFlightFly.EndTm=EndTime;
					currentFlightFlyPlus.NoOfSeats=currentFlightFly.NoOfSeats="99";
					currentFlightFlyPlus.StartTerminal=currentFlightFly.StartTerminal=StartTerminal;
					currentFlightFlyPlus.EndTerminal=currentFlightFly.EndTerminal=EndTerminal;
					currentFlightFlyPlus.AdultBasePrice=currentFlightFly.AdultBasePrice=flyFare.replace(",", "");
					currentFlightFlyPlus.AdultTaxes=currentFlightFly.AdultTaxes ="";
					currentFlightFlyPlus.ChildBasePrice=currentFlightFly.ChildBasePrice=flyFare.replace(",", "");
					currentFlightFlyPlus.ChildTaxes=currentFlightFly.ChildTaxes="";
					if (From.equals("CAI")) {
						currentFlightFlyPlus.InfantBasePrice=currentFlightFly.InfantBasePrice ="250";
						} else if (To.equals("CAI")) {
							currentFlightFlyPlus.InfantBasePrice=currentFlightFly.InfantBasePrice ="250";
						} else {
							if (From.equals("AMM")) {
								currentFlightFlyPlus.InfantBasePrice=currentFlightFly.InfantBasePrice ="65";
								} else if (To.equals("AMM")) {
									currentFlightFlyPlus.InfantBasePrice=currentFlightFly.InfantBasePrice ="345";
								} else {
									if (From.equals("IST")) {
										currentFlightFlyPlus.InfantBasePrice=currentFlightFly.InfantBasePrice ="34";
										} else if (To.equals("IST")) {
											currentFlightFlyPlus.InfantBasePrice=currentFlightFly.InfantBasePrice ="125";
										}else {
											if (From.equals("SSH")) {
											currentFlightFlyPlus.InfantBasePrice=currentFlightFly.InfantBasePrice ="250";
											} else if (To.equals("SSH")) {
												currentFlightFlyPlus.InfantBasePrice=currentFlightFly.InfantBasePrice ="250";
											}else{
							                currentFlightFlyPlus.InfantBasePrice=currentFlightFly.InfantBasePrice ="125";
					                     	}
						             } 
								}
							}
					//currentFlightFlyPlus.InfantBasePrice=currentFlightFly.InfantBasePrice ="90";
					
					
					currentFlightFlyPlus.InfantTaxes=currentFlightFly.InfantTaxes="";
					currentFlightFlyPlus.TotalApiFare=currentFlightFly.TotalApiFare="";
					
					
					
					finalList.add(currentFlightFly);
					
					currentFlightFlyPlus.FareType="fly+";
					currentFlightFlyPlus.ADTBG="25 Kg";
					currentFlightFlyPlus.CHDBG="25 Kg";
					currentFlightFlyPlus.NoOfSeats=flyPlusSeatNum;
					currentFlightFlyPlus.AdultBasePrice=FlyplusFare.replace(",", "");
					currentFlightFlyPlus.AdultTaxes="";
					currentFlightFlyPlus.ChildBasePrice=FlyplusFare.replace(",", "");
					currentFlightFlyPlus.ChildTaxes="";
					
					if (From.equals("CAI")) {
						currentFlightFlyPlus.InfantBasePrice="250";
						} else if (To.equals("CAI")) {
							currentFlightFlyPlus.InfantBasePrice="250";
						} else {
							if (From.equals("AMM")) {
								currentFlightFlyPlus.InfantBasePrice="65";
								} else if (To.equals("AMM")) {
									currentFlightFlyPlus.InfantBasePrice="345";
								} else {
									if (From.equals("IST")) {
										currentFlightFlyPlus.InfantBasePrice="34";
										} else if (To.equals("IST")) {
											currentFlightFlyPlus.InfantBasePrice="125";
										} else {
											if (From.equals("SSH")) {
												currentFlightFlyPlus.InfantBasePrice="250";
												} else if (To.equals("SSH")) {
													currentFlightFlyPlus.InfantBasePrice="250";
												} else {
													currentFlightFlyPlus.InfantBasePrice="125";
						
								}
						  
							}
				      }
				}
					//currentFlightFlyPlus.InfantBasePrice="90";
					
					//finalList.add(currentFlightFlyPlus);
					
			 }
			 }
		}
		
			
			 catch (Exception e) {
			
		}
		
		try {
			ApiMethods.sendResults(Currency,From, To,Depdate, finalList);
			
		}
		catch (Exception e) {
			System.out.println("Data not updated");
		}
		
	}

}




