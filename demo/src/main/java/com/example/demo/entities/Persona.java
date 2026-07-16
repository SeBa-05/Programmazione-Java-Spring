package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;




@Entity
@Table(name="persone")
@Data
public class Persona {  //POJO
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	@Column(nullable = false)
	private String nome;
	
	
	@Column(nullable = false)
	private String cognome;
	
	@Column(name="codice_fiscale", nullable = false, unique = true)
	private String cf;
	
	
	

}
