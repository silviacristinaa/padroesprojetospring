package com.github.silviacristinaa.padroesprojetospring.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class GlobalExceptionHandlerTest {

	private static final String EXCEPTION_MSG_UNEXPECTED_ERROR = "Unexpected error";
	private static final String NOT_FOUND_MSG = "Not found";
	
	@InjectMocks
	private GlobalExceptionHandler globalExceptionHandler;

	@Test
	void whenExceptionReturnResponseEntity() {
		ResponseEntity<ErrorMessage> response = globalExceptionHandler
				.processException(new Exception("Failed to save address"));
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ErrorMessage.class, response.getBody().getClass());
		assertEquals(EXCEPTION_MSG_UNEXPECTED_ERROR, response.getBody().getMessage());
		assertEquals("Failed to save address", response.getBody().getErrors().get(0));
	}
	
	@Test
	void whenNotFoundExceptionReturnResponseEntity() {
		ResponseEntity<ErrorMessage> response = globalExceptionHandler
				.handleMethodArgumentNotFoundException(
						new NotFoundException(String.format("Customer %s not found", 1l)));
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ErrorMessage.class, response.getBody().getClass());
		assertEquals(NOT_FOUND_MSG, response.getBody().getMessage());
		assertEquals("Customer 1 not found", response.getBody().getErrors().get(0));
	}
}