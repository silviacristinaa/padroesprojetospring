package com.github.silviacristinaa.padroesprojetospring.services;

import java.util.List;

import com.github.silviacristinaa.padroesprojetospring.dtos.requests.CustomerRequestDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.CustomerResponseDto;
import com.github.silviacristinaa.padroesprojetospring.exceptions.InternalServerErrorException;
import com.github.silviacristinaa.padroesprojetospring.exceptions.NotFoundException;
import com.github.silviacristinaa.padroesprojetospring.models.Customer;

public interface CustomerService {
	
	List<CustomerResponseDto> findAll();
	
	CustomerResponseDto findOneCustomerById(Long id) throws NotFoundException;
	
	Customer create(CustomerRequestDto customerRequestDto) throws InternalServerErrorException;
	
	void update(Long id, CustomerRequestDto customerRequestDto) throws NotFoundException, InternalServerErrorException;
	
	void delete(Long id) throws NotFoundException;
}
