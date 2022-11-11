package com.github.silviacristinaa.padroesprojetospring.dtos.requests;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ClienteRequisicaoDto {
	
	@NotBlank
	private String nome; 
	@NotBlank
	private String cep; 
}
