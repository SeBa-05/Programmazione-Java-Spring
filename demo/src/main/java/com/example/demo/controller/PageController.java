package com.example.demo.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.Dipendente;
import com.example.demo.services.PersonaService;

import jakarta.validation.Valid;

@Controller
public class PageController {

    private final PersonaService perService;

    public PageController(PersonaService perService) {
        this.perService = perService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("p", new Dipendente());
        return "form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("p") Dipendente d,
                         BindingResult result,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("p", d);
            return "form";
        }

        Dipendente saved = perService.inserimentoPersona(d);
        model.addAttribute("msg", "✅ Dipendente registrato con successo: ");
        model.addAttribute("pNew", saved);
        return "home";
    }
    
    @GetMapping("/gestione")
    public String selectAll(Model model) {
        model.addAttribute("items", perService.selectAll());
        return "gestione";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Dipendente dipendente = perService.findById(id);
        if (dipendente == null) {
            model.addAttribute("msg", "Dipendente non trovato");
            return "redirect:/gestione";
        }
        model.addAttribute("p", dipendente);
        return "form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") int id,
                         @Valid @ModelAttribute("p") Dipendente dForm,
                         BindingResult result,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("p", dForm);
            return "form";
        }

        Dipendente originale = perService.findById(id);
        if (originale == null) {
            model.addAttribute("msg", "Dipendente non trovato");
            return "redirect:/gestione";
        }

        // Controllo modifiche
        boolean modificato = false;
        if (!Objects.equals(originale.getNome(), dForm.getNome())) modificato = true;
        else if (!Objects.equals(originale.getCognome(), dForm.getCognome())) modificato = true;
        else if (!Objects.equals(originale.getCf(), dForm.getCf())) modificato = true;
        else if (!Objects.equals(originale.getDataDiNascita(), dForm.getDataDiNascita())) modificato = true;
        else if (originale.getStipendio() != dForm.getStipendio()) modificato = true;
        else if (!Objects.equals(originale.getEmail(), dForm.getEmail())) modificato = true;
        else if (!Objects.equals(originale.getDataDiAssunzione(), dForm.getDataDiAssunzione())) modificato = true;
        else if (originale.getTipoRuolo() != dForm.getTipoRuolo()) modificato = true;

        if (!modificato) {
            model.addAttribute("p", originale);
            model.addAttribute("msg", "Nessuna modifica effettuata.");
            return "form";
        }

        // Aggiorno
        originale.setNome(dForm.getNome());
        originale.setCognome(dForm.getCognome());
        originale.setCf(dForm.getCf());
        originale.setDataDiNascita(dForm.getDataDiNascita());
        originale.setStipendio(dForm.getStipendio());
        originale.setEmail(dForm.getEmail());
        originale.setDataDiAssunzione(dForm.getDataDiAssunzione());
        originale.setTipoRuolo(dForm.getTipoRuolo());

        perService.updatePersona(originale);

        model.addAttribute("msg", "✅ Dipendente aggiornato con successo: ");
        model.addAttribute("pNew", originale);
        return "home";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model) {
        perService.deletePersona(id);
        model.addAttribute("items", perService.selectAll());
        model.addAttribute("msg", "🗑️ Dipendente eliminato con successo (ID: " + id + ")");
        return "gestione";
    }
}