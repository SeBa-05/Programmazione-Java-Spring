package com.example.demo.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "timesheet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La data è obbligatoria!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "orario_ingresso")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime orarioIngresso;

    @Column(name = "orario_usciuta")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime orarioUsciuta;

    @ManyToOne
    @JoinColumn(name = "id_account", nullable = false)
    private User account;

    @ManyToOne
    @JoinColumn(name = "id_progetto", nullable = false)
    private Progetto progetto;
}