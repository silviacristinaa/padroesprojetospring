package com.github.silviacristinaa.padroesprojetospring.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.silviacristinaa.padroesprojetospring.dtos.requests.CustomerRequestDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.CustomerResponseDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.AddressResponseDto;
import com.github.silviacristinaa.padroesprojetospring.exceptions.InternalServerErrorException;
import com.github.silviacristinaa.padroesprojetospring.exceptions.NotFoundException;
import com.github.silviacristinaa.padroesprojetospring.models.Customer;
import com.github.silviacristinaa.padroesprojetospring.services.CustomerService;

@ExtendWith(SpringExtension.class)
public class CustomerResourceTest {

	private static final long ID = 1l;
	private static final String NAME = "name";
	private static final String ZIP_CODE = "zipCode";
	private static final int INDEX = 0;
	
	private CustomerRequestDto customerRequestDto;
	private AddressResponseDto addressResponseDto;
	private CustomerResponseDto customerResponseDto;
	private Customer customer;

	@InjectMocks
	private CustomerResource customerResource;

	@Mock
	private CustomerService customerService;
	
	@BeforeEach
	void setUp() {
		customerRequestDto = new CustomerRequestDto(NAME, ZIP_CODE);
		
		addressResponseDto = new AddressResponseDto();
		addressResponseDto.setZipCode(ZIP_CODE);
		
		customerResponseDto = new CustomerResponseDto(ID, NAME, addressResponseDto);
		
		customer = new Customer(); 
	}
	
	@Test
	void whenFindAllReturnCustomerList() {
		when(customerService.findAll()).thenReturn(Arrays.asList(customerResponseDto));

		ResponseEntity<List<CustomerResponseDto>> response = customerResource.findAll();

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(CustomerResponseDto.class, response.getBody().get(INDEX).getClass());

		assertEquals(ID, response.getBody().get(INDEX).getId());
		assertEquals(NAME, response.getBody().get(INDEX).getName());
		assertEquals(ZIP_CODE, response.getBody().get(INDEX).getAddress().getZipCode());
	}
	
	@Test
	void whenFindByIdReturnOneCustomer() throws NotFoundException {
		when(customerService.findOneCustomerById(anyLong())).thenReturn(customerResponseDto);
		
		ResponseEntity<CustomerResponseDto> response = customerResource.findById(ID);
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(CustomerResponseDto.class, response.getBody().getClass());
		
		assertEquals(ID, response.getBody().getId());
		assertEquals(NAME, response.getBody().getName());
		assertEquals(ZIP_CODE, response.getBody().getAddress().getZipCode());
	}

	@Test
	void whenCreateCustomerReturnCreated() throws InternalServerErrorException {
		when(customerService.create(Mockito.any())).thenReturn(customer);

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ResponseEntity<Void> response = customerResource.create(customerRequestDto);

		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getHeaders().get("Location"));
	}

	@Test
	void whenUpdateReturnNoContent() throws NotFoundException, InternalServerErrorException {
		ResponseEntity<Void> response = customerResource.update(ID, customerRequestDto);
		
		assertNotNull(response);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()); 
		assertEquals(ResponseEntity.class, response.getClass());
	}
	
	@Test
	void whenDeleteReturnNoContent() throws NotFoundException {
		ResponseEntity<Void> response = customerResource.delete(ID);
		
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(customerService, times(1)).delete(anyLong());
	}
}
