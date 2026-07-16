package com.example.demo.entities;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;


@MappedSuperclass
@Data
public class Persona {  //POJO
	
		
	
	@Column(nullable = false)
	private String nome;
	
	
	@Column(nullable = false)
	private String cognome;
	
	@Column(name="codice_fiscale", nullable = false, unique = true)
	private String cf;
	
	
	@Column(name="data_di_nascita", nullable = false)
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) // per il binding da form
	private LocalDate dataDiNascita;
	
	
	

}
