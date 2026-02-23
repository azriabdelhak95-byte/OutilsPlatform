package com.example.outilsPlatform.service;

import com.example.outilsPlatform.entity.Outil;
import com.example.outilsPlatform.entity.Utilisateur;
import com.example.outilsPlatform.entity.Transaction;
import com.example.outilsPlatform.enums.EtatOutil;
import com.example.outilsPlatform.enums.TypeTransaction;
import com.example.outilsPlatform.repository.OutilRepository;
import com.example.outilsPlatform.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class OutilService {

    @Autowired
    private OutilRepository outilRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public void louerOutil(Long outilId, Utilisateur locataire) {
        Outil outil = outilRepository.findById(outilId)
            .orElseThrow(() -> new RuntimeException("Outil non trouve"));

        // Correction 1 & 2 : Utilisation des Enums exacts
        if (outil.getEtat() == EtatOutil.DISPONIBLE) {
            outil.setEtat(EtatOutil.INDISPONIBLE);
            outilRepository.save(outil);

            Transaction trans = new Transaction();
            trans.setOutil(outil);
            
            // Correction 3 : setEmprunteur au lieu de setLocataire (selon ton entite)
            trans.setEmprunteur(locataire); 
            
            // Correction 4 : setTypeTransaction au lieu de setType (selon ton entite)
            trans.setTypeTransaction(TypeTransaction.LOCATION);
            
            trans.setDateDebut(LocalDate.now());
            
            transactionRepository.save(trans);
        }
    }
}