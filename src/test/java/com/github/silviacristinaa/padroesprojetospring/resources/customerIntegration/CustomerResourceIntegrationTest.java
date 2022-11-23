package com.github.silviacristinaa.padroesprojetospring.resources.customerIntegration;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.silviacristinaa.padroesprojetospring.models.Customer;
import com.github.silviacristinaa.padroesprojetospring.repositories.CustomerRepository;
import com.github.silviacristinaa.padroesprojetospring.resources.integrations.IntegrationTests;

public class CustomerResourceIntegrationTest extends IntegrationTests {

	private String customerId;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Test
	@Order(1)
	public void whenTryCreateCustomerWithEmptyFieldsReturnBadRequest() throws Exception {
		mvc.perform(post("/api/customers").headers(mockHttpHeaders())
				.content(CustomerResourceIntegrationBody.customerCreateBadRequest()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message").value("Arguments not valid"))
			.andDo(print());
	}
	
	@Test
	@Order(2)
	public void whenTryCreateCustomerWithIncorrectZipCodeReturnInternalServerError() throws Exception {
		mvc.perform(post("/api/customers").headers(mockHttpHeaders())
				.content(CustomerResourceIntegrationBody.customerIncorrectZipCode()))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("message").value("Unexpected error"))
			.andExpect(jsonPath("errors.[0]").value("Failed to save address"))
			.andDo(print());
	}
	
	@Test
	@Order(3)
	public void whenCreateCustomerReturnCreated() throws Exception {
		mvc.perform(post("/api/customers").headers(mockHttpHeaders())
				.content(CustomerResourceIntegrationBody.customerCreateSuccess()))
			.andExpect(status().isCreated())
			.andDo(i -> {
				customerId = getIdByLocation(i.getResponse().getHeader("Location"));
		    })
			.andDo(print());
	}
	
	@Test
	@Order(4)
	public void whenFindAllReturnOneValueSuccess() throws Exception {
		mvc.perform(get("/api/customers").headers(mockHttpHeaders()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("[0].name", is("teste")))
			.andExpect(jsonPath("[0].address.zipCode", is("01001-000")))
			.andDo(print());
	}
	
	@Test
	@Order(5)
	public void whenTryFindByIdWithIncorrectIdReturnNotFound() throws Exception {
		mvc.perform(get("/api/customers/{id}", 999).headers(mockHttpHeaders()))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("message", is("Not found")))
			.andExpect(jsonPath("errors.[0]", is("Customer 999 not found")))
			.andDo(print());
	}
	
	@Test
	@Order(6)
	public void whenFindByIdWithCorrectIdReturnSuccess() throws Exception {
		mvc.perform(get("/api/customers/{id}", customerId).headers(mockHttpHeaders()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name", is("teste")))
			.andExpect(jsonPath("address.zipCode", is("01001-000")))
			.andDo(print());
	}

	@Test
	@Order(7)
	public void whenTryUpdateCustomerWithEmptyFieldsReturnBadRequest() throws Exception {
		mvc.perform(put("/api/customers/{id}", customerId).headers(mockHttpHeaders())
				.content(CustomerResourceIntegrationBody.customerCreateBadRequest()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message").value("Arguments not valid"))
			.andDo(print());
	}
	
	@Test
	@Order(8)
	public void whenTryUpdateWithIncorrectIdReturnNotFound() throws Exception {
		mvc.perform(put("/api/customers/{id}", 999).headers(mockHttpHeaders())
				.content(CustomerResourceIntegrationBody.customerUpdateSuccess()))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("message", is("Not found")))
			.andExpect(jsonPath("errors.[0]", is("Customer 999 not found")))
			.andDo(print());
	}
	
	@Test
	@Order(9)
	public void whenTryUpdateCustomerWithIncorrectZipCodeReturnInternalServerError() throws Exception {
		mvc.perform(put("/api/customers/{id}", customerId).headers(mockHttpHeaders())
				.content(CustomerResourceIntegrationBody.customerIncorrectZipCode()))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("message").value("Unexpected error"))
			.andExpect(jsonPath("errors.[0]").value("Failed to save address"))
			.andDo(print());
	}
	
	@Test
	@Order(10)
	public void whenUpdateWithValueExistsReturnNoContent() throws Exception {
		mvc.perform(put("/api/customers/{id}", customerId).headers(mockHttpHeaders())
				.content(CustomerResourceIntegrationBody.customerUpdateSuccess()))
			.andExpect(status().isNoContent())
			.andDo(print());
		
		Optional<Customer> customer = customerRepository.findById(Long.valueOf(customerId));
		assertTrue(customer.isPresent());
		assertEquals(customer.get().getName(), "teste1");
		assertEquals(customer.get().getAddress().getZipCode(), "01001-000");
	}
	
	@Test
	@Order(11)
	public void whenTryDeleteCustomerWithIncorrectIdReturnNotFound() throws Exception {
		mvc.perform(delete("/api/customers/{id}", 999).headers(mockHttpHeaders()))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("message", is("Not found")))
			.andExpect(jsonPath("errors.[0]", is("Customer 999 not found")))
			.andDo(print());
	}
	
	@Test
	@Order(12)
	public void whenDeleteCustomerWithCorrectIdReturnNoContent() throws Exception {
		mvc.perform(delete("/api/customers/{id}", customerId).headers(mockHttpHeaders()))
			.andExpect(status().isNoContent())
			.andDo(print());
		
		Optional<Customer> customer = customerRepository.findById(Long.valueOf(customerId));
		assertFalse(customer.isPresent());
	}
}
