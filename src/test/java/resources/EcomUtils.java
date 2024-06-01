package resources;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class EcomUtils {

	public static RequestSpecification reqBaseLogin;
	public static  ResponseSpecification resBaseLogin;
	public RequestSpecification requestSpecification() throws IOException {
		
		if (reqBaseLogin == null) {
			
			PrintStream log = new PrintStream(new FileOutputStream(getPropertyValue("logFile")));
			CookieFilter cookie = new CookieFilter();
			SessionFilter session = new SessionFilter();
			
			
			 reqBaseLogin = new RequestSpecBuilder().setBaseUri(getPropertyValue("BaseURI"))
					 .setRelaxedHTTPSValidation()
					.addFilter(session)
					.addFilter(cookie)
					.addFilter(RequestLoggingFilter.logRequestTo(log))
					.addFilter(ResponseLoggingFilter.logResponseTo(log))
					
					.setContentType(ContentType.JSON).build();
			return reqBaseLogin;
		}
		return reqBaseLogin;

	}

	public ResponseSpecification responseSpecification() {
		resBaseLogin = new ResponseSpecBuilder().expectStatusCode(200)
				.expectContentType(ContentType.JSON).build();
		return resBaseLogin;
	}
	

	public ResponseSpecification responseCreateProductSpecification() {
		resBaseLogin = new ResponseSpecBuilder().expectStatusCode(201)
				.expectContentType(ContentType.JSON).build();
		return resBaseLogin;
	}
	
	public static String getPropertyValue(String property) throws IOException {
		
		FileInputStream fis = new FileInputStream("C:\\2.Code\\RestAssured\\ecommerce\\src\\test\\java\\resources\\global.properties");
		
		Properties props = new Properties();
		props.load(fis);
		return props.getProperty(property);
		
	}
}
