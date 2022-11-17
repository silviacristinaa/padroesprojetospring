package com.github.silviacristinaa.padroesprojetospring.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.silviacristinaa.padroesprojetospring.dtos.requests.ClienteRequisicaoDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.ClienteRespostaDto;
import com.github.silviacristinaa.padroesprojetospring.exceptions.InternalServerErrorException;
import com.github.silviacristinaa.padroesprojetospring.exceptions.NotFoundException;
import com.github.silviacristinaa.padroesprojetospring.models.Cliente;
import com.github.silviacristinaa.padroesprojetospring.models.Endereco;
import com.github.silviacristinaa.padroesprojetospring.repositories.ClienteRepository;
import com.github.silviacristinaa.padroesprojetospring.repositories.EnderecoRepository;
import com.github.silviacristinaa.padroesprojetospring.services.ClienteService;
import com.github.silviacristinaa.padroesprojetospring.services.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<ClienteRespostaDto> buscarTodos() {
		return clienteRepository.findAll().stream().map(cliente -> modelMapper.map(cliente, ClienteRespostaDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ClienteRespostaDto burcarPorId(Long id) throws NotFoundException {
		Cliente cliente = findById(id);
		return modelMapper.map(cliente, ClienteRespostaDto.class);
	}

	@Override
	@Transactional
	public Cliente inserir(ClienteRequisicaoDto clienteRequisicao) throws InternalServerErrorException {
		Cliente cliente = getClienteComCep(clienteRequisicao);
		return clienteRepository.save(cliente);
	}

	private Cliente getClienteComCep(ClienteRequisicaoDto clienteRequisicao) throws InternalServerErrorException {
		Endereco endereco = salvarEndereco(clienteRequisicao);
		Cliente cliente = modelMapper.map(clienteRequisicao, Cliente.class);
		cliente.setCep(endereco.getCep());

		return cliente;
	}

	private Endereco salvarEndereco(ClienteRequisicaoDto clienteRequisicao) throws InternalServerErrorException {
		try {
			String cep = clienteRequisicao.getCep();
			return enderecoRepository.findById(cep).orElseGet(() -> {
				Endereco novoEndereco = viaCepService.consultarCep(cep);
				enderecoRepository.save(novoEndereco);
				return novoEndereco;
			});
		} catch (Exception e) {
			throw new InternalServerErrorException("Falha ao salvar endereço");
		}
	}

	@Override
	@Transactional
	public void atualizar(Long id, ClienteRequisicaoDto clienteRequisicao) throws NotFoundException, InternalServerErrorException {
		findById(id);
		Cliente cliente = getClienteComCep(clienteRequisicao);
		cliente.setId(id);

		clienteRepository.save(cliente);
	}

	private Cliente findById(Long id) throws NotFoundException {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Cliente %s não encontrado", id)));
	}

	@Override
	@Transactional
	public void deletar(Long id) throws NotFoundException {
		findById(id);
		clienteRepository.deleteById(id);
	}
}
