package com.example.outilsPlatform.controller;

import com.example.outilsPlatform.entity.Outil;
import com.example.outilsPlatform.entity.Utilisateur;
import com.example.outilsPlatform.entity.CategorieOutil;
import com.example.outilsPlatform.enums.TypeTransaction;
import com.example.outilsPlatform.repository.OutilRepository;
import com.example.outilsPlatform.repository.CategorieOutilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.List;
@Controller
@RequestMapping("/outils")
public class OutilController {

    @Autowired
    private OutilRepository outilRepository;
    
     @Autowired
    private CategorieOutilRepository categorieOutilRepository;

     @GetMapping("/ajouter")
    public String ajouterForm(Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            // Redirect to login if no user is found in the session
            return "redirect:/utilisateurs/connexion";
        }

        // Prepare the form with an empty Outil object
        model.addAttribute("outil", new Outil());

        // Add categories to the model
        model.addAttribute("categories", categorieOutilRepository.findAll());

        // Add the utilisateur to the model
        model.addAttribute("utilisateur", utilisateur);

        return "ajout-outil"; // Name of the Thymeleaf template
    }

    @PostMapping("/ajouter")
    public String ajouter(@ModelAttribute @Valid Outil outil, 
                      BindingResult bindingResult,  
                      Model model, HttpSession session) {
        // Retrieve the logged-in user from the session
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            // Redirect to login if no user is found in the session
            return "redirect:/utilisateurs/connexion";
        }

        if (bindingResult.hasErrors()) {
            // Reload the form with validation errors
            List<CategorieOutil> categories = categorieOutilRepository.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("outil", outil);
            model.addAttribute("utilisateur", utilisateur); // Keep utilisateur in the model
            return "ajout-outil";
        }

        // Set the logged-in user as the proprietaire of the outil
        outil.setProprietaire(utilisateur);

        // Save the outil
        outilRepository.save(outil);

        // Redirect to the principal page
        return "redirect:mes-outils";
    }

    @GetMapping("/mes-outils")
    public String mesOutils(Model model) {
        // Fetch all tools along with their owners
    List<Outil> outils = outilRepository.findAll();
    model.addAttribute("outils", outils);
    return "mes-outils";
    }
    
    @GetMapping("/modifier/{id}")
    public String afficherModifierForm(@PathVariable Long id, Model model) {
        // Retrieve the tool by ID
        Outil outil = outilRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid outil ID: " + id));

        // Add the tool and categories to the model
        model.addAttribute("outil", outil);
        model.addAttribute("categories", categorieOutilRepository.findAll());

        return "modifier-outil"; // Name of your Thymeleaf template
    }
    
    @PostMapping("/modifier")
    public String modifierOutil(@ModelAttribute @Valid Outil outil, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Reload the form with validation errors
            model.addAttribute("categories", categorieOutilRepository.findAll());
            return "modifier-outil";
        }

        // Retrieve the existing outil to preserve the proprietaire
        Outil existingOutil = outilRepository.findById(outil.getId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid outil ID: " + outil.getId()));

        // Preserve the proprietaire
        outil.setProprietaire(existingOutil.getProprietaire());

        // Save the updated tool
        outilRepository.save(outil);

        // Redirect to the tools list
        return "redirect:/outils/mes-outils";
    }

    @PostMapping("/supprimer/{id}")
    public String supprimerOutil(@PathVariable Long id) {
        // Delete the tool by ID
        outilRepository.deleteById(id);

        // Redirect to the tools list
        return "redirect:/outils/mes-outils";
    }
    
    @GetMapping("/transactions")
    public String rechercherOutils(@RequestParam(required = false) String nom, Model model) {
        List<Outil> outils;

        if (nom != null && !nom.isEmpty()) {
            outils = outilRepository.findByNomContainingIgnoreCase(nom);
        } else {
            outils = outilRepository.findAll(); // Display all tools if no search term is provided
        }

        model.addAttribute("outils", outils);
        model.addAttribute("nom", nom); // Keep the search term in the input
        return "transactions"; // The name of your Thymeleaf template
    }
}
