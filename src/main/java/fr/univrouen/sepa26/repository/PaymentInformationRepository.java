package fr.univrouen.sepa26.repository;

import fr.univrouen.sepa26.model.PaymentInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentInformationRepository extends JpaRepository<PaymentInformation, Long> {
    // Laissez vide ! Spring va automatiquement créer les méthodes save(), findAll(), findById(), etc.
}