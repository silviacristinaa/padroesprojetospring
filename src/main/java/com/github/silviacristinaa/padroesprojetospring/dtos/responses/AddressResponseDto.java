package com.github.silviacristinaa.padroesprojetospring.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddressResponseDto {
	
	private String zipCode; 
	private String street; 
	private String complement;  
	private String district;
	private String city; 
	private String state; 
	private String ibge;
	private String gia;
	private String ddd; 
	private String siafi;
}
