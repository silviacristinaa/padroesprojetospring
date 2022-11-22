package com.github.silviacristinaa.padroesprojetospring.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.silviacristinaa.padroesprojetospring.dtos.requests.CustomerRequestDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.CustomerResponseDto;
import com.github.silviacristinaa.padroesprojetospring.exceptions.InternalServerErrorException;
import com.github.silviacristinaa.padroesprojetospring.exceptions.NotFoundException;
import com.github.silviacristinaa.padroesprojetospring.models.Customer;
import com.github.silviacristinaa.padroesprojetospring.models.Address;
import com.github.silviacristinaa.padroesprojetospring.repositories.CustomerRepository;
import com.github.silviacristinaa.padroesprojetospring.repositories.AddressRepository;
import com.github.silviacristinaa.padroesprojetospring.services.CustomerService;
import com.github.silviacristinaa.padroesprojetospring.services.ViaCepService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private ViaCepService viaCepService;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<CustomerResponseDto> findAll() {
		return customerRepository.findAll().stream().map(customer -> modelMapper.map(customer, CustomerResponseDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public CustomerResponseDto findOneCustomerById(Long id) throws NotFoundException {
		Customer customer = findById(id);
		return modelMapper.map(customer, CustomerResponseDto.class);
	}

	@Override
	@Transactional
	public Customer create(CustomerRequestDto customerRequestDto) throws InternalServerErrorException {
		Customer customer = getCustomerWithZipCode(customerRequestDto);
		return customerRepository.save(customer);
	}

	private Customer getCustomerWithZipCode(CustomerRequestDto customerRequestDto) throws InternalServerErrorException {
		Address address = saveAddress(customerRequestDto);
		Customer customer = modelMapper.map(customerRequestDto, Customer.class);
		customer.setZipCode(address.getZipCode());

		return customer;
	}

	private Address saveAddress(CustomerRequestDto customerRequestDto) throws InternalServerErrorException {
		try {
			String zipCode = customerRequestDto.getZipCode();
			return addressRepository.findById(zipCode).orElseGet(() -> {
				Address newAddress = viaCepService.consultZipCode(zipCode);
				addressRepository.save(newAddress);
				return newAddress;
			});
		} catch (Exception e) {
			throw new InternalServerErrorException("Failed to save address");
		}
	}

	@Override
	@Transactional
	public void update(Long id, CustomerRequestDto customerRequestDto) throws NotFoundException, InternalServerErrorException {
		findById(id);
		Customer customer = getCustomerWithZipCode(customerRequestDto);
		customer.setId(id);

		customerRepository.save(customer);
	}

	private Customer findById(Long id) throws NotFoundException {
		return customerRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Customer %s not found", id)));
	}

	@Override
	@Transactional
	public void delete(Long id) throws NotFoundException {
		findById(id);
		customerRepository.deleteById(id);
	}
}
