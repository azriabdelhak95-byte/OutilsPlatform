package com.example.outilsPlatform;

import com.example.outilsPlatform.entity.*;
import com.example.outilsPlatform.enums.EtatOutil;
import com.example.outilsPlatform.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class outilPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(outilPlatformApplication.class, args);
    }

    /*@Bean
    CommandLineRunner initData(UtilisateurRepository utilisateurRepository,
                               CategorieOutilRepository categorieOutilRepository,
                               OutilRepository outilRepository) {
        return args -> {
            // Créer des utilisateurs
            Utilisateur utilisateur1 = new Utilisateur("Alice", "Dupont", "alice@test.com", "password123", "Adresse 1");
            Utilisateur utilisateur2 = new Utilisateur("Bob", "Martin", "bob@test.com", "password456", "Adresse 2");
            utilisateurRepository.save(utilisateur1);
            utilisateurRepository.save(utilisateur2);

            // Créer des catégories
            CategorieOutil categorie1 = new CategorieOutil("Outils de Jardinage", "Outils pour le jardin");
            CategorieOutil categorie2 = new CategorieOutil("Outils Électriques", "Outils électriques divers");
            categorieOutilRepository.save(categorie1);
            categorieOutilRepository.save(categorie2);

            // Créer des outils
            Outil outil1 = new Outil("Tondeuse", "Tondeuse à gazon", EtatOutil.NEUF, utilisateur1, categorie1);
            Outil outil2 = new Outil("Perceuse", "Perceuse électrique 500W", EtatOutil.BON_ETAT, utilisateur2, categorie2);
            outilRepository.save(outil1);
            outilRepository.save(outil2);

            System.out.println("Données de test ajoutées avec succès !");
        };
    }*/
}
