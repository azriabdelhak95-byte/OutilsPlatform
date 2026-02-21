package com.example.outilsPlatform.entity;

import com.example.outilsPlatform.enums.TypeTransaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeTransaction typeTransaction; // Enumération : PRET, LOCATION

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    private boolean estPaye;

    // Relation avec Utilisateur (Emprunteur)
    @ManyToOne
    @JoinColumn(name = "emprunteur_id", nullable = false)
    private Utilisateur emprunteur;

    // Relation avec Outil
    @ManyToOne
    @JoinColumn(name = "outil_id", nullable = false)
    private Outil outil;
    
    // Constructeurs
     // Default Constructor (Required by Hibernate)
    public Transaction() {
    }
    
    public Transaction(TypeTransaction typeTransaction, LocalDate dateDebut, LocalDate dateFin, boolean estPaye, Utilisateur emprunteur, Outil outil) {
        this.typeTransaction = typeTransaction;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.estPaye = estPaye;
        this.emprunteur = emprunteur;
        this.outil = outil;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TypeTransaction getTypeTransaction() { return typeTransaction; }
    public void setTypeTransaction(TypeTransaction typeTransaction) { this.typeTransaction = typeTransaction; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public boolean isEstPaye() { return estPaye; }
    public void setEstPaye(boolean estPaye) { this.estPaye = estPaye; }
    public Utilisateur getEmprunteur() { return emprunteur; }
    public void setEmprunteur(Utilisateur emprunteur) { this.emprunteur = emprunteur; }
    public Outil getOutil() { return outil; }
    public void setOutil(Outil outil) { this.outil = outil; }
}
