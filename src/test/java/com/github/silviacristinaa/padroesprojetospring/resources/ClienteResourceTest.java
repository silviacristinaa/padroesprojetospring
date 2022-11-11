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

import com.github.silviacristinaa.padroesprojetospring.dtos.requests.ClienteRequisicaoDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.ClienteRespostaDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.EnderecoRespostaDto;
import com.github.silviacristinaa.padroesprojetospring.exceptions.InternalServerErrorException;
import com.github.silviacristinaa.padroesprojetospring.exceptions.NotFoundException;
import com.github.silviacristinaa.padroesprojetospring.models.Cliente;
import com.github.silviacristinaa.padroesprojetospring.services.ClienteService;

@ExtendWith(SpringExtension.class)
public class ClienteResourceTest {

	private static final long ID = 1l;
	private static final String NOME = "nome";
	private static final String CEP = "cep";
	private static final int INDEX = 0;
	
	private ClienteRequisicaoDto clienteRequisicaoDto;
	private EnderecoRespostaDto enderecoRespostaDto;
	private ClienteRespostaDto clienteRespostaDto;
	private Cliente cliente;

	@InjectMocks
	private ClienteResource clienteResource;

	@Mock
	private ClienteService clienteService;
	
	@BeforeEach
	void setUp() {
		clienteRequisicaoDto = new ClienteRequisicaoDto(NOME, CEP);
		
		enderecoRespostaDto = new EnderecoRespostaDto();
		enderecoRespostaDto.setCep(CEP);
		
		clienteRespostaDto = new ClienteRespostaDto(ID, NOME, enderecoRespostaDto);
		
		cliente = new Cliente(); 
	}
	
	@Test
	void quandoBuscarTodosRetornarListaDeClientes() {
		when(clienteService.buscarTodos()).thenReturn(Arrays.asList(clienteRespostaDto));

		ResponseEntity<List<ClienteRespostaDto>> response = clienteResource.buscarTodos();

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ClienteRespostaDto.class, response.getBody().get(INDEX).getClass());

		assertEquals(ID, response.getBody().get(INDEX).getId());
		assertEquals(NOME, response.getBody().get(INDEX).getNome());
		assertEquals(CEP, response.getBody().get(INDEX).getEndereco().getCep());
	}
	
	@Test
	void quandoBuscarPorIdRetornarClienteUnico() throws NotFoundException {
		when(clienteService.burcarPorId(anyLong())).thenReturn(clienteRespostaDto);
		
		ResponseEntity<ClienteRespostaDto> response = clienteResource.buscarPorId(ID);
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ClienteRespostaDto.class, response.getBody().getClass());
		
		assertEquals(ID, response.getBody().getId());
		assertEquals(NOME, response.getBody().getNome());
		assertEquals(CEP, response.getBody().getEndereco().getCep());
	}

	@Test
	void quandoInserirRetornarCreated() throws InternalServerErrorException {
		when(clienteService.inserir(Mockito.any())).thenReturn(cliente);

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ResponseEntity<Void> response = clienteResource.inserir(clienteRequisicaoDto);

		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getHeaders().get("Location"));
	}

	@Test
	void quandoAtualizarRetornarNoContent() throws NotFoundException, InternalServerErrorException {
		ResponseEntity<Void> response = clienteResource.atualizar(ID, clienteRequisicaoDto);
		
		assertNotNull(response);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()); 
		assertEquals(ResponseEntity.class, response.getClass());
	}
	
	@Test
	void quandoDeletarRetornarNoContent() throws NotFoundException {
		ResponseEntity<Void> response = clienteResource.deletar(ID);
		
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(clienteService, times(1)).deletar(anyLong());
	}
}
