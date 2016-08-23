package carec2.service;

import carec2.domain.HL7Message;
import carec2.domain.Patient;
import carec2.repository.HL7MessageRepository;
import carec2.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HL7MessageServiceImpl implements HL7MessageService{

    private static final Logger log = LoggerFactory.getLogger(HL7MessageServiceImpl.class);

    @Autowired
    HL7MessageRepository hl7MessageRepository;

    @Autowired
    HL7MessageAssembler hl7MessageAssembler;

    @Autowired
    PatientRepository patientRepository;

    @Override
    public void saveMessage(String msg){
        //store hl7 messages in mongo
        try{
            HL7Message hl7Message = hl7MessageAssembler.toRawMessage(msg);
            if (hl7Message != null) {
                hl7MessageRepository.save(hl7Message);
            }
        }catch (Exception e){
            log.error("Error in ProcessorAudit: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void savePatient(Patient patient){
        patientRepository.save(patient);
    }



}
