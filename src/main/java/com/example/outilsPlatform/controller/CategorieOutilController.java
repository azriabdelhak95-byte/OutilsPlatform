package com.example.outilsPlatform.controller;

import com.example.outilsPlatform.entity.CategorieOutil;
import com.example.outilsPlatform.repository.CategorieOutilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategorieOutilController {

    @Autowired
    private CategorieOutilRepository categorieOutilRepository;

    // Liste des catégories
    @GetMapping
    public String afficherCategories(Model model) {
        model.addAttribute("categories", categorieOutilRepository.findAll());
        return "categories";
    }

    // Ajouter une catégorie
    @GetMapping("/ajouter")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("categorie", new CategorieOutil());
        return "ajout-categorie";
    }

    @PostMapping("/ajouter")
    public String ajouterCategorie(@ModelAttribute CategorieOutil categorie) {
        categorieOutilRepository.save(categorie);
        return "redirect:/categories";
    }
}
