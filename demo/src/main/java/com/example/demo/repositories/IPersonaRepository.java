package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Persona;

public interface IPersonaRepository extends JpaRepository<Persona, Integer>{
	
	
	

}
