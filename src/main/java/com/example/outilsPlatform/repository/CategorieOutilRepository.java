package com.example.outilsPlatform.repository;

import com.example.outilsPlatform.entity.CategorieOutil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieOutilRepository extends JpaRepository<CategorieOutil, Long> {
}