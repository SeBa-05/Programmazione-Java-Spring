package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.ProgettoListDTO;
import com.example.demo.entities.Dipendente;
import com.example.demo.entities.Progetto;
import com.example.demo.entities.User;
import com.example.demo.services.ProgettoService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/progetti")
public class ProgettoController {

    private final ProgettoService progettoService;

    public ProgettoController(ProgettoService progettoService) {
        this.progettoService = progettoService;
    }

    private boolean isLoggedAdmin(HttpSession session) {
        User logged = (User) session.getAttribute("loggedUser");
        return logged != null && "ADMIN".equals(logged.getPermission().getType().name());
    }

    private boolean isLogged(HttpSession session) {
        return session.getAttribute("loggedUser") != null;
    }

    // ========== LISTA PROGETTI (con statistiche) ==========
    @GetMapping
    public String selectAll(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            HttpSession session) {
        if (!isLogged(session)) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, 5, Sort.by("dataInizio").descending());
        Page<ProgettoListDTO> progettiPage = progettoService.getProgettiWithStats(pageable);

        model.addAttribute("items", progettiPage.getContent());
        model.addAttribute("totalPages", progettiPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("isAdmin", isLoggedAdmin(session));

        return "progetti";
    }

    // ========== DETTAGLIO PROGETTO (con due liste) ==========
    @GetMapping("/{id}")
    public String dettaglioProgetto(@PathVariable Integer id, Model model, HttpSession session) {
        if (!isLogged(session)) {
            return "redirect:/login";
        }

        Progetto progetto = progettoService.selectById(id);
        List<Dipendente> assegnati = progettoService.getDipendentiAssegnati(id);
        List<Dipendente> nonAssegnati = progettoService.getDipendentiNonAssegnati(id);

        model.addAttribute("progetto", progetto);
        model.addAttribute("assegnati", assegnati);
        model.addAttribute("nonAssegnati", nonAssegnati);
        model.addAttribute("isAdmin", isLoggedAdmin(session));

        return "progetto-dettaglio";
    }

    // ========== CREAZIONE ==========
    @GetMapping("/new")
    public String create(Model model, HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("progetto", new Progetto());
        return "progetto-form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("progetto") Progetto progetto,
                         BindingResult result,
                         Model model,
                         HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "progetto-form";
        }

        progettoService.inserimentoProgetto(progetto);
        return "redirect:/progetti";
    }

    // ========== MODIFICA ==========
    @GetMapping("/{id}/edit")
    public String editById(@PathVariable Integer id,
                           Model model,
                           HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }

        Progetto progetto = progettoService.selectById(id);
        model.addAttribute("progetto", progetto);
        return "progetto-form";
    }

    @PostMapping("/{id}/edit")
    public String editProgetto(@PathVariable Integer id,
                               @Valid @ModelAttribute("progetto") Progetto progetto,
                               BindingResult result,
                               Model model,
                               HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "progetto-form";
        }

        progetto.setId(id);
        progettoService.editProgetto(progetto);
        return "redirect:/progetti";
    }

    // ========== ELIMINA ==========
    @GetMapping("/{id}/delete")
    public String deleteById(@PathVariable Integer id, HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }

        progettoService.deleteById(id);
        return "redirect:/progetti";
    }

    // ========== ASSEGNA DIPENDENTE (AJAX) ==========
    @PostMapping("/{id}/assegna")
    @ResponseBody
    public String assegnaDipendente(@PathVariable Integer id,
                                    @RequestParam Integer dipendenteId,
                                    HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "error: Non autorizzato";
        }

        try {
            progettoService.assegnaDipendente(id, dipendenteId);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    // ========== RIMUOVI DIPENDENTE (AJAX) ==========
    @PostMapping("/{id}/rimuovi")
    @ResponseBody
    public String rimuoviDipendente(@PathVariable Integer id,
                                    @RequestParam Integer dipendenteId,
                                    HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "error: Non autorizzato";
        }

        try {
            progettoService.rimuoviDipendente(id, dipendenteId);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}