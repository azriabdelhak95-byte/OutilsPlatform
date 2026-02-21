package com.example.outilsPlatform.controller;

import com.example.outilsPlatform.entity.Evaluation;
import com.example.outilsPlatform.entity.Transaction;
import com.example.outilsPlatform.repository.EvaluationRepository;
import com.example.outilsPlatform.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Ajouter une évaluation
    @GetMapping("/ajouter/{transactionId}")
    public String afficherFormulaireAjoutEvaluation(@PathVariable Long transactionId, Model model) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction non trouvée"));
        model.addAttribute("transaction", transaction);
        model.addAttribute("evaluation", new Evaluation());
        return "ajout-evaluation";
    }

    @PostMapping("/ajouter")
    public String ajouterEvaluation(@ModelAttribute Evaluation evaluation, @RequestParam Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction non trouvée"));
        evaluation.setTransaction(transaction);
        evaluationRepository.save(evaluation);
        return "redirect:/transactions/mes-transactions";
    }
}
