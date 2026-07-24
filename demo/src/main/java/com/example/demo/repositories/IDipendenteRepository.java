package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Dipendente;

public interface IDipendenteRepository extends JpaRepository<Dipendente, Integer> {

    // Metodo personalizzato per cercare per nome e cognome
    List<Dipendente> findByNomeAndCognome(String nome, String cognome);
}