package com.example.outilsPlatform.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class CategorieOutil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;

    // Relation : Une catégorie peut avoir plusieurs outils
    // mappedBy doit correspondre au nom du champ "categorie" dans la classe Outil
    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private List<Outil> outils;

    // Constructeur vide (obligatoire pour JPA)
    public CategorieOutil() {}

    // Constructeur pratique pour l'initialisation
    public CategorieOutil(Long id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    // Getters et Setters (Indispensables pour Thymeleaf)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Outil> getOutils() { return outils; }
    public void setOutils(List<Outil> outils) { this.outils = outils; }
}
