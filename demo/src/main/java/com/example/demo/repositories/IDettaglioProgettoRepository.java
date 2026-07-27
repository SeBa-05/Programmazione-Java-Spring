package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.DettaglioProgetto;
import com.example.demo.entities.Dipendente;

public interface IDettaglioProgettoRepository extends JpaRepository<DettaglioProgetto, Integer> {

    List<DettaglioProgetto> findByProgettoId(Integer progettoId);

    List<DettaglioProgetto> findByDipendenteId(Integer dipendenteId);

    // Conta le assegnazioni per una coppia (più robusto di findBy)
    @Query("SELECT COUNT(dp) FROM DettaglioProgetto dp WHERE dp.dipendente.id = :dipendenteId AND dp.progetto.id = :progettoId")
    int countByDipendenteIdAndProgettoId(@Param("dipendenteId") Integer dipendenteId, 
                                         @Param("progettoId") Integer progettoId);

    @Query("SELECT dp.dipendente FROM DettaglioProgetto dp WHERE dp.progetto.id = :progettoId")
    List<Dipendente> findDipendentiAssegnati(@Param("progettoId") Integer progettoId);

    @Query("SELECT d FROM Dipendente d WHERE d.id NOT IN (SELECT dp.dipendente.id FROM DettaglioProgetto dp WHERE dp.progetto.id = :progettoId)")
    List<Dipendente> findDipendentiNonAssegnati(@Param("progettoId") Integer progettoId);

    long countByProgettoId(Integer progettoId);

    @Modifying
    @Transactional
    @Query("DELETE FROM DettaglioProgetto dp WHERE dp.dipendente.id = :dipendenteId AND dp.progetto.id = :progettoId")
    void deleteByDipendenteIdAndProgettoId(@Param("dipendenteId") Integer dipendenteId, 
                                           @Param("progettoId") Integer progettoId);

    @Modifying
    @Transactional
    @Query("DELETE FROM DettaglioProgetto dp WHERE dp.dipendente.id = :dipendenteId")
    void deleteByDipendenteId(@Param("dipendenteId") Integer dipendenteId);
}