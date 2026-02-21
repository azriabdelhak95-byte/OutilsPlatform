package com.example.outilsPlatform.entity;

import com.example.outilsPlatform.enums.EtatOutil;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name = "outil")
public class Outil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "L'état est obligatoire")
    private EtatOutil etat; // Enum pour l'état de l'outil : NEUF, BON_ETAT, USE
    
    // Add this method to calculate availability
    @Transient
    public boolean isDisponible() {
    // Logic to determine availability, e.g., no active transaction
    return true; // Replace with actual logic
}
    // Relation avec Utilisateur (Propriétaire)
    
    @ManyToOne
    @JoinColumn(name = "proprietaire_id", nullable = false)
    private Utilisateur proprietaire;

    // Relation avec CategorieOutil
    @ManyToOne
    @JoinColumn(name = "categorie_id", nullable = false)
    private CategorieOutil categorie;

    // Relation avec Transaction (1 outil peut avoir plusieurs transactions)
    @OneToMany(mappedBy = "outil", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    // Constructeurs
    public Outil() {
    }

    public Outil(String nom, String description, EtatOutil etat, Utilisateur proprietaire, CategorieOutil categorie) {
        this.nom = nom;
        this.description = description;
        this.etat = etat;
        this.proprietaire = proprietaire;
        this.categorie = categorie;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public EtatOutil getEtat() { return etat; }
    public void setEtat(EtatOutil etat) { this.etat = etat; }
    public Utilisateur getProprietaire() { return proprietaire; }
    public void setProprietaire(Utilisateur proprietaire) { this.proprietaire = proprietaire; }
    public CategorieOutil getCategorie() { return categorie; }
    public void setCategorie(CategorieOutil categorie) { this.categorie = categorie; }
    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
}
