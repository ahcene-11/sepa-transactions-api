package fr.univrouen.sepa26.controllers;

import fr.univrouen.sepa26.model.DirectDebitTransaction;
import fr.univrouen.sepa26.model.GroupHeader;
import fr.univrouen.sepa26.model.PaymentInformation;
import fr.univrouen.sepa26.repository.DirectDebitTransactionRepository;
import fr.univrouen.sepa26.repository.GroupHeaderRepository;
import fr.univrouen.sepa26.repository.PaymentInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class DeleteController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    @Autowired
    private DirectDebitTransactionRepository transactionRepository;
     @Autowired
     private PaymentInformationRepository paymentInformationRepository;
     @Autowired
     private GroupHeaderRepository groupHeaderRepository;

    @Transactional // Obligatoire pour lire et modifier les parents/enfants dans la même requête
    @DeleteMapping(value = "/sepa26/delete/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> deleteTransaction(@PathVariable("id") Long id) {
        try {
            // on cherche la transaction
            Optional<DirectDebitTransaction> txOpt = transactionRepository.findById(id);

            if (!txOpt.isPresent()) {
                // Si elle n'existe pas, on renvoie ERROR selon la consigne
                return ResponseEntity.ok("<Response><status>ERROR</status></Response>");
            }

            DirectDebitTransaction tx = txOpt.get();

            // On sauvegarde les parents avant de supprimer l'enfant
            PaymentInformation lot = tx.getPaymentInformation();
            GroupHeader header = (lot != null) ? lot.getGroupHeader() : null;

            // on supprime la transaction
            transactionRepository.delete(tx);

            // nettoyage du Lot s'il est vide
            if (lot != null) {
                // On retire la transaction de la liste mémoire de Java
                lot.getTransactions().remove(tx);

                if (lot.getTransactions().isEmpty()) {
                    // S'il n'y a plus de transactions, on supprime le lot
                    paymentInformationRepository.delete(lot);

                    // nettoyage du Fichier complet s'il est vide
                    if (header != null) {
                        header.getPaymentInformationList().remove(lot);

                        if (header.getPaymentInformationList().isEmpty()) {
                            // S'il n'y a plus de lots, on supprime l'en-tête (GrpHdr)
                            groupHeaderRepository.delete(header);
                        }
                    }
                }
            }
            log.info("Succès : Transaction supprimé avec succès.");
            // on renvoie l'ID supprimé et le status DELETED (Bonus : la description)
            return ResponseEntity.ok(
                    "<Response>\n" +
                            "    <id>" + id + "</id>\n" +
                            "    <status>DELETED</status>\n" +
                            "</Response>"
            );

        } catch (Exception e) {
            // En cas de problème de base de données
            log.error("Échec de la suppression : " + e.getMessage());
            return ResponseEntity.ok("<Response><status>ERROR</status></Response>");
        }
    }

}
