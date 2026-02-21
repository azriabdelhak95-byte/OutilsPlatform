package com.example.outilsPlatform.repository;

import com.example.outilsPlatform.entity.Outil;
import com.example.outilsPlatform.entity.Transaction;
import com.example.outilsPlatform.entity.Utilisateur;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    public boolean existsByOutilAndDateFinAfter(Outil outil, LocalDate now);
    public List<Transaction> findByEmprunteur(Utilisateur emprunteur);

    public boolean existsByOutilAndDateFinAfterAndDateDebutBefore(Outil outil, LocalDate dateDebut, LocalDate dateFin);
}
