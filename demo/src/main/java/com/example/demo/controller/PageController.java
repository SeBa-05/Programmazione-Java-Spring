package com.example.demo.controller;

import java.util.Objects;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Dipendente;
import com.example.demo.entities.PermissionType;
import com.example.demo.entities.User;
import com.example.demo.services.DipendenteService;
import com.example.demo.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    private final DipendenteService dipService;
    private final UserService userService;

    public PageController(DipendenteService dipService, UserService userService) {
        this.dipService = dipService;
        this.userService = userService;
    }

    private boolean isLoggedAdmin(HttpSession session) {
        User logged = (User) session.getAttribute("loggedUser");
        return logged != null && "ADMIN".equals(logged.getPermission().getType().name());
    }

    private boolean isLogged(HttpSession session) {
        return session.getAttribute("loggedUser") != null;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/gestione")
    public String selectAll(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            HttpSession session) {
        if (!isLogged(session)) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, 5, Sort.by("nome").ascending());
        Page<Dipendente> dipPage = dipService.selectAll(pageable);

        model.addAttribute("items", dipPage.getContent());
        model.addAttribute("totalPages", dipPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("isAdmin", isLoggedAdmin(session));

        return "gestione";
    }

    @GetMapping("/new")
    public String create(Model model, HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("d", new Dipendente());
        model.addAttribute("users", userService.findAll());
        return "form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("d") Dipendente d,
                         BindingResult result,
                         @RequestParam(required = false) Integer userId,
                         Model model,
                         HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }

        // LOG DI DEBUG per verificare i valori
        System.out.println("=== DATI DIPENDENTE ===");
        System.out.println("Nome: " + d.getNome());
        System.out.println("Cognome: " + d.getCognome());
        System.out.println("CF: " + d.getCf());
        System.out.println("Data Nascita: " + d.getDataNascita());
        System.out.println("Stipendio: " + d.getStipendio());
        System.out.println("User ID: " + (userId != null ? userId : "null"));

        if (userId != null && userId > 0) {
            User user = userService.findById(userId).orElse(null);
            if (user != null) {
                d.setUser(user);
            }
        }

        if (result.hasErrors()) {
            result.getAllErrors().forEach(e -> System.out.println("ERRORE: " + e.getDefaultMessage()));
            model.addAttribute("d", d);
            model.addAttribute("users", userService.findAll());
            return "form";
        }

        try {
            Dipendente dNew = dipService.inserimentoDipendente(d);
            model.addAttribute("msg", "✅ Dipendente registrato con successo: ");
            model.addAttribute("dNew", dNew);
            return "home";
        } catch (Exception e) {
            System.err.println("ERRORE SALVATAGGIO: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("d", d);
            model.addAttribute("users", userService.findAll());
            model.addAttribute("msg", "Errore durante il salvataggio: " + e.getMessage());
            return "form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editById(@PathVariable Integer id,
                           Model model,
                           HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }

        Dipendente d = dipService.selectById(id);
        model.addAttribute("d", d);
        model.addAttribute("users", userService.findAll());
        return "form";
    }

    @PostMapping("/{id}/edit")
    public String editPersona(@Valid @ModelAttribute("d") Dipendente dForm,
                              BindingResult result,
                              @RequestParam(required = false) Integer userId,
                              Model model,
                              HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }

        if (userId != null && userId > 0) {
            User user = userService.findById(userId).orElse(null);
            if (user != null) {
                dForm.setUser(user);
                // RIMOSSA: dForm.setEmail(user.getEmail());  // <-- ERRORE!
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("d", dForm);
            model.addAttribute("users", userService.findAll());
            return "form";
        }

        Dipendente originale = dipService.selectById(dForm.getId());

        boolean modificato = false;
        if (!Objects.equals(originale.getNome(), dForm.getNome())) modificato = true;
        else if (!Objects.equals(originale.getCognome(), dForm.getCognome())) modificato = true;
        else if (!Objects.equals(originale.getCf(), dForm.getCf())) modificato = true;
        else if (!Objects.equals(originale.getDataNascita(), dForm.getDataNascita())) modificato = true;
        else if (originale.getStipendio() != dForm.getStipendio()) modificato = true;
        else if (!Objects.equals(originale.getUser(), dForm.getUser())) modificato = true;

        if (!modificato) {
            model.addAttribute("d", originale);
            model.addAttribute("users", userService.findAll());
            model.addAttribute("msg", "Nessuna modifica effettuata.");
            return "form";
        }

        originale.setNome(dForm.getNome());
        originale.setCognome(dForm.getCognome());
        originale.setCf(dForm.getCf());
        originale.setDataNascita(dForm.getDataNascita());
        originale.setStipendio(dForm.getStipendio());
        originale.setUser(dForm.getUser());

        dipService.editPersona(originale);

        model.addAttribute("msg", "✅ Dipendente aggiornato con successo: ");
        model.addAttribute("dNew", originale);
        return "home";
    }

    @GetMapping("/delete")
    public String deleteById(@RequestParam("id") Integer id,
                             Model model,
                             HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }

        dipService.deleteById(id);
        model.addAttribute("msg", "🗑️ Dipendente eliminato con successo!");
        return "redirect:/gestione";
    }

    // ---------- GESTIONE UTENTI ----------
    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/users/new")
    public String createUserForm(Model model, HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("user", new User());
        return "user-form";
    }

    @PostMapping("/users/new")
    public String createUser(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String email,
                             @RequestParam(defaultValue = "GUEST") String role,
                             HttpSession session,
                             Model model) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }

        try {
            PermissionType tipoRuolo = PermissionType.valueOf(role.toUpperCase());
            userService.register(username, password, email, tipoRuolo);
            model.addAttribute("msg", "✅ Utente creato con successo!");
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "user-form";
        }
    }

    @GetMapping("/users/delete")
    public String deleteUser(@RequestParam("id") Integer id, HttpSession session) {
        if (!isLoggedAdmin(session)) {
            return "redirect:/login";
        }
        userService.deleteById(id);
        return "redirect:/users";
    }
}