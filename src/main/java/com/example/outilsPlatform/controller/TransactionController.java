package com.example.outilsPlatform.controller;

import com.example.outilsPlatform.entity.Outil;
import com.example.outilsPlatform.entity.Transaction;
import com.example.outilsPlatform.entity.Utilisateur;
import com.example.outilsPlatform.enums.TypeTransaction;
import com.example.outilsPlatform.repository.TransactionRepository;
import com.example.outilsPlatform.repository.OutilRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private OutilRepository outilRepository;

    @GetMapping("/mes-ransactions")
    public String getMesTransactions(Model model, HttpSession session) {
        // Retrieve the connected user from the session
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            // Redirect to login if no user is in the session
            return "redirect:/utilisateurs/connexion";
        }

        // Fetch transactions for the connected user
        List<Transaction> mesTransactions = transactionRepository.findByEmprunteur(utilisateur);

        // Add transactions to the model
        model.addAttribute("transactions", mesTransactions);

        return "transactions"; // Name of your Thymeleaf template
    }
    
    @GetMapping("/rechercher-outils")
    public String rechercherOutils(
            @RequestParam(required = false) String nom,
            Model model, 
            HttpSession session) {

        // Retrieve the connected user from the session
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            return "redirect:/utilisateurs/connexion"; // Redirect to login if no user in session
        }

        // Fetch tools by name or all tools if no search term provided
        List<Outil> outils;
        if (nom != null && !nom.isEmpty()) {
            outils = outilRepository.findByNomContainingIgnoreCase(nom);
        } else {
            outils = outilRepository.findAll();
        }

        // Check transaction statuses for each tool
        List<Map<String, Object>> outilStatuses = outils.stream().map(outil -> {
            boolean isReserved = transactionRepository.existsByOutilAndDateFinAfter(outil, LocalDate.now());
            Map<String, Object> outilStatus = new HashMap<>();
            outilStatus.put("outil", outil);
            outilStatus.put("status", isReserved ? "Réservé" : "Disponible");
            return outilStatus;
        }).collect(Collectors.toList());

        // Add data to the model
        model.addAttribute("outilStatuses", outilStatuses);
        model.addAttribute("nom", nom); // Keep the search term
        return "transactions"; // Thymeleaf template name
    }
    
    @GetMapping("/louer/{id}")
    public String afficherFormLouer(@PathVariable Long id, Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/utilisateurs/connexion";
        }

        Outil outil = outilRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Outil introuvable"));

        model.addAttribute("outil", outil);
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("typeTransactions", TypeTransaction.values()); // Pass enum values to the model

        return "louer-outil";
    }
    
    @PostMapping("/louer")
    public String louerOutil(
            @RequestParam("outil_id") Long outilId,
            @RequestParam("dateDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Check if the user is logged in
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/utilisateurs/connexion"; // Redirect to login if not logged in
        }

        // Retrieve the tool by ID
        Outil outil = outilRepository.findById(outilId)
                .orElseThrow(() -> new IllegalArgumentException("Outil introuvable"));

        // Validate dates
        if (!dateFin.isAfter(dateDebut)) {
            redirectAttributes.addFlashAttribute("error", "La date de fin doit être postérieure à la date de début.");
            return "redirect:/transactions/louer/" + outilId;
        }

        // Check if the tool has a valid owner
        if (outil.getProprietaire() == null) {
            redirectAttributes.addFlashAttribute("error", "Cet outil n'a pas de propriétaire enregistré.");
            return "redirect:/transactions/louer/" + outilId;
        }

        // Check if the user is trying to rent their own tool
        if (outil.getProprietaire().getId().equals(utilisateur.getId())) {
            redirectAttributes.addFlashAttribute("error", "Vous ne pouvez pas emprunter vos propres outils.");
            return "redirect:/transactions/louer/" + outilId;
        }

        // Check availability
        boolean outilReserve = transactionRepository.existsByOutilAndDateFinAfterAndDateDebutBefore(outil, dateDebut, dateFin);
        if (outilReserve) {
            redirectAttributes.addFlashAttribute("error", "Cet outil est déjà réservé pour cette période.");
            return "redirect:/transactions/louer/" + outilId;
        }

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setEmprunteur(utilisateur);
        transaction.setOutil(outil);
        transaction.setDateDebut(dateDebut);
        transaction.setDateFin(dateFin);
        transaction.setEstPaye(false);
        transaction.setTypeTransaction(TypeTransaction.LOCATION);

        transactionRepository.save(transaction);

        redirectAttributes.addFlashAttribute("success", "Transaction créée avec succès !");
        return "redirect:/transactions/mes-transactions";
    }
    
    @GetMapping("/mes-transactions")
public String afficherMesTransactions(HttpSession session, Model model) {
    Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
    if (utilisateur == null) {
        return "redirect:/utilisateurs/connexion";
    }

    List<Transaction> transactions = transactionRepository.findByEmprunteur(utilisateur);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Precompute data for the view
    List<Map<String, Object>> transactionsView = transactions.stream().map(transaction -> {
        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("outilNom", transaction.getOutil().getNom());
        transactionData.put("dateDebut", transaction.getDateDebut() != null ? transaction.getDateDebut().format(formatter) : "N/A");
        transactionData.put("dateFin", transaction.getDateFin() != null ? transaction.getDateFin().format(formatter) : "N/A");
        transactionData.put("typeTransaction", transaction.getTypeTransaction());
        transactionData.put("status", transaction.getDateFin() != null && transaction.getDateFin().isBefore(LocalDate.now()) ? "Terminée" : "En cours");
        return transactionData;
    }).collect(Collectors.toList());

    model.addAttribute("transactions", transactionsView);

    return "mes-transactions";
}




}
