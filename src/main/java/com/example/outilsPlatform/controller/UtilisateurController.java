package com.example.outilsPlatform.controller;

import com.example.outilsPlatform.entity.Utilisateur;
import com.example.outilsPlatform.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/accueil")
    public String accueil() {
        return "index";
    }

    @GetMapping("/inscription")
    public String afficherInscription(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "inscription";
    }

    @PostMapping("/inscription")
    public String inscription(@ModelAttribute @Valid Utilisateur utilisateur, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "inscription";
        }
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()).isPresent()) {
            model.addAttribute("erreur", "Cet email est déjà utilisé.");
            return "inscription";
        }
        utilisateurRepository.save(utilisateur);
        return "redirect:/utilisateurs/connexion";
    }

    @GetMapping("/connexion")
    public String afficherConnexion(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "connexion";
    }

    @PostMapping("/connexion")
    public String connexion(@RequestParam String email, @RequestParam String motDePasse, HttpSession session, Model model) {
        Utilisateur user = utilisateurRepository.findByEmailAndMotDePasse(email, motDePasse).orElse(null);
        if (user != null) {
            session.setAttribute("utilisateur", user);
            return "redirect:/utilisateurs/principal";
        }
        model.addAttribute("erreur", "Identifiants incorrects.");
        return "connexion";
    }

    @GetMapping("/principal")
    public String principal(Model model, HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) return "redirect:/utilisateurs/connexion";
        model.addAttribute("utilisateur", user);
        return "principal";
    }

    @GetMapping("/deconnexion")
    public String deconnexion(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/utilisateurs/accueil";
    }
}
