package com.github.silviacristinaa.padroesprojetospring.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	private String cep;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cep", referencedColumnName = "cep", insertable = false, updatable = false)
	private Endereco endereco; 
}