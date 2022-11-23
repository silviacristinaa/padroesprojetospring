package com.github.silviacristinaa.padroesprojetospring.resources.customerIntegration;

public class CustomerResourceIntegrationBody {

	static String customerCreateBadRequest() {
		return "{\r\n" + 
				"    \"name\": \"\",\r\n" + 
				"    \"zipCode\": \"\"\r\n" + 
				"}";
	}
	
	static String customerCreateSuccess() {
		return "{\r\n" + 
				"    \"name\": \"teste\",\r\n" + 
				"    \"zipCode\": \"01001000\"\r\n" + 
				"}";
	}
	
	static String customerIncorrectZipCode() {
		return "{\r\n" + 
				"    \"name\": \"teste\",\r\n" + 
				"    \"zipCode\": \"0\"\r\n" + 
				"}";
	}
	
	static String customerUpdateSuccess() {
		return "{\r\n" + 
				"    \"name\": \"teste1\",\r\n" + 
				"    \"zipCode\": \"01001000\"\r\n" + 
				"}";
	}
}
