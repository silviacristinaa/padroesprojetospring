package com.github.silviacristinaa.padroesprojetospring.services;

import java.util.List;

import com.github.silviacristinaa.padroesprojetospring.dtos.requests.ClienteRequisicaoDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.ClienteRespostaDto;
import com.github.silviacristinaa.padroesprojetospring.exceptions.NotFoundException;
import com.github.silviacristinaa.padroesprojetospring.models.Cliente;

public interface ClienteService {
	
	List<ClienteRespostaDto> buscarTodos();
	
	ClienteRespostaDto burcarPorId(Long id) throws NotFoundException;
	
	Cliente inserir(ClienteRequisicaoDto clienteRequisicao);
	
	void atualizar(Long id, ClienteRequisicaoDto clienteRequisicao) throws NotFoundException;
	
	void deletar(Long id) throws NotFoundException;
}
