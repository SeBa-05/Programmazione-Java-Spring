package com.example.demo.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="dipendenti")
@EqualsAndHashCode(callSuper=false)
@Data
public class Dipendente extends Persona{

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@Column(nullable = false)
	private double stipendio;
	
	
	@Column(nullable = false, unique = true)
	private String email;
	
	
	@Column(name="data_di_assunzione", nullable = false)
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) // per il binding da form
	private LocalDate dataDiAssunzione;
	
	
	
	
	@Enumerated(EnumType.STRING)
	@Column(name="ruolo", nullable = false)
	private Ruolo tipoRuolo;
	
	
	
	
}
