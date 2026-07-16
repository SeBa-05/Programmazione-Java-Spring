package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Dipendente;



public interface IPersonaRepository extends JpaRepository<Dipendente, Integer>{
	
	
	

}
