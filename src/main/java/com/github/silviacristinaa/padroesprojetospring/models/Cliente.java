package com.github.silviacristinaa.padroesprojetospring.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	@Column(nullable = false)
	private String nome; 
	@ManyToOne
	private Endereco endereco; 
}
