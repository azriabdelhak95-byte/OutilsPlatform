package com.example.outilsPlatform.repository;

import com.example.outilsPlatform.entity.Outil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutilRepository extends JpaRepository<Outil, Long> {
    List<Outil> findByProprietaireId(Long proprietaireId);
    List<Outil> findByNomContainingIgnoreCase(String nom);
}
