package ecommerce;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.ResPayloadCreateProduct;
import resources.EcomUtils;
import resources.ReadExcelData;
import resources.TestDataBuild;

public class ParallelTestsUsingExcelData extends EcomUtils {
	public static String user_token = null;
	public static String user_id = null;
	public static String productId = null;
	public static RequestSpecification reqSpec =null;
	public static String testDataFile = null;

	/*
	 * 
	 * https://rahulshettyacademy.com/client/auth/login
	 * 
	 * "userEmail": "clare.kavalam@gmail.com", "userPassword": "Test*01cv"
	 * "userEmail": "clarelearning@gmail.com", "userPassword": "Test*01cg"
	 * 
	 * 
	 * }
	 */
	
	@Test(priority =1, dataProvider = "dataEnd2EndTest")
	public  void callEnd2EndTest(String userEmail,
							String userPassword ,
							String productName, 
							String productSubCategory, 
							String productPrice) throws IOException {
		// TODO Auto-generated method stub
		
		/*1. One Java file to build request and response specifications:
		 * 	 Request Specification SETs the following
		 * 		- URI
		 * 		- Content-Type
		 * 		- SessionFilter
		 *  	- CookieFilter
		 *   	- RequestLoggingFilter
		 *   	- ResponseLoggingFilter	
		 * 		- Finally build()
		 * 
		 * 	Response Specification EXPECTs the following
		 * 		- Content-Type
		 * 		- Content-Length .header("Content-Length", Integer::parseInt, greaterThan(0))
		 * 		- status code
		 * 2. Another Java file for Payloads to be sent in the request body OR extracted from response body 
		 * 		a) In Request - a.raw JSON  b.json File c.Serialized POJO class
		 * 				
		 * 		b) Response - extracted from the the body of response
		 */
		
	

		reqSpec = given().spec(requestSpecification())
				//.body(TestDataBuild.reqPayloadLoginRawJSON()); // payload send as raw JSON
				.body(TestDataBuild.reqPayloadLoginPOJOJSON(userEmail, userPassword));  // payload send as serialized POJO class
		
		Response resLogin = reqSpec.when().post(getPropertyValue("Login")).then().spec(responseSpecification()).extract()
				.response();
		JsonPath jresLogin = new JsonPath(resLogin.asString());
		user_token = jresLogin.getString("token").toString();
		user_id = jresLogin.getString("userId").toString();

		Assert.assertEquals("Login Successfully", jresLogin.getString("message").toString());
		System.out.println(userEmail + " logged in successfully");

//////////////////////////
		System.out.println("user_token:"+ user_token + " \t user_id:" + user_id);
		
		reqSpec = given()
				.spec(requestSpecification())
				.header("authorization" ,user_token)
				.contentType(ContentType.MULTIPART)
				.formParams(TestDataBuild.getFormdataForCreateProduct(
						productName, user_id, productSubCategory, productPrice))
				.multiPart("productImage",new File("C:\\Users\\clare\\OneDrive\\Pictures\\Screenshots\\NewBook.jpeg"));

		
		Response resCreateProduct = reqSpec
				.when().post(getPropertyValue("CreateProduct"))
				.then().spec(responseCreateProductSpecification())
				.header("Content-Length", Integer::parseInt, greaterThan(0))
				.extract().response();
		
		ResPayloadCreateProduct  resPayloadCreateProduct = new ResPayloadCreateProduct();
		resPayloadCreateProduct = resCreateProduct.as(ResPayloadCreateProduct.class);
		productId = resPayloadCreateProduct.getProductId();
		Assert.assertEquals("Product Added Successfully", resPayloadCreateProduct.getMessage());
		System.out.println(userEmail + " created product successfully");

/////////////////////////////////////////////////
		
		reqSpec= 	given()
					.spec(requestSpecification())
					.header("authorization" ,user_token)
					.contentType(ContentType.ANY)
					.pathParam("productId", productId);
		
		Response resDeleteProductId = reqSpec
										.when()
											.delete(getPropertyValue("DeleteProduct"))
										.then()
											.spec(responseSpecification())
											.header("Content-Length", Integer::parseInt, greaterThan(0))
											.extract().response();
		
		JsonPath jresDeleteProductId = new JsonPath(resDeleteProductId.asString());
		
		Assert.assertEquals("Product Deleted Successfully", jresDeleteProductId.getString("message"));
		System.out.println(userEmail + " deleted product successfully");

	}
	@BeforeTest
	public void test() throws IOException {
		testDataFile = getPropertyValue("testDataFile");
		System.out.println("*********BEFORE*************");
	};

	@DataProvider(name = "dataEnd2EndTest")
	public Object[][] dataEnd2EndTest() throws IOException {
		
		System.out.println("*********IN DATAPROVIDER METHOD*************");
		Object[][] testdata = ReadExcelData.readExcelData("End2EndTest", testDataFile);
		return testdata;
	 
	}
	
	@DataProvider(name = "dataLogin")
	public Object[][] dataLogin() throws IOException {
		Object[][] testdata = ReadExcelData.readExcelData("Login", testDataFile);
		return testdata;
	}
	
	@DataProvider(name = "dataCreateProduct")
	public Object[][] dataCreateProduct() throws IOException {
		Object[][] testdata = ReadExcelData.readExcelData("CreateProduct", testDataFile);
		/*
		 * return new Object[][] { {"clare.kavalam@gmail.com", "Test*01cv"}, {
		 * "clarelearning@gmail.com","Test*01cg"} };
		 */
		return testdata;
	}
}
