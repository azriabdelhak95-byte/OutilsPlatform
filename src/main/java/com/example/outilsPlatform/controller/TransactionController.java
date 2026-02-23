package com.example.outilsPlatform.controller;

import com.example.outilsPlatform.entity.Outil;
import com.example.outilsPlatform.entity.Transaction;
import com.example.outilsPlatform.entity.Utilisateur;
import com.example.outilsPlatform.enums.TypeTransaction;
import com.example.outilsPlatform.repository.TransactionRepository;
import com.example.outilsPlatform.repository.OutilRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
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

    // AFFICHER L'HISTORIQUE (L'URL doit être /transactions/mes-transactions)
    @GetMapping("/mes-transactions")
    public String afficherMesTransactions(HttpSession session, Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) return "redirect:/utilisateurs/connexion";

        // On récupère les objets réels pour que le HTML les reconnaisse
        List<Transaction> transactions = transactionRepository.findByEmprunteur(utilisateur);
        model.addAttribute("transactions", transactions);

        return "mes-transactions"; 
    }

    // FORMULAIRE DE LOCATION
    @GetMapping("/louer/{id}")
    public String afficherFormLouer(@PathVariable Long id, Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) return "redirect:/utilisateurs/connexion";

        Outil outil = outilRepository.findById(id).orElseThrow();
        model.addAttribute("outil", outil);
        model.addAttribute("typeTransactions", TypeTransaction.values());
        return "louer-outil";
    }

    // POST : ENREGISTRER LA LOCATION
    @PostMapping("/louer")
    public String louerOutil(
            @RequestParam("outil_id") Long outilId,
            @RequestParam("dateDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam("typeTransaction") TypeTransaction type,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) return "redirect:/utilisateurs/connexion";

        Outil outil = outilRepository.findById(outilId).orElseThrow();

        Transaction transaction = new Transaction();
        transaction.setEmprunteur(utilisateur);
        transaction.setOutil(outil);
        transaction.setDateDebut(dateDebut);
        transaction.setDateFin(dateFin);
        transaction.setTypeTransaction(type);
        transaction.setEstPaye(false);

        transactionRepository.save(transaction);

        redirectAttributes.addFlashAttribute("success", "Réservation confirmée !");
        return "redirect:/transactions/mes-transactions";
    }
}