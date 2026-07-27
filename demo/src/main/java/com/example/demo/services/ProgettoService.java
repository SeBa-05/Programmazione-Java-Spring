package com.example.demo.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ProgettoListDTO;
import com.example.demo.entities.DettaglioProgetto;
import com.example.demo.entities.Dipendente;
import com.example.demo.entities.Progetto;
import com.example.demo.repositories.IDettaglioProgettoRepository;
import com.example.demo.repositories.IDipendenteRepository;
import com.example.demo.repositories.IProgettoRepository;

@Service
public class ProgettoService {

    private final IProgettoRepository progettoRepo;
    private final IDettaglioProgettoRepository dettaglioRepo;
    private final IDipendenteRepository dipendenteRepo;

    public ProgettoService(IProgettoRepository progettoRepo,
                           IDettaglioProgettoRepository dettaglioRepo,
                           IDipendenteRepository dipendenteRepo) {
        this.progettoRepo = progettoRepo;
        this.dettaglioRepo = dettaglioRepo;
        this.dipendenteRepo = dipendenteRepo;
    }

    // ========== CRUD PROGETTO ==========
    public Progetto inserimentoProgetto(Progetto p) {
        if (p.getStato() == null) {
            p.setStato("ATTIVO");
        }
        return progettoRepo.save(p);
    }

    public Page<Progetto> selectAll(Pageable pageable) {
        return progettoRepo.findAll(pageable);
    }

    public List<Progetto> selectAll() {
        return progettoRepo.findAll();
    }

    public Progetto selectById(Integer id) {
        return progettoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Progetto non trovato!"));
    }

    public Progetto editProgetto(Progetto p) {
        return progettoRepo.save(p);
    }

    public void deleteById(Integer id) {
        progettoRepo.deleteById(id);
    }

    // ========== STATISTICHE PER LISTA ==========
    public Page<ProgettoListDTO> getProgettiWithStats(Pageable pageable) {
        Page<Progetto> page = progettoRepo.findAll(pageable);
        return page.map(p -> {
            ProgettoListDTO dto = new ProgettoListDTO();
            dto.setId(p.getId());
            dto.setNome(p.getNome());
            dto.setDataInizio(p.getDataInizio());
            dto.setDataFine(p.getDataFine());
            dto.setBudget(p.getBudget());
            dto.setStato(p.getStato());
            dto.setNumDipendenti(countDipendentiAssegnati(p.getId()));
            dto.setStipendioMedio(getStipendioMedio(p.getId()));
            return dto;
        });
    }

    public long countDipendentiAssegnati(Integer progettoId) {
        return dettaglioRepo.countByProgettoId(progettoId);
    }

    public double getStipendioMedio(Integer progettoId) {
        List<Dipendente> dipendenti = getDipendentiAssegnati(progettoId);
        if (dipendenti.isEmpty()) return 0.0;
        return dipendenti.stream().mapToDouble(Dipendente::getStipendio).average().orElse(0.0);
    }

    // ========== GESTIONE DIPENDENTI ==========
    public List<Dipendente> getDipendentiAssegnati(Integer progettoId) {
        return dettaglioRepo.findDipendentiAssegnati(progettoId);
    }

    public List<Dipendente> getDipendentiNonAssegnati(Integer progettoId) {
        return dettaglioRepo.findDipendentiNonAssegnati(progettoId);
    }

    @Transactional
    public DettaglioProgetto assegnaDipendente(Integer progettoId, Integer dipendenteId) {
        Progetto progetto = selectById(progettoId);
        Dipendente dipendente = dipendenteRepo.findById(dipendenteId)
                .orElseThrow(() -> new IllegalArgumentException("Dipendente non trovato!"));

        // CONTROLLO ROBUSTO: usa count per evitare problemi con duplicati
        int count = dettaglioRepo.countByDipendenteIdAndProgettoId(dipendenteId, progettoId);
        if (count > 0) {
            throw new IllegalArgumentException("Il dipendente è già assegnato a questo progetto!");
        }

        DettaglioProgetto dettaglio = new DettaglioProgetto();
        dettaglio.setProgetto(progetto);
        dettaglio.setDipendente(dipendente);
        return dettaglioRepo.save(dettaglio);
    }

    @Transactional
    public void rimuoviDipendente(Integer progettoId, Integer dipendenteId) {
        // Elimina direttamente (gestisce anche duplicati se presenti)
        dettaglioRepo.deleteByDipendenteIdAndProgettoId(dipendenteId, progettoId);
    }

    @Transactional
    public void rimuoviDipendenteDaTuttiIProgetti(Integer dipendenteId) {
        dettaglioRepo.deleteByDipendenteId(dipendenteId);
    }
}