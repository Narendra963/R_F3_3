package FlyModules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;



public class ApiMethods {
	
	@Test
	
	public static void sendResults(String Currency,String F,String T,String DD,List<FadFlightDetails> FlightDetails) throws InterruptedException, IOException
	{
	
		
		if(BrowserContants.ENV.equals("PRD"))
		{
			RestAssured.baseURI =BrowserContants.PRD_API_URL;
			//RestAssured.baseURI ="https://scrappingapi.rehlat.co/v1/scraping/";
		}
		else if(BrowserContants.ENV.equals("STG"))
		{
			RestAssured.baseURI =BrowserContants.STG_API_URL;
		}
		
		
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "text/json");
		FadFlightResponse result = new FadFlightResponse();
		result.From =F;
		result.To = T;
		result.Currency = Currency;
		result.DepartureDate = DD;
		
		
		result.FlightDetails = FlightDetails;
		Gson gson = new Gson();
		
	
		request.body(gson.toJson(result));
		System.out.println(gson.toJson(result));
		Response response = request.post("/SaveF3SrpToCache");
		System.out.println("Response body: " + response.body().asString());
		String s=response.body().asString();
		System.out.println(s);
		int statusCode = response.getStatusCode();
		System.out.println("The status code recieved: " + statusCode);
		
		
	}

}
