package com.example.demo.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Dipendente;
import com.example.demo.repositories.IDipendenteRepository;
import com.example.demo.repositories.IDettaglioProgettoRepository;

@Service
public class DipendenteService {

    private final IDipendenteRepository dipendenteRepo;
    private final IDettaglioProgettoRepository dettaglioRepo;  // <-- NUOVO

    public DipendenteService(IDipendenteRepository dipendenteRepo, 
                             IDettaglioProgettoRepository dettaglioRepo) {
        this.dipendenteRepo = dipendenteRepo;
        this.dettaglioRepo = dettaglioRepo;
    }

    public Dipendente inserimentoDipendente(Dipendente d) {
        return dipendenteRepo.save(d);
    }

    public Page<Dipendente> selectAll(Pageable pageable) {
        return dipendenteRepo.findAll(pageable);
    }

    public List<Dipendente> selectAll() {
        return dipendenteRepo.findAll();
    }

    public Dipendente selectById(Integer id) {
        return dipendenteRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dipendente non trovato!"));
    }

    public Dipendente editPersona(Dipendente d) {
        return dipendenteRepo.save(d);
    }

    @Transactional
    public void deleteById(Integer id) {
        // 1. Rimuovi il dipendente da tutti i progetti (distacco)
        dettaglioRepo.deleteByDipendenteId(id);
        // 2. Elimina il dipendente
        dipendenteRepo.deleteById(id);
    }
}