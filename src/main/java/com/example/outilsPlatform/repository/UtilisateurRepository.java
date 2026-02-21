package com.example.outilsPlatform.repository;

import com.example.outilsPlatform.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmailAndMotDePasse(String email, String motDePasse);

    Optional<Utilisateur> findByEmail(String email);
}

