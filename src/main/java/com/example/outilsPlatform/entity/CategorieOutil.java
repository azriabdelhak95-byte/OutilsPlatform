package com.example.outilsPlatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "categorie_outil")
public class CategorieOutil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    private String nom;

    private String description;

    // Relation avec Outil
    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private List<Outil> outils;

    // Constructeurs
    public CategorieOutil() {
    }

    public CategorieOutil(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Outil> getOutils() { return outils; }
    public void setOutils(List<Outil> outils) { this.outils = outils; }
}
