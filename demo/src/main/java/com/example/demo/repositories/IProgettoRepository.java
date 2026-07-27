package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Progetto;

public interface IProgettoRepository extends JpaRepository<Progetto, Integer> {

    List<Progetto> findByStato(String stato);

    Page<Progetto> findByStato(String stato, Pageable pageable);

    List<Progetto> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT COUNT(dp) FROM DettaglioProgetto dp WHERE dp.progetto.id = :progettoId")
    long countDipendentiAssegnati(@Param("progettoId") Integer progettoId);
}