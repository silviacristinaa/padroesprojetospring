package com.github.silviacristinaa.padroesprojetospring.dtos.requests;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CustomerRequestDto {
	
	@NotBlank
	private String name; 
	@NotBlank
	private String zipCode; 
}