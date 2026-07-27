package com.example.demo.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "progetto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Progetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Il nome del progetto è obbligatorio!")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull(message = "La data di inizio è obbligatoria!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "data_inizio", nullable = false)
    private LocalDate dataInizio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "data_fine")
    private LocalDate dataFine;

    @PositiveOrZero(message = "Il budget deve essere maggiore o uguale a zero")
    @Column(precision = 15, scale = 2)
    private BigDecimal budget;

    @Column(length = 20)
    private String stato = "ATTIVO";

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @OneToMany(mappedBy = "progetto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DettaglioProgetto> dettagliProgetto = new ArrayList<>();
}