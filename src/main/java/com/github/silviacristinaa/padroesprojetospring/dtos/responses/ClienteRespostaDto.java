package com.github.silviacristinaa.padroesprojetospring.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteRespostaDto {
	
	private Long id; 
	private String nome; 
	private EnderecoRespostaDto endereco; 
}
