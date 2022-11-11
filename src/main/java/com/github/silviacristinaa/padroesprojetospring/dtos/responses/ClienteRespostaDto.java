package com.github.silviacristinaa.padroesprojetospring.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRespostaDto {
	
	private Long id; 
	private String nome; 
	private EnderecoRespostaDto endereco; 
}
