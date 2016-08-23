package carec2.repository;


import carec2.domain.HL7Message;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface HL7MessageRepository extends MongoRepository<HL7Message, String> {
}
