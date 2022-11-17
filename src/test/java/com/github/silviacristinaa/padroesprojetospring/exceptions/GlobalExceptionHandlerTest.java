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

	@InjectMocks
	private GlobalExceptionHandler globalExceptionHandler;

	@Test
	void quandoExceptionRetornarResponseEntity() {
		ResponseEntity<ErrorMessage> response = globalExceptionHandler
				.processException(new Exception("Falha ao salvar endereço"));
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ErrorMessage.class, response.getBody().getClass());
		assertEquals("Unexpected error", response.getBody().getMessage());
		assertEquals("Falha ao salvar endereço", response.getBody().getErrors().get(0));
	}
	
	@Test
	void quandoNotFoundExceptionRetornarResponseEntity() {
		ResponseEntity<ErrorMessage> response = globalExceptionHandler
				.handleMethodArgumentNotFoundException(
						new NotFoundException(String.format("Cliente %s não encontrado", 1l)));
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ErrorMessage.class, response.getBody().getClass());
		assertEquals("Not found", response.getBody().getMessage());
		assertEquals("Cliente 1 não encontrado", response.getBody().getErrors().get(0));
	}
}
