package carec2.repository;


import carec2.domain.Encounter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly=true)
public interface EncounterRepository extends CrudRepository<Encounter, Long> {
    @Query(value = "SELECT e.id FROM Encounter e where e.visit_nbr=?1", nativeQuery = true)
    List<Long> findByVisitNbr(String visitNbr);
}
