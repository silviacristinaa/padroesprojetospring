package com.github.silviacristinaa.padroesprojetospring.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.silviacristinaa.padroesprojetospring.dtos.requests.ClienteRequisicaoDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.ClienteRespostaDto;
import com.github.silviacristinaa.padroesprojetospring.exceptions.InternalServerErrorException;
import com.github.silviacristinaa.padroesprojetospring.exceptions.NotFoundException;
import com.github.silviacristinaa.padroesprojetospring.services.ClienteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/clientes")
@Api(value = "Clientes", tags = {"Serviço para controle de Clientes"})
public class ClienteResource {

	private static final String ID = "/{id}";
	
	@Autowired
	private ClienteService clienteService;

	@GetMapping
	@ApiOperation(value="Retorna todos os clientes")
	public ResponseEntity<List<ClienteRespostaDto>> buscarTodos() {
		return ResponseEntity.ok(clienteService.buscarTodos());
	}

	@GetMapping(value = ID)
	@ApiOperation(value="Retorna um cliente único")
	public ResponseEntity<ClienteRespostaDto> buscarPorId(@PathVariable Long id) throws NotFoundException {
		return ResponseEntity.ok(clienteService.burcarPorId(id));
	}

	@PostMapping
	@ApiOperation(value="Cria um cliente")
	public ResponseEntity<Void> inserir(@RequestBody @Valid ClienteRequisicaoDto clienteRequisicao) throws InternalServerErrorException {
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path(ID).buildAndExpand(clienteService.inserir(clienteRequisicao).getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PutMapping(value = ID)
	@ApiOperation(value="Atualiza um cliente")
	public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequisicaoDto clienteRequisicao) throws NotFoundException, InternalServerErrorException {
		clienteService.atualizar(id, clienteRequisicao);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = ID)
	@ApiOperation(value="Deleta um cliente")
	public ResponseEntity<Void> deletar(@PathVariable Long id) throws NotFoundException {
		clienteService.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
