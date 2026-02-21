package com.example.outilsPlatform.controller;

import com.example.outilsPlatform.entity.Message;
import com.example.outilsPlatform.entity.Utilisateur;
import com.example.outilsPlatform.repository.MessageRepository;
import com.example.outilsPlatform.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Formulaire pour envoyer un message
    @GetMapping("/envoyer/{destinataireId}")
    public String afficherFormulaireMessage(@PathVariable Long destinataireId, Model model) {
        Utilisateur destinataire = utilisateurRepository.findById(destinataireId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        model.addAttribute("destinataire", destinataire);
        model.addAttribute("message", new Message());
        return "envoyer-message";
    }

    @PostMapping("/envoyer")
    public String envoyerMessage(@ModelAttribute Message message, @RequestParam Long destinataireId, @RequestParam Long expediteurId) {
        Utilisateur destinataire = utilisateurRepository.findById(destinataireId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        Utilisateur expediteur = utilisateurRepository.findById(expediteurId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        message.setDestinataire(destinataire);
        message.setExpediteur(expediteur);
        messageRepository.save(message);
        return "redirect:/utilisateurs/principal";
    }
}
