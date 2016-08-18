package carec2.repository;


import carec2.domain.HL7Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HL7MessageRepository extends MongoRepository<HL7Message, String> {
    void delete(HL7Message deleted);

    List<HL7Message> findAll();

    HL7Message findOne(String id);

    HL7Message save(HL7Message saved);
}
