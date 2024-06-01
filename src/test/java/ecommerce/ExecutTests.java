package ecommerce;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
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
import resources.TestDataBuild;

public class ExecutTests extends EcomUtils {
	public static String user_token = null;
	public static String user_id = null;
	public static String productId = null;
	public static RequestSpecification reqSpec =null;

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
	@Test(priority =1)
	public  void callLogin() throws IOException {
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
				.body(TestDataBuild.reqPayloadLoginPOJOJSON());  // payload send as serialized POJO class
		
		Response resLogin = reqSpec.when().post("/api/ecom/auth/login").then().spec(responseSpecification()).extract()
				.response();
		JsonPath jresLogin = new JsonPath(resLogin.asString());
		user_token = jresLogin.getString("token").toString();
		user_id = jresLogin.getString("userId").toString();
		System.out.println("user_token:"+ user_token + " \t user_id:" + user_id);
		Assert.assertEquals("Login Successfully", jresLogin.getString("message").toString());

	}
	@Test(priority =2)
	public void callCreateProduct() throws IOException {
		System.out.println("user_token:"+ user_token + " \t user_id:" + user_id);
		
		reqSpec = given()
				.spec(requestSpecification())
				.header("authorization" ,user_token)
				.contentType(ContentType.MULTIPART)
				.formParams(TestDataBuild.getFormdataForCreateProduct(user_id))
				.multiPart("productImage",new File("C:\\Users\\clare\\OneDrive\\Pictures\\Screenshots\\NewBook.jpeg"));
		
		/*
		 * .param("productName", "item-2") .param("productAddedBy", user_id)
		 * .param("productCategory", "Fashion") .param("productSubCategory", "shirts")
		 * .param("productPrice", "11500") .param("productDescription",
		 * "Addias Originals") .param("productFor", "women") .multiPart("productImage",
		 * new File("C:\\Users\\clare\\OneDrive\\Pictures\\Screenshots\\NewBook.jpeg"));
		 */
		
		Response resCreateProduct = reqSpec
				.when().post("/api/ecom/product/add-product")
				.then().spec(responseCreateProductSpecification())
				.header("Content-Length", Integer::parseInt, greaterThan(0))
				.extract().response();
		
		ResPayloadCreateProduct  resPayloadCreateProduct = new ResPayloadCreateProduct();
		resPayloadCreateProduct = resCreateProduct.as(ResPayloadCreateProduct.class);
		productId = resPayloadCreateProduct.getProductId();
		Assert.assertEquals("Product Added Successfully", resPayloadCreateProduct.getMessage());
		
		
		
				
	}
	@Test(priority =3)
	public void callDeleteProductId() throws IOException {
		
		reqSpec= 	given()
					.spec(requestSpecification())
					.header("authorization" ,user_token)
					.contentType(ContentType.ANY)
					.pathParam("productId", productId);
		
		Response resDeleteProductId = reqSpec
										.when()
											.delete("/api/ecom/product/delete-product/{productId}")
										.then()
											.spec(responseSpecification())
											.header("Content-Length", Integer::parseInt, greaterThan(0))
											.extract().response();
		
		JsonPath jresDeleteProductId = new JsonPath(resDeleteProductId.asString());
		
		Assert.assertEquals("Product Deleted Successfully", jresDeleteProductId.getString("message"));
		
	}


}
