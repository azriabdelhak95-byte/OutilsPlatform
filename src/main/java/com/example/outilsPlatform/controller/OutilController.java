package com.example.outilsPlatform.controller;

import com.example.outilsPlatform.entity.Outil;
import com.example.outilsPlatform.entity.Utilisateur;
import com.example.outilsPlatform.repository.OutilRepository;
import com.example.outilsPlatform.repository.CategorieOutilRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/outils")
public class OutilController {

    @Autowired
    private OutilRepository outilRepository;
    
    @Autowired
    private CategorieOutilRepository categorieOutilRepository;

    @GetMapping({"/transactions", "/catalogue"})
    public String catalogue(@RequestParam(required = false) String nom, Model model) {
        List<Outil> outils;
        if (nom != null && !nom.isEmpty()) {
            outils = outilRepository.findByNomContainingIgnoreCase(nom);
        } else {
            outils = outilRepository.findAll();
        }
        model.addAttribute("outils", outils);
        model.addAttribute("nom", nom);
        return "catalogue"; 
    }

    @GetMapping("/mes-outils")
    public String mesOutils(Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) return "redirect:/utilisateurs/connexion";

        model.addAttribute("outils", outilRepository.findByProprietaire(utilisateur));
        return "mes-outils"; 
    }

    // --- NOUVELLE MÉTHODE : AFFICHER LE FORMULAIRE DE MODIFICATION ---
    @GetMapping("/modifier/{id}")
    public String afficherFormModifier(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("utilisateur") == null) return "redirect:/utilisateurs/connexion";
        
        Outil outil = outilRepository.findById(id).orElseThrow();
        model.addAttribute("outil", outil);
        model.addAttribute("categories", categorieOutilRepository.findAll());
        return "modifier-outil";
    }

    // --- NOUVELLE MÉTHODE : ENREGISTRER LES CHANGEMENTS ---
    @PostMapping("/modifier")
    public String modifierOutil(@ModelAttribute Outil outil, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) return "redirect:/utilisateurs/connexion";
        
        outil.setProprietaire(utilisateur);
        outilRepository.save(outil);
        return "redirect:/outils/mes-outils";
    }

    @GetMapping("/ajouter")
    public String ajouterForm(Model model, HttpSession session) {
        if (session.getAttribute("utilisateur") == null) return "redirect:/utilisateurs/connexion";
        model.addAttribute("outil", new Outil());
        model.addAttribute("categories", categorieOutilRepository.findAll());
        return "ajout-outil";
    }

    @PostMapping("/ajouter")
    public String ajouterOutil(@ModelAttribute Outil outil, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) return "redirect:/utilisateurs/connexion";
        
        outil.setProprietaire(utilisateur);
        outilRepository.save(outil);
        return "redirect:/outils/mes-outils"; 
    }
}