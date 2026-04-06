package fr.univrouen.sepa26.repository;

import fr.univrouen.sepa26.model.GroupHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupHeaderRepository extends JpaRepository<GroupHeader, Long> {
}