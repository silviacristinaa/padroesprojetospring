package com.github.silviacristinaa.padroesprojetospring.dtos.requests;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteRequisicaoDto {
	
	@NotBlank
	private String nome; 
	@NotBlank
	private String cep; 
}
