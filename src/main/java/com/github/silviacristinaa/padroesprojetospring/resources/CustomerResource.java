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

import com.github.silviacristinaa.padroesprojetospring.dtos.requests.CustomerRequestDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.CustomerResponseDto;
import com.github.silviacristinaa.padroesprojetospring.exceptions.InternalServerErrorException;
import com.github.silviacristinaa.padroesprojetospring.exceptions.NotFoundException;
import com.github.silviacristinaa.padroesprojetospring.services.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/customers")
@Api(value = "Clientes", tags = {"Serviço para controle de Clientes"})
public class CustomerResource {

	private static final String ID = "/{id}";
	
	@Autowired
	private CustomerService customerService;

	@GetMapping
	@ApiOperation(value="Retorna todos os clientes")
	public ResponseEntity<List<CustomerResponseDto>> findAll() {
		return ResponseEntity.ok(customerService.findAll());
	}

	@GetMapping(value = ID)
	@ApiOperation(value="Retorna um cliente único")
	public ResponseEntity<CustomerResponseDto> findById(@PathVariable Long id) throws NotFoundException {
		return ResponseEntity.ok(customerService.findOneCustomerById(id));
	}

	@PostMapping
	@ApiOperation(value="Cria um cliente")
	public ResponseEntity<Void> create(@RequestBody @Valid CustomerRequestDto customerRequestDto) throws InternalServerErrorException {
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path(ID).buildAndExpand(customerService.create(customerRequestDto).getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PutMapping(value = ID)
	@ApiOperation(value="Atualiza um cliente")
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid CustomerRequestDto customerRequestDto) throws NotFoundException, InternalServerErrorException {
		customerService.update(id, customerRequestDto);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = ID)
	@ApiOperation(value="Deleta um cliente")
	public ResponseEntity<Void> delete(@PathVariable Long id) throws NotFoundException {
		customerService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
