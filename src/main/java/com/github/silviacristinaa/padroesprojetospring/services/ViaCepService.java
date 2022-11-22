package com.github.silviacristinaa.padroesprojetospring.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.github.silviacristinaa.padroesprojetospring.models.Address;

@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface ViaCepService {
	
	@GetMapping("/{cep}/json/")
	Address consultZipCode(@PathVariable("cep") String zipCode);
}
