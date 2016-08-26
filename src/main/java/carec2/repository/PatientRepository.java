package carec2.repository;


import carec2.domain.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
@Repository
public interface PatientRepository extends CrudRepository<Patient, Long>{
    @Query(value = "SELECT p.id FROM Patient p where p.corporate_mrn=?1", nativeQuery = true)
    Long findByCorporateMrn(String corporateMrn);

    @Query(value = "UPDATE Patient p SET p.firstName=?1 where p.id=?2", nativeQuery = true)
    int update(String firstName, Long id);
    void flush();
}
