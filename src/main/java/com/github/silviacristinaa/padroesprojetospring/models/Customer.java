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
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	@Column(nullable = false)
	private String name; 
	private String zipCode;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "zipCode", referencedColumnName = "zipCode", insertable = false, updatable = false)
	private Address address; 
}