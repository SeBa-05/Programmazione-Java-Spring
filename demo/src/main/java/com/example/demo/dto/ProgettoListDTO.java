package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ProgettoListDTO {
    private Integer id;
    private String nome;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private BigDecimal budget;
    private String stato;
    private long numDipendenti;
    private double stipendioMedio;
}