package fr.univrouen.sepa26.repository;

import fr.univrouen.sepa26.model.DirectDebitTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectDebitTransactionRepository extends JpaRepository<DirectDebitTransaction, Long> {

    // requête SQL native pour récupérer les 10 plus récentes
    @Query(value = "SELECT * FROM direct_debit_transaction ORDER BY id_db DESC LIMIT 10", nativeQuery = true)
    List<DirectDebitTransaction> findTop10Transactions();
}