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

import com.github.silviacristinaa.padroesprojetospring.dtos.requests.CustomerRequestDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.CustomerResponseDto;
import com.github.silviacristinaa.padroesprojetospring.dtos.responses.AddressResponseDto;
import com.github.silviacristinaa.padroesprojetospring.exceptions.InternalServerErrorException;
import com.github.silviacristinaa.padroesprojetospring.exceptions.NotFoundException;
import com.github.silviacristinaa.padroesprojetospring.models.Customer;
import com.github.silviacristinaa.padroesprojetospring.models.Address;
import com.github.silviacristinaa.padroesprojetospring.repositories.CustomerRepository;
import com.github.silviacristinaa.padroesprojetospring.repositories.AddressRepository;
import com.github.silviacristinaa.padroesprojetospring.services.ViaCepService;

@ExtendWith(SpringExtension.class)
public class CustomerServiceImplTest {
	
	private static final long ID = 1l;
	private static final String NAME = "name";
	private static final String ZIP_CODE = "zipCode";
	private static final int INDEX = 0;
	
	private CustomerRequestDto customerRequestDto;
	private AddressResponseDto addressResponseDto;
	private CustomerResponseDto customerResponseDto;
	private Customer customer;
	private Address address;

	@InjectMocks
	private CustomerServiceImpl customerServiceImpl;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AddressRepository addressRepository;

	@Mock
	private ViaCepService viaCepService;

	@Mock
	private ModelMapper modelMapper;

	@BeforeEach
	void setUp() {
		customerRequestDto = new CustomerRequestDto(NAME, ZIP_CODE);

		addressResponseDto = new AddressResponseDto();
		addressResponseDto.setZipCode(ZIP_CODE);

		customerResponseDto = new CustomerResponseDto(1l, NAME, addressResponseDto);

		address = new Address();
		address.setZipCode(ZIP_CODE);
		
		customer = new Customer(1l, NAME, ZIP_CODE, address);
	}

	@Test
	void whenFindAllReturnCustomerList() {
		when(customerRepository.findAll()).thenReturn(List.of(customer));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(customerResponseDto);

		List<CustomerResponseDto> response = customerServiceImpl.findAll();

		assertNotNull(response);
		assertEquals(1, response.size());
		assertEquals(CustomerResponseDto.class, response.get(INDEX).getClass());

		assertEquals(ID, response.get(INDEX).getId());
		assertEquals(NAME, response.get(INDEX).getName());
		assertEquals(ZIP_CODE, response.get(INDEX).getAddress().getZipCode());
	}

	@Test
	void whenFindByIdReturnOneCustomer() throws NotFoundException {
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(customerResponseDto);

		CustomerResponseDto response = customerServiceImpl.findOneCustomerById(ID);

		assertNotNull(response);

		assertEquals(CustomerResponseDto.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NAME, response.getName());
		assertEquals(ZIP_CODE, response.getAddress().getZipCode());
	}

	@Test
	void whenTryFindByIdReturnNotFoundException() {
		when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> customerServiceImpl.findOneCustomerById(ID));

		assertEquals(String.format("Customer %s not found", ID), exception.getMessage());
	}

	@Test
	void whenCreateReturnSuccess() throws InternalServerErrorException {
		when(addressRepository.findById(Mockito.any())).thenReturn(Optional.of(address));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(customer);
		when(customerRepository.save(Mockito.any())).thenReturn(customer);

		Customer response = customerServiceImpl.create(customerRequestDto);

		assertNotNull(response);
		assertEquals(Customer.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NAME, response.getName());
		assertEquals(ZIP_CODE, response.getAddress().getZipCode());

		verify(customerRepository, times(1)).save(Mockito.any());
	}

	@Test
	void whenCreateWithNewZipCodeReturnSuccess() throws InternalServerErrorException {
		when(addressRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		when(viaCepService.consultZipCode(Mockito.anyString())).thenReturn(address);
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(customer);
		when(customerRepository.save(Mockito.any())).thenReturn(customer);

		Customer response = customerServiceImpl.create(customerRequestDto);

		assertNotNull(response);
		assertEquals(Customer.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NAME, response.getName());
		assertEquals(ZIP_CODE, response.getAddress().getZipCode());

		verify(addressRepository, times(1)).save(Mockito.any());
		verify(customerRepository, times(1)).save(Mockito.any());
	}

	@Test
	void whenTryCreateReturnInternalServerErrorException() throws InternalServerErrorException {
		when(addressRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		when(viaCepService.consultZipCode(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

		InternalServerErrorException exception = assertThrows(InternalServerErrorException.class,
				() -> customerServiceImpl.create(customerRequestDto));

		assertEquals("Failed to save address", exception.getMessage());
	}

	@Test
	void whenUpdateReturnSuccess() throws NotFoundException, InternalServerErrorException {
		when(customerRepository.findById(Mockito.any())).thenReturn(Optional.of(customer));
		when(addressRepository.findById(Mockito.any())).thenReturn(Optional.of(address));
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(customer);

		customerServiceImpl.update(ID, customerRequestDto);

		verify(customerRepository, times(1)).save(Mockito.any());
	}

	@Test
	void whenUpdateWithNewZipCodeReturnSuccess() throws NotFoundException, InternalServerErrorException {
		when(customerRepository.findById(Mockito.any())).thenReturn(Optional.of(customer));
		when(addressRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		when(viaCepService.consultZipCode(Mockito.anyString())).thenReturn(address);
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(customer);

		customerServiceImpl.update(ID, customerRequestDto);

		verify(addressRepository, times(1)).save(Mockito.any());
		verify(customerRepository, times(1)).save(Mockito.any());
	}

	@Test
	void whenTryUpdateReturnNotFoundException() throws NotFoundException, InternalServerErrorException {
		when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class,
				() -> customerServiceImpl.update(ID, customerRequestDto));

		assertEquals(String.format("Customer %s not found", ID), exception.getMessage());
	}

	@Test
	void whenTryUpdateReturnInternalServerErrorException() throws NotFoundException, InternalServerErrorException {
		when(customerRepository.findById(Mockito.any())).thenReturn(Optional.of(customer));
		when(addressRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		when(viaCepService.consultZipCode(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

		InternalServerErrorException exception = assertThrows(InternalServerErrorException.class,
				() -> customerServiceImpl.update(ID, customerRequestDto));

		assertEquals("Failed to save address", exception.getMessage());
	}

	@Test
	void whenDeleteReturnSuccess() throws NotFoundException {
		when(customerRepository.findById(Mockito.any())).thenReturn(Optional.of(customer));
		
		customerServiceImpl.delete(ID);
		
		verify(customerRepository, times(1)).deleteById(anyLong());
	}

	@Test
	void whenTryDeleteReturnNotFoundException() {
		when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> customerServiceImpl.delete(ID));

		assertEquals(String.format("Customer %s not found", ID), exception.getMessage());
	}
}
