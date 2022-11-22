package com.github.silviacristinaa.padroesprojetospring.models;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Address {
	
	@Id
	@JsonProperty("cep")
	private String zipCode;
	@JsonProperty("logradouro")
	private String street;
	@JsonProperty("complemento")
	private String complement;
	@JsonProperty("bairro")
	private String district;
	@JsonProperty("localidade")
	private String city;
	@JsonProperty("uf")
	private String state;
	private String ibge;
	private String gia;
	private String ddd;
	private String siafi;
}
