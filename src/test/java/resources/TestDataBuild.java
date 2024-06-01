package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import pojo.ReqPayloadLogin;

public class TestDataBuild {
	
	public static String reqPayloadLoginRawJSON () {
		
		return "{\r\n"
				+ "    \"userEmail\": \"clare.kavalam@gmail.com\",\r\n"
				+ "    \"userPassword\": \"Test*01cv\"\r\n"
				+ "}";
	}
	
	public static ReqPayloadLogin reqPayloadLoginPOJOJSON () {
		
		ReqPayloadLogin reqPayloadLogin = new ReqPayloadLogin();
		reqPayloadLogin.setUserEmail("clare.kavalam@gmail.com");
		reqPayloadLogin.setUserPassword("Test*01cv");
		return reqPayloadLogin;
	}
	public static ReqPayloadLogin reqPayloadLoginPOJOJSON (String userEmail, String userPassword) {
			
		ReqPayloadLogin reqPayloadLogin = new ReqPayloadLogin();
		reqPayloadLogin.setUserEmail(userEmail);
		reqPayloadLogin.setUserPassword(userPassword);
		return reqPayloadLogin;
	}
	public static Map<String, ?> getFormdataForCreateProduct(String user_id) {
		// TODO Auto-generated method stub
		
		
		Map<String, Object> formdataForCreateProduct = new HashMap<String, Object>();
		formdataForCreateProduct.put("productName", "item-2");
		formdataForCreateProduct.put("productAddedBy", user_id)  ;
		formdataForCreateProduct.put("productCategory", "Fashion") ;
		formdataForCreateProduct.put("productSubCategory", "shirts") ;
		formdataForCreateProduct.put("productPrice", "11500") ;
		formdataForCreateProduct.put("productDescription", "Addias Originals") ;
		formdataForCreateProduct.put("productFor", "women") ;
		return formdataForCreateProduct;
	}
	
	public static Map<String, ?> getFormdataForCreateProduct(
			 
			String  productName,
			String user_id,
			String productSubCategory,
			String productPrice) {
		// TODO Auto-generated method stub
		
		
		Map<String, Object> formdataForCreateProduct = new HashMap<String, Object>();
		formdataForCreateProduct.put("productName", productName);
		formdataForCreateProduct.put("productAddedBy", user_id)  ;
		formdataForCreateProduct.put("productCategory", "Fashion") ;
		formdataForCreateProduct.put("productSubCategory", productSubCategory) ;
		formdataForCreateProduct.put("productPrice", productPrice) ;
		formdataForCreateProduct.put("productDescription", "Addias Originals") ;
		formdataForCreateProduct.put("productFor", "women") ;
		return formdataForCreateProduct;
	}

}
