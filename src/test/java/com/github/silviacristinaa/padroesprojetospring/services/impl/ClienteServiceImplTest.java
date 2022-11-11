package com.github.silviacristinaa.padroesprojetospring.services.impl;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.silviacristinaa.padroesprojetospring.dtos.requests.ClienteRequisicaoDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.ClienteRespostaDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.EnderecoRespostaDto;
import com.github.silviacristinaa.padroesprojetospring.exceptions.InternalServerErrorException;
import com.github.silviacristinaa.padroesprojetospring.exceptions.NotFoundException;
import com.github.silviacristinaa.padroesprojetospring.models.Cliente;
import com.github.silviacristinaa.padroesprojetospring.models.Endereco;
import com.github.silviacristinaa.padroesprojetospring.repositories.ClienteRepository;
import com.github.silviacristinaa.padroesprojetospring.repositories.EnderecoRepository;
import com.github.silviacristinaa.padroesprojetospring.services.ViaCepService;

@ExtendWith(SpringExtension.class)
public class ClienteServiceImplTest {
	
	private static final long ID = 1l;
	private static final String NOME = "nome";
	private static final String CEP = "cep";
	private static final int INDEX = 0;
	
	private ClienteRequisicaoDto clienteRequisicaoDto;
	private EnderecoRespostaDto enderecoRespostaDto;
	private ClienteRespostaDto clienteRespostaDto;
	private Cliente cliente;
	private Endereco endereco;

	@InjectMocks
	private ClienteServiceImpl clienteServiceImpl;

	@Mock
	private ClienteRepository clienteRepository;

	@Mock
	private EnderecoRepository enderecoRepository;

	@Mock
	private ViaCepService viaCepService;

	@Mock
	private ModelMapper modelMapper;

	@BeforeEach
	void setUp() {
		clienteRequisicaoDto = new ClienteRequisicaoDto(NOME, CEP);

		enderecoRespostaDto = new EnderecoRespostaDto();
		enderecoRespostaDto.setCep(CEP);

		clienteRespostaDto = new ClienteRespostaDto(1l, NOME, enderecoRespostaDto);

		cliente = new Cliente(1l, NOME, endereco);

		endereco = new Endereco();
		endereco.setCep(CEP);
	}

	@Test
	void quandoBuscarTodosRetornarListaDeClientes() {
		when(clienteRepository.findAll()).thenReturn(List.of(cliente));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(clienteRespostaDto);

		List<ClienteRespostaDto> response = clienteServiceImpl.buscarTodos();

		assertNotNull(response);
		assertEquals(1, response.size());
		assertEquals(ClienteRespostaDto.class, response.get(INDEX).getClass());

		assertEquals(ID, response.get(INDEX).getId());
		assertEquals(NOME, response.get(INDEX).getNome());
		assertEquals(CEP, response.get(INDEX).getEndereco().getCep());
	}

	@Test
	void quandoBuscarPorIdRetornarClienteUnico() throws NotFoundException {
		when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(clienteRespostaDto);

		ClienteRespostaDto response = clienteServiceImpl.burcarPorId(ID);

		assertNotNull(response);

		assertEquals(ClienteRespostaDto.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NOME, response.getNome());
		assertEquals(CEP, response.getEndereco().getCep());
	}

	@Test
	void quandoTentarBuscarPorIdRetornarNotFoundException() {
		when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> clienteServiceImpl.burcarPorId(ID));

		assertEquals(String.format("Cliente %s não encontrado", ID), exception.getMessage());
	}

	@Test
	void quandoInserirRetornarSucesso() throws InternalServerErrorException {
		when(enderecoRepository.findById(Mockito.any())).thenReturn(Optional.of(endereco));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(cliente);
		when(clienteRepository.save(Mockito.any())).thenReturn(cliente);

		Cliente response = clienteServiceImpl.inserir(clienteRequisicaoDto);

		assertNotNull(response);
		assertEquals(Cliente.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NOME, response.getNome());
		assertEquals(CEP, response.getEndereco().getCep());

		verify(clienteRepository, times(1)).save(Mockito.any());
	}

	@Test
	void quandoInserirComCepNovoRetornarSucesso() throws InternalServerErrorException {
		when(enderecoRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		when(viaCepService.consultarCep(Mockito.anyString())).thenReturn(endereco);
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(cliente);
		when(clienteRepository.save(Mockito.any())).thenReturn(cliente);

		Cliente response = clienteServiceImpl.inserir(clienteRequisicaoDto);

		assertNotNull(response);
		assertEquals(Cliente.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NOME, response.getNome());
		assertEquals(CEP, response.getEndereco().getCep());

		verify(enderecoRepository, times(1)).save(Mockito.any());
		verify(clienteRepository, times(1)).save(Mockito.any());
	}

	@Test
	void quandoTentarInserirRetornarInternalServerErrorException() throws InternalServerErrorException {
		when(enderecoRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		when(viaCepService.consultarCep(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

		InternalServerErrorException exception = assertThrows(InternalServerErrorException.class,
				() -> clienteServiceImpl.inserir(clienteRequisicaoDto));

		assertEquals("Falha ao salvar endereço", exception.getMessage());
	}

	@Test
	void quandoAtualizarRetornarSucesso() throws NotFoundException, InternalServerErrorException {
		when(clienteRepository.findById(Mockito.any())).thenReturn(Optional.of(cliente));
		when(enderecoRepository.findById(Mockito.any())).thenReturn(Optional.of(endereco));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(cliente);

		clienteServiceImpl.atualizar(ID, clienteRequisicaoDto);

		verify(clienteRepository, times(1)).save(Mockito.any());
	}

	@Test
	void quandoAtualizarComCepNovoRetornarSucesso() throws NotFoundException, InternalServerErrorException {
		when(clienteRepository.findById(Mockito.any())).thenReturn(Optional.of(cliente));
		when(enderecoRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		when(viaCepService.consultarCep(Mockito.anyString())).thenReturn(endereco);
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(cliente);

		clienteServiceImpl.atualizar(ID, clienteRequisicaoDto);

		verify(enderecoRepository, times(1)).save(Mockito.any());
		verify(clienteRepository, times(1)).save(Mockito.any());
	}

	@Test
	void quandoTentarAtualizarRetornarNotFoundException() throws NotFoundException, InternalServerErrorException {
		when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class,
				() -> clienteServiceImpl.atualizar(ID, clienteRequisicaoDto));

		assertEquals(String.format("Cliente %s não encontrado", ID), exception.getMessage());
	}

	@Test
	void quandoTentarAtualizarRetornarInternalServerErrorException() throws NotFoundException, InternalServerErrorException {
		when(clienteRepository.findById(Mockito.any())).thenReturn(Optional.of(cliente));
		when(enderecoRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		when(viaCepService.consultarCep(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

		InternalServerErrorException exception = assertThrows(InternalServerErrorException.class,
				() -> clienteServiceImpl.atualizar(ID, clienteRequisicaoDto));

		assertEquals("Falha ao salvar endereço", exception.getMessage());
	}

	@Test
	void quandoDeletarRetornarSucesso() throws NotFoundException {
		when(clienteRepository.findById(Mockito.any())).thenReturn(Optional.of(cliente));
		
		clienteServiceImpl.deletar(ID);
		
		verify(clienteRepository, times(1)).deleteById(anyLong());
	}

	@Test
	void quandoTentarDeletarRetornarNotFoundException() {
		when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> clienteServiceImpl.deletar(ID));

		assertEquals(String.format("Cliente %s não encontrado", ID), exception.getMessage());
	}
}
