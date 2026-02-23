package com.example.outilsPlatform.repository;

import com.example.outilsPlatform.entity.Outil;
import com.example.outilsPlatform.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OutilRepository extends JpaRepository<Outil, Long> {
    
    // Cette ligne est OBLIGATOIRE pour que le OutilController compile
    List<Outil> findByProprietaire(Utilisateur proprietaire);
    
    // Cette ligne est OBLIGATOIRE pour la recherche dans la page transactions
    List<Outil> findByNomContainingIgnoreCase(String nom);
}
