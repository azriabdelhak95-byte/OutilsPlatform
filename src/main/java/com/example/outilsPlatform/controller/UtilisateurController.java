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
    public String afficherConnexion(Model model, HttpSession session) {

        model.addAttribute("utilisateur", new Utilisateur());
        return "connexion";
    }
    
    @PostMapping("/connexion")
    public String connexion(@RequestParam String email,
                                   @RequestParam String motDePasse, Model model,  HttpSession session) {

        Utilisateur utilisateur = utilisateurRepository.findByEmailAndMotDePasse(email, motDePasse).orElse(null);
        if (utilisateur == null) {
            model.addAttribute("erreur", "Identifiants incorrects.");
            return "connexion";
        }
        
        // Save the authenticated user in the session
        session.setAttribute("utilisateur", utilisateur);
    
        model.addAttribute("utilisateur", utilisateur);
        return "principal";
    }

    @GetMapping("/principal")
    public String principal(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "principal";
    }

    /*@GetMapping("/deconnexion")
    public String deconnexion(HttpSession session) {
        // Invalidate the session to log out the user
        if (session != null) {
            session.invalidate();
            // Redirect to the accueil page
        }   
        return "redirect:/utilisateurs/accueil";
    }*/
}
